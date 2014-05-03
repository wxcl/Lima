package cpnv.jav1.lima;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.widget.Toast;

public class LimaDb {
// Class that allows access to the Lima database through a web service
// Author: X. Carrel
// Date: May 2014
	
	private String _wsURL; // Address of the webservice
	private String _selection; // Data retrieved from a select query (XML format)
	
	// for XML parsing of the response to a select
	XmlPullParserFactory _xmlFactoryObject;
	XmlPullParser _myparser;
	int _event;
	HashMap<String, String> _fields = new HashMap<String, String>();// The pairs key/value
	
	public LimaDb (String wsURL){
		_wsURL = wsURL;
	}
	
	public boolean connectionIsOK () {
		HashMap<String, String> postData = new HashMap<String, String>();// The data of the POST request
		String res = httpPost(postData,"testdb.php");
		if (res.equals("OK"))
			return true;
		else
		{
			Log.i ("LIMA","Database check failed: "+res);
			return false;
		}
	}
	
	public boolean select (String query){
		
		// Executes the query and returns true if it went fine
		
		HashMap<String, String> postData = new HashMap<String, String>();// The data of the POST request
		postData.put("sql", query); // $_POST['sql']=query
		_selection = httpPost(postData,"select.php");
		//Log.i("LIMA",_selection);

		// Initialise the XML parser
		_xmlFactoryObject = null;
		_myparser = null;
		_event = 0;
		try {
			_xmlFactoryObject = XmlPullParserFactory.newInstance();
			_myparser = _xmlFactoryObject.newPullParser();
			_myparser.setInput(new ByteArrayInputStream(_selection.getBytes("UTF-8")), null); // Use input string as a stream for XML input
		} catch (XmlPullParserException e) {
			Log.i("LIMA","Erreur d'initialisation du parser XML");
			return false;
		} catch (UnsupportedEncodingException e) {
			Log.i("LIMA","Mauvais encodage du XML");
			return false;
		} 
		return true;
	}
	
	public boolean moveNext()
	{
		// moves to the next record
		// returns true if a record could be read

		_fields = new HashMap<String, String>();// Zap the pairs key/value from previous record
		
		String tag=null;
		boolean eor = false;
		
		while ((_event != XmlPullParser.END_DOCUMENT) && (!eor))
		{
		   switch (_event){
              case XmlPullParser.START_TAG:
            	  tag = _myparser.getName();
		      break;
		      
              case XmlPullParser.TEXT:
            	  _fields.put(tag,_myparser.getText());
		      break;

              case XmlPullParser.END_TAG:
            	  if (_myparser.getName().equals("record")) 
            		  eor = true;
		      break;
		   }		 
		   try {
			   _event = _myparser.next();
		   } catch (XmlPullParserException e) {
				Log.i("LIMA","XML pourri");
		   } catch (IOException e) {
				Log.i("LIMA","Erreur d'IO dans le parsing XML");
		   } 					
		}

		return eor;
	}
	
	public String getField(String fname)
	{
		return _fields.get(fname);
	}
	
	private String httpPost(HashMap<String, String> data, String page){
		// synchronous post: we post in background, then wait for completion
		AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
		asyncHttpPost.execute(_wsURL+"/Lima/"+page);
		
		// now wait for completion
		while (asyncHttpPost.getMyStatus() == 0)
			try {
				Thread.sleep(100); // don't hog the CPU with a loop
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		// Handle result
		if (asyncHttpPost.getMyStatus() == 1) // success
			return asyncHttpPost.getResult();
		else
			return "AsyncHttpPost error ("+String.valueOf(asyncHttpPost.getMyStatus())+")";
	}
	
}

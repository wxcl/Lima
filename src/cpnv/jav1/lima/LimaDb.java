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

/** Class that allows access to the Lima database through a web service
*/
public class LimaDb {
	
	final String LIMA_ERROR_FIELD_DOES_NOT_EXIST = ">>>Lima error: that field does not exist<<<";
	
	public enum queryType { SELECT, INSERT, UPDATE, DELETE }
	
	private String _wsURL; // Address of the webservice
	private String _selection; // Data retrieved from a select query (XML format)
	
	// for XML parsing of the response to a select
	XmlPullParserFactory _xmlFactoryObject;
	XmlPullParser _myparser;
	int _event;
	HashMap<String, String> _fields = new HashMap<String, String>();// The pairs key/value
	
	// ================================== Constructors ==========================================

	// Constructor takes one argument: the address of the webservice
	public LimaDb (String wsURL)
	{
		_wsURL = wsURL;
	}
	
	// ================================== Public methods ==========================================

	// Returns true if the connection with the service is established
	public boolean connectionIsOK () 
	{
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
	
	// Executes the query and returns the number of affeted or selected records
	// A negative value indicates that an error occurred
	// In the case of a SELECT, the first record has *NOT* been read at this point
	public int executeQuery (String query)
	{
		HashMap<String, String> postData = new HashMap<String, String>();// The data of the POST request
		postData.put("sql", query); // $_POST['sql']=query
		_selection = httpPost(postData,"execquery.php");

		// Initialise the XML parser
		_xmlFactoryObject = null;
		_myparser = null;
		_event = 0;
		try 
		{
			_xmlFactoryObject = XmlPullParserFactory.newInstance();
			_myparser = _xmlFactoryObject.newPullParser();
			_myparser.setInput(new ByteArrayInputStream(_selection.getBytes("UTF-8")), null); // Use input string as a stream for XML input
		} 
		catch (XmlPullParserException e) 
		{
			Log.i("LIMA","Error initialising XML parser");
			return -1;
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.i("LIMA","Bad XML encoding");
			return -1;
		}
		
		return nbRecords();
	}
	
	// Reads the next record; returns false if there was no more records
	public boolean moveNext()
	{
		_fields = new HashMap<String, String>();// Zap the pairs key/value from previous record
		
		String tag=null;
		String text=null;
		boolean eor = false;
		
		while ((_event != XmlPullParser.END_DOCUMENT) && (!eor))
		{
		   switch (_event){
              case XmlPullParser.START_TAG:
            	  tag = _myparser.getName();
            	  text=null;
		      break;
		      
              case XmlPullParser.TEXT:
            	  text=_myparser.getText();
		      break;

              case XmlPullParser.END_TAG:
            	  _fields.put(tag,text);
            	  if (_myparser.getName().equals("record")) 
            		  eor = true;
		      break;
		   }		 
		   try 
		   {
			   _event = _myparser.next();
		   } 
		   catch (XmlPullParserException e) 
		   {
				Log.i("LIMA","Bad XML");
		   } 
		   catch (IOException e) 
		   {
				Log.i("LIMA","IO error parsing XML");
		   } 					
		}

		return eor;
	}
	
	// Returns the value of the field "fname" of the current record
	// If the field does not exist in the record, the return value is that of the constant LIMA_ERROR_FIELD_DOES_NOT_EXIST
	public String getField(String fname)
	{
		if (!_fields.containsKey(fname)) return LIMA_ERROR_FIELD_DOES_NOT_EXIST; // No Such Field
		return _fields.get(fname);
	}
	
	// Executes the INSERT query and returns the number of created records
	
	// ================================== Private methods ==========================================

	private String httpPost(HashMap<String, String> data, String page)
	{
		// synchronous post: we post in background, then wait for completion
		AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
		asyncHttpPost.execute(_wsURL+"/Lima/"+page);
		
		// now wait for completion
		while (asyncHttpPost.getMyStatus() == 0)
			try 
			{
				Thread.sleep(100); // don't hog the CPU with a loop
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		// Handle result
		if (asyncHttpPost.getMyStatus() == 1) // success
			return asyncHttpPost.getResult();
		else
			return "AsyncHttpPost error ("+String.valueOf(asyncHttpPost.getMyStatus())+")";
	}
	
	private int nbRecords()
	{
		// Returns the number of affected records (first tag of the XML response: <nbrecords>)
		try 
		{
			_event = _myparser.next();
			if (_event == XmlPullParser.START_TAG)
				if (_myparser.getName().equals("nbrecords")) // tag ok
				{
					_event = _myparser.next();
					if (_event == XmlPullParser.TEXT) // value
					{
						int res = Integer.parseInt(_myparser.getText());
						_event = _myparser.next();
						if (_event == XmlPullParser.END_TAG) // check tag closure
							return res; // Everything ok
					} 
				}
				else
					if (_myparser.getName().equals("error")) // server side error
					{
						_event = _myparser.next();
						Log.i("LIMA","Server error: "+_myparser.getText());
						return -1;
					}
		} 
		catch (XmlPullParserException e) 
		{
			Log.i("LIMA","Error reading XML");
		} 
		catch (IOException e) 
		{
			Log.i("LIMA","Bad XML encoding");
		}
		Log.i ("LIMA","Bad response from ws");
		return -1;
	}
	
}

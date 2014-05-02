package cpnv.jav1.lima;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import cpnv.jav1.lima.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DebugActivity extends Activity 
				   implements OnClickListener {

	// References on controls of this activity
	private Button _btn;
	private TextView _output;
	

    // Create activity event handler
	@Override
    public void onCreate(Bundle savedInstanceState) {
		// Initialize and display view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug);
        
        // Log messages (logcat)
        Log.i ("LIMA", "Started debug activity");
        // Retrieve the intent that invoked us
        Intent intent = getIntent();
        // Retrieve parameters
        String arg0 = intent.getStringExtra("arg0");
		int arg1 = intent.getIntExtra("arg1",0);
        
        // Setup event handler on action button
        _btn = (Button)findViewById(R.id.action1); 
       	_btn.setOnClickListener(this); 
        _btn = (Button)findViewById(R.id.action2); 
       	_btn.setOnClickListener(this); 
        _btn = (Button)findViewById(R.id.action3); 
       	_btn.setOnClickListener(this); 
       	
       	// Get reference on the output textview
		_output = (TextView)findViewById(R.id.outputzone);
    }

	// Any click on this screen will invoke this handler
	@Override
	public void onClick(View btn) {
		// Let's see which action must be performed
		switch (btn.getId()) 
		{
		case R.id.action1: // Add timestamp to the debug text
			Time now = new Time();
			now.setToNow();
			_output.setText(_output.getText()+"\n"+now.toString());
			break;
		case R.id.action2: // get data from web service using POST
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("sql", "Select Acronyme, Description from Acronymes Where Acronyme Like 'A%'"); // $_POST['acro']="ABR"
			AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
			asyncHttpPost.execute("http://192.168.0.20/Acronymes/XML_get_data.php");
			Log.i ("LIMA","Select * from Acronymes Where Acronyme Like 'A%'");
			
			// Wait for completion
			while (asyncHttpPost.getMyStatus() == 0) 
				try {
					Thread.sleep(500); // don't hog the CPU with a loop
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			Log.i("LIMA","Done");
			// Handle result
			if (asyncHttpPost.getMyStatus() == 1) // success
				_output.setText(_output.getText()+"\n"+ParseXML(asyncHttpPost.getResult()));
			else
				_output.setText("AsyncHttpPost error ("+String.valueOf(asyncHttpPost.getMyStatus())+")");
			break;
		case R.id.action3: // Read button text from external file
			File sdcard = Environment.getExternalStorageDirectory();
			//Get the text file
			File file = new File(sdcard,"limaprefs.txt");
			try {
			    BufferedReader br = new BufferedReader(new FileReader(file));
			    String line;
			    int bn = 0;
			    while ((line = br.readLine()) != null) {
			    	int id = getResources().getIdentifier("action"+String.valueOf(++bn), "id", getApplicationContext().getPackageName());
			    	Button b = (Button) findViewById(id);
			    	b.setText(line);
			    }
			}
			catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Fichier inexistant",Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
	
	private String ParseXML(String is)
	{
		// Sources: 
		// http://www.tutorialspoint.com/android/android_xml_parsers.htm
		// http://stackoverflow.com/questions/4436923/xml-string-parsing-in-android
		
		String res = ""; // result
		
		// Initialize XML parser
		XmlPullParserFactory xmlFactoryObject = null;
		XmlPullParser myparser = null;
		int event = 0;
		try {
			xmlFactoryObject = XmlPullParserFactory.newInstance();
			myparser = xmlFactoryObject.newPullParser();
			myparser.setInput(new ByteArrayInputStream(is.getBytes("UTF-8")), null); // Use input string as a stream for XML input
		} catch (XmlPullParserException e) {
			Toast.makeText(getApplicationContext(), "Erreur d'initialisation du parser XML",Toast.LENGTH_LONG).show();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(getApplicationContext(), "Mauvais encodage",Toast.LENGTH_LONG).show();
		} 

		// Read content
		String tag=null;
		while (event != XmlPullParser.END_DOCUMENT) 
		{
		   switch (event){
              case XmlPullParser.START_TAG:
            	  tag = myparser.getName();
		      break;
		      
              case XmlPullParser.TEXT:
		    	  res = res + tag + ": " + myparser.getText() + ", ";
		      break;

              case XmlPullParser.END_TAG:
            	  if (myparser.getName().equals("record")) 
            		  res = res.substring(0,res.length()-2) + "\n";
		      break;
		   }		 
		   try {
			event = myparser.next();
		} catch (XmlPullParserException e) {
			Toast.makeText(getApplicationContext(), "XML pourri",Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Erreur d'IO",Toast.LENGTH_LONG).show();
		} 					
		}
		return res;
	}
	
}

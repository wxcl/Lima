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
			LimaDb myDb = new LimaDb("http://192.168.0.10");
			if (!myDb.connectionIsOK())
			{
				Toast.makeText(getApplicationContext(), "Echec de la connexion à la base de données",Toast.LENGTH_LONG).show();
				return;
			}

			if(myDb.select("SELECT * FROM person"))
			{
				int n = 0;
				while (myDb.moveNext())
					_output.setText(_output.getText()+myDb.getField("personfirstname")+" "+myDb.getField("personlastname")+"\n");
			}
			
			break;
		case R.id.action2: // test database
			Time now = new Time();
			now.setToNow();
			_output.setText(_output.getText()+"\n"+now.toString());
			break;
		case R.id.action3: // 
			break;
		}
	}
		
}

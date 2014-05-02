package cpnv.jav1.lima;

import cpnv.jav1.lima.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
			_output.setText(_output.getText()+"\nAction 1");
			break;
		case R.id.action2: // get data from web service using POST
			_output.setText(_output.getText()+"\nAction 2");
			break;
		case R.id.action3: // Read button text from external file
			_output.setText(_output.getText()+"\nAction 3");
			break;
		}
	}
		
}

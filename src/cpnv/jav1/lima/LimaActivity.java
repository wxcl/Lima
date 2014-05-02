package cpnv.jav1.lima;

import cpnv.jav1.lima.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LimaActivity extends Activity
							implements OnClickListener {

	// References on controls of this activity
	private Button _btn;
    private TextView _cmdMainValidation, _cmdMainInventory, _cmdMainDistribution;
	
    // Create activity event handler
	@Override
    public void onCreate(Bundle savedInstanceState) {
		// Initialize and display view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Setup event handler on debug button
        _btn = (Button)findViewById(R.id.cmdMainDebug); 
       	_btn.setOnClickListener(this);        

       	// Setup event handler textviews (application buttons)
       	_cmdMainValidation = (TextView)findViewById(R.id.cmdMainValidation);
       	_cmdMainValidation.setOnClickListener(this);        
       	_cmdMainInventory = (TextView)findViewById(R.id.cmdMainInventory);
       	_cmdMainInventory.setOnClickListener(this);        
       	_cmdMainDistribution = (TextView)findViewById(R.id.cmdMainDistribution);
       	_cmdMainDistribution.setOnClickListener(this);        
    }
    	
	// Any click on this screen will invoke this handler
	@Override
	public void onClick(View btn) {
		
		// Let's see who called us
		switch (btn.getId()) 
		{
		case R.id.cmdMainDebug: // Debug button -> switch to debug activity
			Intent myIntent = new Intent(LimaActivity.this, DebugActivity.class);
			// Let's pass some parameters to the debug activity
			myIntent.putExtra("arg0", "arg0 is a string value, unlike arg1, which is an integer");
			myIntent.putExtra("arg1", 5);
			// Call the activity
			LimaActivity.this.startActivity(myIntent);			
			break;
		case R.id.cmdMainValidation: // Validation button -> switch to Validation activity
			break;
		case R.id.cmdMainInventory: // Inventory button -> switch to Inventory activity
			break;
		case R.id.cmdMainDistribution: // Distribution button -> switch to Distribution activity
			break;
		}
	}

}
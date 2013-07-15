package org.syc.android;


import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Intent;

public class HomeActivity extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	
		/*PriceDialog pd = new PriceDialog(this);
		pd.setItem("Track212", 210, 120,0xffffff);
		pd.show();*/
		
		//startActivity(new Intent(this,CommenceActivity.class));
		startActivity(new Intent(this,LocalActivity.class));
	}

	
	@Override
	public void onBackPressed() {
		MessageDialog exitDialog = new MessageDialog(this){
			public void onOKPressed(){
				Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(200);  
				finish();
	            System.exit(0);
			}
		};
		exitDialog.setTitle("Exit?");
		exitDialog.show();
	}

}

package org.syc.android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class CommenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commence);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		RelativeLayout screen = (RelativeLayout) findViewById(R.id.screen);
		screen.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(CommenceActivity.this,LoginActivity.class));
				//startActivity(new Intent(CommenceActivity.this,PlayScrollActivity.class));

				overridePendingTransition(R.anim.slide2,R.anim.slide1);	
				CommenceActivity.this.finish();				
			}
		});
	}
	

}

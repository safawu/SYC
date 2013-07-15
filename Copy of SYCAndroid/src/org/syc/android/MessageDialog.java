package org.syc.android;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class MessageDialog extends Dialog 
{
	public MessageDialog(Context context) 
	{
		super(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.dialog);
		Window window= getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		

		Button buttonOK = (Button) findViewById(R.id.DialogOKButton);
		buttonOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				onOKPressed();				
			}
		});
	}
	
	public void onOKPressed()
	{
		dismiss();
	}
	
	public void setTitle(String title)
	{
		TextView messageTitle = (TextView)findViewById(R.id.DialogMessageTitle);
		messageTitle.setText(title);
	}
	
	public void setMessage(String message)
	{
		TextView messageContent = (TextView)findViewById(R.id.DialogMessageContent);
		messageContent.setText(message);		
	}
	
}

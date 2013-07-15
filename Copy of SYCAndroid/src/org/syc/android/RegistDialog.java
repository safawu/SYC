package org.syc.android;

import org.syc.framework.Const.ReturnInfo;
import org.syc.framework.Kernel.KernelManager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegistDialog extends Dialog 
{

	public EditText etName;
	public EditText etPassword;
	public EditText etConfirmPassword;
	public EditText etMailAddress;
	
	public Button btnOK;
	public Button btnCancel;
	
	public RegistDialog(Context context)
	{
		super(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.dialog_regist);
			
		Window window= getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		window.setGravity(Gravity.CENTER);

		btnOK = (Button)findViewById(R.id.button_OK);
		btnCancel = (Button)findViewById(R.id.button_cancel);
		etName = (EditText)findViewById(R.id.username);
		etPassword = (EditText)findViewById(R.id.password);
		etConfirmPassword = (EditText)findViewById(R.id.confirm_password);
		etMailAddress = (EditText)findViewById(R.id.mail_addr);

		
		btnOK.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View arg0)
			{
				if(onOK() == true)
					return;
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View arg0)
			{
				dismiss();
			}
		});
	}
	
	@Override
	public void onBackPressed() 	{
		
	}
	
	public boolean onOK()
	{
		String name = etName.getText().toString();
		String pw = etPassword.getText().toString();
		String cpw = etConfirmPassword.getText().toString();
		String ma = etMailAddress.getText().toString();
		
		if(pw.equals(cpw) == false)
		{
			Toast.makeText(getContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		KernelManager.user.setName(name);
		KernelManager.user.setPassword(pw);
		ReturnInfo rtnInfo = KernelManager.regist();
		switch (rtnInfo) 
		{
		case SUCCESS:
			Toast.makeText(getContext(), "Regist Success!", Toast.LENGTH_SHORT).show();
			return true;
		case REQUEST_TIMEOUT:
			Toast.makeText(getContext(), "Request Timeout!", Toast.LENGTH_SHORT).show();
			break;
		case IO_EXCEPTION:
			Toast.makeText(getContext(), "Network error!", Toast.LENGTH_SHORT).show();
			break;
		case UNKNOWN_EXPECTION:
			Toast.makeText(getContext(), "Unknow exception!", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return false;
	}
}

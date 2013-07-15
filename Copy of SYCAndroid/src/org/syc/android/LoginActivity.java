package org.syc.android;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.syc.framework.Const.*;
import org.syc.framework.Kernel.*;
import org.syc.framework.Util.Config;
import org.syc.framework.Util.NetTool;
import org.syc.music_trading_system.Item.ExTransItem;
import org.syc.music_trading_system.Item.Music;
import org.syc.music_trading_system.Item.OriginalMusic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginActivity extends Activity 
{

	private EditText username, password;
	private Button loginBtn;
	private Button registBtn;
	private CheckBox remPswdBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		loginBtn = (Button) findViewById(R.id.button_GO);
		registBtn = (Button) findViewById(R.id.button_regist);
		remPswdBox = (CheckBox) findViewById(R.id.remember_pw);
		NetTool.serverIpAddr = "192.168.15.1";

		loginBtn.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View arg0) 
			{
				if(username.getText().toString().equals("test1"))
				{
					Global.mode = Const.LOCAL_MODE;
				}
				else {
					Global.mode = Const.NET_MODE;
				}
				if(Global.mode == Const.NET_MODE)
				{
					networkTask nt = new networkTask(LoginActivity.this);
					nt.execute();					
				}
				else 
				{
				    getLocalExList();
				    getLocalMelody();
				    getMoneyAndRank();
				    Global.config.userName = username.getText().toString();
					Global.config.password = password.getText().toString();
					Global.config.remPassword = remPswdBox.isChecked();
					Global.writeConfig();
					go2Next();
				}
				
			}
		});
		
		registBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
					showRegistDialog();
			}
		});
		
		/**load config*/
		Global.loadConfig();
		remPswdBox.setChecked(Global.config.remPassword);
		if (Global.config.remPassword == true) 
		{
			username.setText(Global.config.userName);
			password.setText(Global.config.password);
		}
	}
	
	private void showRegistDialog()
	{
		final RegistDialog rd = new RegistDialog(this);
		rd.btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				if(rd.onOK() == true)
				{
					Toast.makeText(LoginActivity.this, "Regist Success!", Toast.LENGTH_SHORT).show();
					rd.dismiss();
				}
			}
		});
		rd.show();
	}

	private ReturnInfo submit()
	{
		String usrn = username.getText().toString();
		String pswd = password.getText().toString();
		/**kernel code start*/
		KernelManager.user.setName(usrn);
		KernelManager.user.setPassword(pswd);

		/**write config*/
		Global.config.userName = usrn;
		Global.config.password = pswd;
		Global.config.remPassword = remPswdBox.isChecked();
		Global.writeConfig();
		ReturnInfo rtnInfo = KernelManager.login();
		return rtnInfo;
	}

	private void go2Next()
	{
		
		startActivity(new Intent(this,LocalActivity.class));
		overridePendingTransition(R.anim.slide2,R.anim.slide1);	
		this.finish();
	}
	
	 
	/*private void addTouchEffect(){
		RelativeLayout rl = (RelativeLayout) findViewById(R.layout.activity_commence);
		rl.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            float x = event.getX();
	            float y = event.getY();
	            return super.onTouchEvent();
	        }


	    });
	}*/
	
	class networkTask extends AsyncTask<Void, Void, ReturnInfo>
	{
        ProgressDialog pdialog;
        public networkTask(Context context)
        {
            pdialog = new ProgressDialog(context, 0);  
            pdialog.setMessage("Connecting to server...");
            pdialog.show();
        }
        @Override
        protected void onPostExecute(ReturnInfo result) 
        {
        	pdialog.dismiss(); 
        	switch (result) 
    		{
    		case SUCCESS:
    			Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
    			go2Next();
    			break;
    		case WRONG_PASSWORD:
    			Toast.makeText(LoginActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
    			break;
    		case REQUEST_TIMEOUT:
    			Toast.makeText(LoginActivity.this, "Request Timeout!", Toast.LENGTH_SHORT).show();
    			break;
    		case IO_EXCEPTION:
    			Toast.makeText(LoginActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
    			break;
    		case UNKNOWN_EXPECTION:
    			Toast.makeText(LoginActivity.this, "Unknow exception!", Toast.LENGTH_SHORT).show();
    			break;
    		default:
    			break;
    		}
        	
        }

        @Override
        protected void onPreExecute() 
        {
        }
		@Override
		protected ReturnInfo doInBackground(Void... params) 
		{
			//return ReturnInfo.SUCCESS;
			return submit();
		}

     }
	private void getLocalMelody()
	{
		KernelManager.originalMusicList.clear();
		KernelManager.originalMusicList.add(new OriginalMusic(5L, "Variation", false));
		KernelManager.originalMusicList.add(new OriginalMusic(10L, "BlueElf", false));
		KernelManager.originalMusicList.add(new OriginalMusic(15L, "CoolestEthnic", false));
		KernelManager.originalMusicList.add(new OriginalMusic(20L, "FallInMyMusic", false));
	}
	
	//TODO exception handle
	private void getLocalExList()
	{
		//long id, long sellerId, Item item, long price, long pPrice, Date pTime
		KernelManager.exList.clear();
		String date="2013-03-11 18:10:20";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = new Date();
		try {
			d1 = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KernelManager.exList.add(new ExTransItem(100L, 0L,  new Music(6L, "Variation_1", true, 5L),10L, 5L, d1));
		KernelManager.exList.add(new ExTransItem(101L, 0L,  new Music(7L, "Variation_2", true, 5L),10L, 5L, new Date()));
		KernelManager.exList.add(new ExTransItem(102L, 0L,  new Music(8L, "Variation_3", true, 5L),10L, 5L, d1));
		KernelManager.exList.add(new ExTransItem(103L, 0L,  new Music(11L, "BlueElf_1", true, 10L),10L, 5L, new Date()));
		KernelManager.exList.add(new ExTransItem(104L, 0L,  new Music(12L, "BlueElf_2", true, 10L),10L, 5L, new Date()));
		KernelManager.exList.add(new ExTransItem(105L, 0L,  new Music(16L, "CoolestEthnic_1", true, 15L),10L, 5L, new Date()));
		KernelManager.exList.add(new ExTransItem(106L, 0L,  new Music(21L, "FallInMyMusic_1", true, 20L),10L, 5L, new Date()));
		KernelManager.exList.add(new ExTransItem(107L, 0L,  new Music(22L, "FallInMyMusic_2", true, 20L),10L, 5L, new Date()));
		KernelManager.exList.add(new ExTransItem(108L, 0L,  new Music(23L, "FallInMyMusic_3", true, 20L),10L, 5L, new Date()));
	}

	private void getMoneyAndRank()
	{
		KernelManager.user.setProperty(1000L);
		KernelManager.user.setRank(3L);
	}
}


package org.syc.android;

import java.util.ArrayList;
import java.util.Date;

import org.syc.android.LoginActivity.networkTask;
import org.syc.framework.Const.Command;
import org.syc.framework.Const.ReturnInfo;
import org.syc.framework.Kernel.KernelManager;
import org.syc.framework.Kernel.TransactionItem;
import org.syc.framework.Kernel.TransactionItemList;
import org.syc.framework.Util.Config;
import org.syc.music_trading_system.Item.ExTransItem;
import org.syc.music_trading_system.Item.ExTransItemList;
import org.syc.music_trading_system.Item.Music;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayScrollActivity extends Activity
{

	ViewPager pager;
	ArrayList<View> viewList;
	TextView indicator, nameText;
	Button billboard;
	SeekBar seekBar;
	String[] itemName;
	long[] pPrice, sPrice;
	int[] color;
	int position;
	int code;
	long melodyId;
	
	TransactionItemList commodityList;
	
	ExTransItemList backupExList;
	long backupMoney;
	
	
	Intent intent;
	Bundle bundle;
	
	//TODO TEST CODE
	//String filepath = Environment.getExternalStorageDirectory().getPath();
	String filepath = Environment.getExternalStorageDirectory().getPath()+ "/SYC/";
	MediaPlayer player;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_scroll);
		intent= getIntent();

		bundle = intent.getExtras();
		code = bundle.getInt("code");
		melodyId = bundle.getLong("melodyId");

		backupExList = new ExTransItemList(KernelManager.exList);
		backupMoney = KernelManager.user.getProperty();
		getCommodityList();

		updateViewPager();

	}

	
	private void getCommodityList()
	{
		if(code == 1)
		{
			long id = bundle.getLong("exItemId");
			position = bundle.getInt("position");
			commodityList = ExTransItemList.serchItemsHaveOriId(((Music)(KernelManager.exList.searchId(id).getItem())).getOriginMusicId(), KernelManager.exList);
		}
		else if(code == 2 || code == 3)
		{
			position = bundle.getInt("position");
			commodityList = KernelManager.auction.list;
		}
	}
	
	private void updateViewPager()
	{
		viewList = new ArrayList<View>();
		indicator = (TextView) findViewById(R.id.indicator);
		LinearLayout seekBarLayout = (LinearLayout) findViewById(R.id.seekBarLayout);
		seekBar = (SeekBar) seekBarLayout.findViewById(R.id.seekBar);
		pager = (ViewPager) findViewById(R.id.viewpagerLayout);
		pager.setAdapter(new MPagerAdapter());
		player = new MediaPlayer();
		
		LayoutInflater mInflater = getLayoutInflater();		
		for (int i = 0; i < commodityList.size(); ++i)
		{
			View view = mInflater.inflate(R.layout.activity_play, null);
			viewList.add(view);
		}
		
		itemName = new String[commodityList.size()];
		pPrice = new long[commodityList.size()];
		sPrice = new long[commodityList.size()];
		color = new int[commodityList.size()];
		
		pager.setCurrentItem(position);
		
		renewView(position);

		pager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int index) 
			{
				renewView(index);
			}

			@Override
			public void onPageScrolled(int index, float arg1, int arg2){}

			@Override
			public void onPageScrollStateChanged(int arg0){}
		});
	}

	void renewView(final int index) 
	{
		try
		{
			TransactionItem currentTItem = commodityList.get(index);
			itemName[index] = currentTItem.getItem().getName();
			sPrice[index] = currentTItem.getPrice();
			color[index] = ItemColor.getColor(currentTItem.getId());
			if(code == 1)
				pPrice[index] = ((ExTransItem)currentTItem).getPuchasePrice();
			player.reset();
			//TODO TEST CODE
			if(commodityList.get(index).getItem().getId() <= 9)
				player.setDataSource(filepath + commodityList.get(index).getItem().getId() + ".m4a");
			else
				player.setDataSource(filepath + commodityList.get(index).getItem().getId() + ".mid");
			//player.setDataSource(filepath + "/a.mid");
			
			player.prepare();
			player.setLooping(Global.config.autoLoop);

			indicator.setText(Integer.toString(index + 1) + " of "
					+ Integer.toString(viewList.size()));

			nameText = (TextView) viewList.get(index).findViewById(
					R.id.itemName);
			nameText.setText(itemName[index]);

			seekBar.setProgress(0);
			seekBar.setMax(player.getDuration());
			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() 
			{
				@Override
				public void onStopTrackingTouch(SeekBar arg0)
				{
					player.seekTo(seekBar.getProgress());
					player.start();
				}

				@Override
				public void onStartTrackingTouch(SeekBar arg0)
				{
					player.pause();
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) 
				{
				}
			});

			billboard = (Button) viewList.get(index).findViewById(
					R.id.btn_billboard);
			if(currentTItem.getSellerId() == 0L)
				billboard.setBackgroundResource(R.drawable.billboard3);
			else 
				billboard.setBackgroundResource(R.drawable.purchase);
			
			billboard.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
						player.pause();
						if(commodityList.get(index).getSellerId() == 0)
						{
							showPricingDialog(index);
						}
						
						else 
						{
							showPuchaseDialog(index);
						}
				}
			});
			handler.post(start);
		} 
		catch (Exception e) 
		{
			Toast.makeText(PlayScrollActivity.this,
					"Music Service Failed to start", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			onBackPressed();
		}
	}
	
	void showPricingDialog(final int index)
	{
		final PriceDialog pd = new PriceDialog(this);
		pd.setItem(itemName[index], sPrice[index], pPrice[index], 0xaf000000 + color[index]);
		pd.done_btn.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				if (pd.getLabelPrice() != KernelManager.exList.searchId(commodityList.get(index).getId()).getPrice()) 
				{
					KernelManager.exList.searchId(commodityList.get(index).getId()).setPutOnTime(new Date());
				}
				commodityList.get(index).setPrice(pd.getLabelPrice());
				sPrice[index] = commodityList.get(index).getPrice();
				player.start();
				pd.dismiss();
			}
		});
		pd.show();
	}

	void showPuchaseDialog(final int index)
	{
		final PurchaseDialog pd = new PurchaseDialog(this);
		pd.setItem(itemName[index], sPrice[index], 0xaf000000 + color[index]);
		pd.deal_btn.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				sPrice[index] = commodityList.get(index).getPrice();
				//TODO submit to server
				ReturnInfo rtnInfo = ReturnInfo.SUCCESS;
				if(code == Const.SERVER_ACT && Global.mode == Const.NET_MODE)
				{
					rtnInfo = KernelManager.buyFromServer(commodityList.get(index));
				}	
				else if(code == Const.SHOP_ACT && Global.mode == Const.NET_MODE)
				{
					rtnInfo = KernelManager.makeTransaction(commodityList.get(index));
				}
				if(rtnInfo == ReturnInfo.SUCCESS)
				{
					commodityList.get(index).setSellerId(0L);
					pPrice[index] = commodityList.get(index).getPrice();
					sPrice[index] = commodityList.get(index).getPrice();
					ExTransItem eItem = new ExTransItem(commodityList.get(index).getId(), 0L,
							new Music(commodityList.get(index).getItem().getId(),
									commodityList.get(index).getItem().getName(),
									commodityList.get(index).getItem().isAuction(),
									melodyId), commodityList.get(index).getPrice(), 
								commodityList.get(index).getPrice(), new Date());
					KernelManager.exList.add(eItem);
					KernelManager.user.setProperty(KernelManager.user.getProperty()-commodityList.get(index).getPrice());
				}	
				if(Global.config.showDlgAftPurchase)
				{
					pd.dismiss();
					renewView(index);
					showPricingDialog(index);
				}
				else
				{
					pd.dismiss();
					renewView(index);
					player.start();
				}
					
			}
		});
		pd.cancel_btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0)
			{
				pd.dismiss();
				player.start();
			}
			
		});
		pd.show();
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		handler.removeCallbacks(start);
		handler.removeCallbacks(updatesb);
		player.release();
	}

	@Override
	public void onStop() 
	{
		super.onStop();
		player.stop();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		player.pause();
	}

	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent();
		// intent.putExtra("labelPrice", labelPrice);
		// intent.putExtra("position", pos);
		setResult(10, intent);
		//TODO submit changes
		//submitChanges();
		//net
		networkTask nt = new networkTask(PlayScrollActivity.this);
		nt.execute();
		//showMessageDlg("INFO", "UPDATE SUCCESS!");
	//	submitChanges();
		
	}

	Runnable start = new Runnable() 
	{
		@Override
		public void run() 
		{
			player.start();
			handler.post(updatesb);
		}
	};

	Runnable updatesb = new Runnable() 
	{
		@Override
		public void run() {
			seekBar.setProgress(player.getCurrentPosition());
			handler.postDelayed(updatesb, 100);
		}
	};

	class MPagerAdapter extends PagerAdapter
	{

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPager) arg0).removeView(viewList.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) 
		{
			((ViewPager) arg0).addView(viewList.get(arg1), 0);
			return viewList.get(arg1);
		}

		@Override
		public int getCount() 
		{
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) 
		{
			return arg0 == (arg1);
		}

	}
	
	private ReturnInfo submitChanges()
	{
		ReturnInfo rtnInfo = ReturnInfo.SUCCESS;
		TransactionItemList changedList = getChangedList();
		for(int i=0; i<changedList.size(); i++)
		{
			rtnInfo = KernelManager.SellItem(changedList.get(i));
			if (rtnInfo != ReturnInfo.SUCCESS)
			{
				return rtnInfo;
			}
		}
		/*Local*/
		for(int i=0; i<changedList.size(); i++)
		{
			KernelManager.exList.searchId(changedList.get(i).getId()).setPrice(changedList.get(i).getPrice()); 
		}
		/*for(int i=0; i<commodityList.size(); i++)
		{
			if(commodityList.get(i).getPrice() != 
					backupExList.searchId(commodityList.get(i).getId()).getPrice())
			rtnInfo = KernelManager.SellItem(commodityList.get(i));
		}*/
		//Toast.makeText(PlayScrollActivity.this,""+changedList.size(), Toast.LENGTH_SHORT).show();
		return rtnInfo;
	}
	/*
	private void submitChanges(int code)
	{
		if(code == 1)
		{
			for(int i=0; i<commodityList.size(); i++)
			{
				KernelManager.exList.searchId(commodityList.get(i).getId()).setPrice(commodityList.get(i).getPrice()); 
				Toast.makeText(this, "changed", Toast.LENGTH_SHORT).show();
			}
		}
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
        	//showMessageDlg("INFO", "UPDATE SUCCESS!");

        	switch (result) 
    		{
    		case SUCCESS:
    			//Toast.makeText(PlayScrollActivity.this, "Success!", Toast.LENGTH_SHORT).show();
    			//showMessageDlg("INFO", "UPDATE SUCCESS!");
    			PlayScrollActivity.this.finish();
    			break;
    		case REQUEST_TIMEOUT:
    			Toast.makeText(PlayScrollActivity.this, "Request Timeout!", Toast.LENGTH_SHORT).show();
    			break;
    		case IO_EXCEPTION:
    			Toast.makeText(PlayScrollActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
    			break;
    		case UNKNOWN_EXPECTION:
    			Toast.makeText(PlayScrollActivity.this, "Unknow exception!", Toast.LENGTH_SHORT).show();
    			break;
    		default:
    			break;
    		}  
        	pdialog.dismiss();
        }

        @Override
        protected void onPreExecute() 
        {
        }
		@Override
		protected ReturnInfo doInBackground(Void... params) 
		{
			//return ReturnInfo.SUCCESS;
			ReturnInfo rtnInfo = ReturnInfo.SUCCESS;
			if(Global.mode == Const.NET_MODE)
				rtnInfo = submitChanges();		
			
			/*KernelManager.refreshExList();
			KernelManager.refreshOriginalMusicList();
			KernelManager.refreshProperty();
			KernelManager.refreshRank();
			KernelManager.refreshSoldoutInfo();*/
			return rtnInfo;
				
		}
	}
	
	private void drawback()
	{
		KernelManager.exList = new ExTransItemList(backupExList);
		KernelManager.user.setProperty(backupMoney);
	}
	
	private TransactionItemList getChangedList()
	{
		TransactionItemList changedList = new TransactionItemList();
		for(int i=0; i<commodityList.size(); i++)
		{
			if(commodityList.get(i).getSellerId() == 0L)
			{
				if(backupExList.searchId(commodityList.get(i).getId())==null)
					changedList.add(commodityList.get(i));
				else
				{
					if(backupExList.searchId(commodityList.get(i).getId()).getPrice() !=
							commodityList.get(i).getPrice())
					{
						changedList.add(commodityList.get(i));
					}
				}
			}
		}
		return changedList;
	}
	/*
	public void showMessageDlg(String title, String msg) 
	{
		MessageDialog msgDlg = new MessageDialog(this){
			public void onOKPressed()
			{
				finish();
			}
			public void onBackPressed()
			{
				dismiss();
			}
		};
		msgDlg.setTitle(title);
		msgDlg.setMessage(msg);
		msgDlg.show();
	}*/

}

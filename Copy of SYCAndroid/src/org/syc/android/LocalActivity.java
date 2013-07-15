package org.syc.android;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.*;

import network.NWSystemMessage;

import org.syc.framework.Kernel.*;
import org.syc.framework.Util.Config;
import org.syc.framework.Util.DateManager;
import org.syc.music_trading_system.Item.*;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer;

public class LocalActivity extends Activity {
	
	private LayoutInflater inflater;
	private LinearLayout variationScroll;
	private LinearLayout scrollIndicator;
	
	private TextView property, rank;
	
	private MediaPlayer player;
	
	private ViewPager pager;
	private ArrayList<View> viewList;

	//TODO TEST CODE
	private String melodyDirectory = Environment.getExternalStorageDirectory().getPath() + "/SYC/";
	//private String melodyDirectory = Environment.getExternalStorageDirectory().getPath();
	
	
	private View prevItemView;

	private Button shopButton;
	private Button serverButton;

	private Button soldoutButton;
	private ExTransItemList variationList = new ExTransItemList();
	
	private SlidingDrawer soldout_drawer;
	private SlidingDrawer config_drawer;
	
	private CheckBox playWhenScrollCB;
	private CheckBox showDlgAftPurchaseCB;
	private CheckBox autoLoopCB;
	
	private boolean playWhenScroll; 

	
	private  ArrayList<NWSystemMessage> soldoutMsgs;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);		

		initColors();
		inflater = LayoutInflater.from(this);
		shopButton = (Button)findViewById(R.id.shop_button);
		serverButton = (Button)findViewById(R.id.server_button);
		soldoutButton = (Button)findViewById(R.id.drawer_handler_sold);
		scrollIndicator = (LinearLayout) findViewById(R.id.scrollindicator);		
		pager = (ViewPager) findViewById(R.id.item_list);
		soldout_drawer = (SlidingDrawer)findViewById(R.id.sliding_sold_info);
		variationScroll = (LinearLayout) findViewById(R.id.list_scroll_view);
		config_drawer = (SlidingDrawer)findViewById(R.id.sliding_config);
		property = (TextView) findViewById(R.id.property);
		rank = (TextView) findViewById(R.id.rank);

		if(Global.mode == Const.NET_MODE)
			doNet();
		else
			doLocal();
		
		updateMelodyViewPager();
		pager.setCurrentItem(0);
		
		config_drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()
		{
			@Override
			public void onDrawerOpened() 
			{
				playWhenScrollCB = (CheckBox)findViewById(R.id.playWhenScroll_checkBox);
				playWhenScrollCB.setChecked(Global.config.playWhenScroll);
				showDlgAftPurchaseCB = (CheckBox)findViewById(R.id.showPricingDlg_checkBox);
				showDlgAftPurchaseCB.setChecked(Global.config.showDlgAftPurchase);	
				autoLoopCB = (CheckBox)findViewById(R.id.autoLoop_checkBox);
				autoLoopCB.setChecked(Global.config.autoLoop);
			}			
		});
		config_drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener()
		{
			@Override
			public void onDrawerClosed() 
			{
				Global.config.playWhenScroll = playWhenScrollCB.isChecked();
				Global.config.showDlgAftPurchase = showDlgAftPurchaseCB.isChecked();
				Global.config.autoLoop = autoLoopCB.isChecked();
				Global.writeConfig();
			}			
		});
		
		shopButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent();
				intent.setClass(LocalActivity.this, ShopActivity.class);
				intent.putExtra("code", Const.SHOP_ACT);
				//intent.putExtra("melodyId", KernelManager.originalMusicList.get(index).getId());
				intent.putExtra("melodyId", KernelManager.originalMusicList.get(0).getId());
				startActivityForResult(intent, 123);
				overridePendingTransition(R.anim.slide6,R.anim.slide5);
			}
		});
		 
		serverButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent();
				intent.setClass(LocalActivity.this, ShopActivity.class);
				intent.putExtra("code", Const.SERVER_ACT);
				intent.putExtra("melodyId", KernelManager.originalMusicList.get(0).getId());
				startActivityForResult(intent, 123);
				overridePendingTransition(R.anim.slide4, R.anim.slide3);
			}
		});
		
		
		genIndicator();
		
		player = new MediaPlayer();
		
		updateProperty();
		updateRank();
		
		
	}

/**=====================Local Mode==========================*/
	private void doLocal()
	{

	}
	
	
/**=====================Net Mode==========================*/
	@SuppressWarnings("deprecation")
	private void doNet()
	{
		soldoutMsgs = getSoldoutInfo();
		soldout_drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()
		{
			@Override
			public void onDrawerOpened() 
			{
				soldoutMsgs = getSoldoutInfo();
				updateSoldoutInfo(soldoutMsgs);				
			}			
		});
		
		soldout_drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener()
		{
			@Override
			public void onDrawerClosed() 
			{
				KernelManager.soldoutInfoRead(soldoutMsgs);
			//	updateSoldoutInfoHandler(0);				
			}
		});
	}
/**=====================Common==========================*/
	
	
	
/*********************Melody**********************/

	private void updateMelodyViewPager()
	{
		viewList = new ArrayList<View>();
		for (int i = 0; i < KernelManager.originalMusicList.size(); i++) 
		{
			View view = inflater.inflate(R.layout.local_scroll, null);
			
			variationScroll = (LinearLayout) view.findViewById(R.id.list_scroll_view);
			variationList = getVariationList(KernelManager.originalMusicList.get(i).getId());
			updateVariationsView(variationList);
			
			viewList.add(view);			
		}
		pager.setOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageSelected(final int index)
			{
				/**设置弹簧特效*/
				
				/*if(index == 0)
				{  
		             pager.setCurrentItem(1, false);  
		        } 
				else if(index == viewList.size() - 1)
				{  
		             pager.setCurrentItem(viewList.size() - 2, false);  
		        }  */
				Toast.makeText(LocalActivity.this, KernelManager.originalMusicList.get(index).getName(), Toast.LENGTH_SHORT).show();
				
				onScrollChanged(index);
				
				if(Global.config.playWhenScroll)
					playMusic(index);
				else
				{
					if(player.isPlaying())
					{
						player.pause();
						//player.release();
					}
				}
				shopButton.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View arg0) 
					{
						Intent intent = new Intent();
						intent.setClass(LocalActivity.this, ShopActivity.class);
						intent.putExtra("code", 2);
						//intent.putExtra("melodyId", KernelManager.originalMusicList.get(index).getId());
						intent.putExtra("melodyId", KernelManager.originalMusicList.get(index).getId());
						startActivityForResult(intent, 123);
						overridePendingTransition(R.anim.slide6,R.anim.slide5);
					}
				});
				 
				serverButton.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View arg0) 
					{
						Intent intent = new Intent();
						intent.setClass(LocalActivity.this, ShopActivity.class);
						intent.putExtra("code", 3);
						intent.putExtra("melodyId", KernelManager.originalMusicList.get(index).getId());
						startActivityForResult(intent, 123);
						overridePendingTransition(R.anim.slide4, R.anim.slide3);
					}
				});
			}
			@Override
			public void onPageScrolled(int index, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		pager.removeAllViews();
		pager.setAdapter(new MPagerAdapter());
		
	}
	
	class MPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(viewList.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(viewList.get(arg1), 0);
			return viewList.get(arg1);
		}

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

	}
	
	private void playMusic(int index){
		player.reset();
		try {
			String filepath;
			if(KernelManager.originalMusicList.get(index).getId() == 5L)
				filepath = melodyDirectory + "5.m4a";
			else {
				filepath = melodyDirectory + Long.toString(KernelManager.originalMusicList.get(index).getId()) + ".mid";
			}
			//TODO:TEST CODE
			//filepath = melodyDirectory + "/a.mid";
			
			//Log.i("", filepath);
			player.setDataSource(filepath);
			
			player.prepare();
			player.setLooping(true);
			player.start();
		} catch (Exception e) {
			Toast.makeText(LocalActivity.this,
					"Music Service Failed to start", Toast.LENGTH_LONG).show();
			e.printStackTrace();			
		}
}

/*********************VariationList**********************/	


	
	private ExTransItemList getVariationList(long originMusicId) 
	{

		ExTransItemList rtnList = ExTransItemList.serchItemsHaveOriId(originMusicId, KernelManager.exList);
       // Log.i("exlist",""+rtnList.get(rtnList.size()-1).getId());
		return rtnList;
	}
	
	private void updateVariationsView(ExTransItemList variations)
	{
		variationScroll.removeAllViews();
		//variationScroll.addView(getShopItemView());
		/*if(variations.size() == 0)
			Toast.makeText(this, "No Variations!", Toast.LENGTH_SHORT).show();*/
		for (int i = 0; i < variations.size(); i++)
			variationScroll.addView(getVariationItemView(variations.get(i),i));

		//variationScroll.addView(getServerItemView());
	}
	
	private LinearLayout getVariationItemView(final ExTransItem exItem, final int pos_row)
	{
		LinearLayout variationItemLayout = (LinearLayout) inflater.inflate(R.layout.local_item, null);
		((LinearLayout) variationItemLayout).setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout block = (LinearLayout) variationItemLayout.findViewById(R.id.itemColor);
		block.setBackgroundColor(0xcf000000 + ItemColor.getColor(exItem.getId()));

		TextView name = (TextView) variationItemLayout.findViewById(R.id.itemName);
		name.setText(exItem.getItem().getName());

		TextView purchasePrice = (TextView) variationItemLayout.findViewById(R.id.itemPurchasePrice);
		purchasePrice.setText("$" + exItem.getPuchasePrice());

		TextView sellingPrice = (TextView) variationItemLayout.findViewById(R.id.itemSellingPrice);
		sellingPrice.setText("$" + exItem.getPrice());

		TextView sellingTime = (TextView) variationItemLayout.findViewById(R.id.itemSellingTime);
		sellingTime.setText(DateManager.getDeltaTimeTillNow(exItem.getPutOnTime()));
		
		variationItemLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent();
				intent.setClass(LocalActivity.this, PlayScrollActivity.class);
				intent.putExtra("code", 1);
				intent.putExtra("exItemId", exItem.getId());
				intent.putExtra("position", pos_row);
				startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			}
		});
		
		return variationItemLayout;
	}

/*********************Indicator**********************/	
	private void genIndicator()
	{
		for(int i=0; i<viewList.size(); ++i)
		{
			LayoutInflater inflater2 = LayoutInflater.from(this);
			LinearLayout indicator = (LinearLayout) inflater2.inflate(R.layout.indicator, null);
			//indicator.setLayoutParams(param);
			//indicator.setBackgroundResource(R.drawable.note2);
			Button indicatorBtn = (Button)indicator.findViewById(R.id.btn_indicator);
			final int index = i;
			indicatorBtn.setOnClickListener(new View.OnClickListener() 
			{				
				@Override
				public void onClick(View view) 
				{
					pager.setCurrentItem(index);
				}
			});
			scrollIndicator.addView(indicator);
		}
	}

	private void onScrollChanged(int index)
	{
		if(prevItemView!=null)
			prevItemView.setBackgroundResource(R.drawable.note_grey);
		
		View newButton = scrollIndicator.getChildAt(index).findViewById(R.id.btn_indicator);
		newButton.setBackgroundResource(R.drawable.note_red);
		prevItemView = newButton;
	}

/*********************Soldout Info**********************/
	private void updateSoldoutInfo(ArrayList<NWSystemMessage> msgs)
	{
		LinearLayout soldInfoLayout = (LinearLayout)findViewById(R.id.soldout_scrollview_layout);
		soldInfoLayout.removeAllViews();
		for(int i=0; i<msgs.size(); i++)
		{
			TextView entry = new TextView(this);
			entry.setText(msgs.get(i).Context);
			entry.setTextSize(20);
			entry.setTextColor(0xff000000);
			soldInfoLayout.addView(entry);
		}
	}
	
	private ArrayList<NWSystemMessage> getSoldoutInfo()
	{
		ArrayList<NWSystemMessage> msgs = new ArrayList<NWSystemMessage>();
		return KernelManager.refreshSoldoutInfo();
	}

	
/*********************Others**********************/
	private void updateProperty()
	{
		property.setText(""+KernelManager.user.getProperty());
	}
	
	private void updateRank()
	{
		rank.setText(""+KernelManager.user.getRank());
	}
	
		
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
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
		MessageDialog exitDialog = new MessageDialog(this){
			public void onOKPressed()
			{
				Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(200);  
				finish();
	            System.exit(0);
			}
			public void onBackPressed()
			{
				dismiss();
			}
		};
		exitDialog.setTitle("Exit?");
		exitDialog.setMessage("Are you sure to quit the game?");
		exitDialog.show();
	}




	private void initColors()
	{
		ItemColor.colors.add(getResources().getColor(R.color.dark_green));
		ItemColor.colors.add(getResources().getColor(R.color.dark_blue));
		ItemColor.colors.add(getResources().getColor(R.color.purple));
		ItemColor.colors.add(getResources().getColor(R.color.light_green));
		ItemColor.colors.add(getResources().getColor(R.color.light_blue));
		ItemColor.colors.add(getResources().getColor(R.color.grey));
		ItemColor.colors.add(getResources().getColor(R.color.red));		
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        updateMelodyViewPager();
        updateProperty();
      //  Log.i("exlist",""+);
	}

}


/*
private ExSellingItemList loadExListFile()
{
	if(KernelManager.auction.sellingList.size()==0)
		return new ExSellingItemList();
	ExSellingItemList list = new ExSellingItemList();
	FileInputStream fis = null;
	ObjectInputStream ois = null;
	try
	{
		openFileInput("exList.dat"); 
		ois = new ObjectInputStream(fis);
		list = (ExSellingItemList)ois.readObject();
		return list;
	}
	catch (IOException e)
	{
	}
	catch (ClassNotFoundException e) 
	{
	}
	finally
	{
		try
		{
			ois.close();	
		} catch (IOException e)
		{
		}
		try
		{
			fis.close();	
		} catch (IOException e)
		{
		}
		return null;
	}
}*/

/**本地与server比较*/
/*private void getExList()
{
	ExSellingItemList localList = loadExListFile();
	KernelManager.exList.clear();
	if(localList.size()==0)
		return;
	TransactionItemList serverList = KernelManager.auction.sellingList;
	for (int i = 0; i < serverList.size(); i++) 
	{
		ExSellingItem exItem = localList.searchId(serverList.get(i).getItem().getId());
		if( exItem != null)
			KernelManager.exList.add(exItem);
	}
}*/




/*
private String[] getSoldoutInfo()
{
	String[] infos = {"track1 was sold at 12:00 for $10", 
			 "track2 was sold at 12:00 for $10",
			 "track3 was sold at 12:00 for $10",
		     "track4 was sold at 12:00 for $10"};
	return infos;
}*/



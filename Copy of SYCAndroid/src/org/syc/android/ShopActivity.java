package org.syc.android;

import java.util.ArrayList;

import org.syc.android.PlayScrollActivity.networkTask;
import org.syc.framework.Kernel.Auction.searchArgs;
import org.syc.framework.Kernel.KernelManager;
import org.syc.framework.Kernel.TransactionItem;
import org.syc.framework.Kernel.TransactionItemList;
import org.syc.framework.Const.Command;
import org.syc.framework.Const.ReturnInfo;
import org.syc.framework.Item.*;
import org.syc.music_trading_system.Item.ExTransItem;
import org.syc.music_trading_system.Item.ExTransItemList;
import org.syc.music_trading_system.Item.Music;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ClipData.Item;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ShopActivity extends Activity 
{

	private LinearLayout hsvLayout;
	private ArrayList<Integer> colors;
	private int coloridx;
	private long melodyId;
	private Intent intent;
	private Bundle bundle;
	private int code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);

		intent = getIntent();
		bundle = intent.getExtras();
		code = bundle.getInt("code");
		//networkTask nt = new networkTask(ShopActivity.this);
		//nt.execute();
		if(Global.mode == Const.NET_MODE)
			doNet();
		else
			doLocal();
				
		hsvLayout = (LinearLayout) findViewById(R.id.hsv_layout);
		updateHSV();		
	}

/**=====================Local Mode==========================*/
	private void doLocal()
	{
		getLocalCommodityList();
	}
	
	private void getLocalCommodityList()
	{
		melodyId = bundle.getLong("melodyId");
		//(long id, long sellerId, Item item, long price, long pPrice, Date pTime)
		KernelManager.auction.list = new TransactionItemList();
		KernelManager.auction.list.add(new TransactionItem(200L, -1L, new Music(7L, "Variation_2", true, 5L), 10L));
		KernelManager.auction.list.add(new TransactionItem(201L, -1L, new Music(8L, "Variation_3", true, 5L), 15L));
		KernelManager.auction.list.add(new TransactionItem(202L, -1L, new Music(9L, "Variation_4", true, 5L), 10L));
		KernelManager.auction.list.add(new TransactionItem(203L, -1L, new Music(13L, "BlueElf_3", true, 10L), 30L));
		KernelManager.auction.list.add(new TransactionItem(204L, -1L, new Music(14L, "BlueElf_4", true, 10L), 5L));
		KernelManager.auction.list.add(new TransactionItem(205L, -1L, new Music(17L, "CoolestEthnic_2", true, 15L), 20L));
		KernelManager.auction.list.add(new TransactionItem(206L, -1L, new Music(18L, "CoolestEthnic_3", true, 15L), 10L));
		KernelManager.auction.list.add(new TransactionItem(207L, -1L, new Music(19L, "CoolestEthnic_4", true, 15L), 10L));
		KernelManager.auction.list.add(new TransactionItem(208L, -1L, new Music(22L, "FallInMyMusic_2", true, 20L), 10L));
		KernelManager.auction.list.add(new TransactionItem(209L, -1L, new Music(23L, "FallInMyMusic_3", true, 20L), 15L));
		KernelManager.auction.list.add(new TransactionItem(210L, -1L, new Music(24L, "FallInMyMusic_4", true, 20L), 10L));
		TransactionItemList list = new TransactionItemList();
		
		for(int i=0; i<KernelManager.auction.list.size(); i++)
		{
			Music music = (Music)KernelManager.auction.list.get(i).getItem();
			if(music.getOriginMusicId() == melodyId)
				list.add(KernelManager.auction.list.get(i));
		}
		KernelManager.auction.list = list;
	}
/**=====================Net Mode==========================*/
	private void doNet()
	{
		getCommodityList();
	}
	private void getCommodityList()
	{
		
		melodyId = bundle.getLong("melodyId");
		KernelManager.auction.args.resetArgs();
		KernelManager.auction.args.musicSrcID = melodyId;
		if(code == 2)
			KernelManager.getTransItemList(Command.GET_COMMODITY_LIST);
		else if(code == 3)
			KernelManager.getTransItemList(Command.GET_SERVER_MUSIC_LIST);
	}

/**=====================Common==========================*/	
	private void updateHSV()
	{
		TransactionItemList tList = KernelManager.auction.list;
		int i = 0;
		while(i < tList.size())
		{
			LinearLayout column = new LinearLayout(this);
			column.setOrientation(LinearLayout.VERTICAL);
			column.setPadding(10, 10, 10, 10);			
			LinearLayout padding = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.height = 20;
			padding.setLayoutParams(params);
			
			column.addView(newItemBlock(tList.get(i), i));
			column.addView(padding);
			i++;
			if(i < tList.size())
			{
				column.addView(newItemBlock(tList.get(i), i));
				i++;
			}
			hsvLayout.addView(column);			
		}
	}
	
	private ViewGroup newItemBlock(final TransactionItem tItem, final int positon)
	{
		String name = tItem.getItem().getName();
		long price = tItem.getPrice();
		int color = ItemColor.getColor(tItem.getItem().getId());
		
		LayoutInflater inflater = LayoutInflater.from(this);
		ViewGroup item = (ViewGroup)inflater.inflate(R.layout.block_button,null);
		
		TextView itemName = (TextView)item.findViewById(R.id.itemName);
		/*if(name.length()<8)
			itemName.setText(name);
		else
		{
			String cname = name.substring(0, 7)+"...";
			itemName.setText(cname);
		}*/
		itemName.setText(name);
		
		TextView itemPrice = (TextView)item.findViewById(R.id.itemPrice);
		itemPrice.setText("$"+price);
		
		GradientDrawable background = (GradientDrawable) item.getBackground();
		background.setColor(0xcf000000+ color);
		
		item.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View view)
			{
				String name = ((TextView) view.findViewById(R.id.itemName)).getText().toString();
				String priceLabel = ((TextView) view.findViewById(R.id.itemPrice)).getText().toString();
				priceLabel = priceLabel.substring(1, priceLabel.length());
				GradientDrawable background = (GradientDrawable) view.getBackground();
				int price = Integer.parseInt(priceLabel);		
				
				Intent i = new Intent();
				i.setClass(ShopActivity.this, PlayScrollActivity.class);
				i.putExtra("code", code);
				i.putExtra("melodyId", melodyId);
				i.putExtra("position", positon);				

				startActivityForResult(i, 2);
				overridePendingTransition(R.anim.zoom_enter,R.anim.zoom_exit);	
			}
		});
		return item;
	}
	
	
	@Override
	public void onBackPressed() 
	{
		ShopActivity.this.finish();
		if(code == Const.SHOP_ACT)
			overridePendingTransition(R.anim.slide4, R.anim.slide3);
		else
			overridePendingTransition(R.anim.slide6,R.anim.slide5);
	}
	/*class networkTask extends AsyncTask<Void, Void, ReturnInfo>
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
			getCommodityList();
			return ReturnInfo.SUCCESS;
		}

     }*/
	/*
	private void initColors(){
		colors = new ArrayList<Integer>();
		colors.add(getResources().getColor(R.color.dark_green));
		colors.add(getResources().getColor(R.color.dark_blue));
		colors.add(getResources().getColor(R.color.purple));
		colors.add(getResources().getColor(R.color.light_green));
		colors.add(getResources().getColor(R.color.light_blue));
		colors.add(getResources().getColor(R.color.grey));
		colors.add(getResources().getColor(R.color.red));
		
	}*/

}

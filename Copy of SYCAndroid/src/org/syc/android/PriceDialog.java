package org.syc.android;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PriceDialog extends Dialog
{
	

	private TextView itemName, itemSellingPrice, itemPurchasePrice;
	private GradientDrawable itemBackground;
	private long price;
	Button done_btn;
	
	public PriceDialog(Context context)
	{
		super(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.dialog_price);
		Window window= getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		window.setGravity(Gravity.CENTER);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.pricingItem);
		itemName = (TextView) layout.findViewById(R.id.itemName);
		itemSellingPrice =  (TextView) layout.findViewById(R.id.itemPrice);
		itemBackground = (GradientDrawable) layout.getBackground();
		itemPurchasePrice = (TextView) findViewById(R.id.originalPrice);
				
		done_btn = (Button) findViewById(R.id.button_OK);
		Button minus = (Button) findViewById(R.id.minus);
		Button plus = (Button) findViewById(R.id.plus);
		
		
		minus.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View arg0)
			{
				if(price>=10)
				{
					price -= 5;
					itemSellingPrice.setText("$"+Long.toString(price));	
				}
				else
					Toast.makeText(getContext(), "Price cannot be lower than 5!",
						     Toast.LENGTH_SHORT).show();
			}
		});
		
		plus.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View arg0)
			{
				price += 5;
				itemSellingPrice.setText("$"+Long.toString(price));	
			}
		});
	}
	

	public void setItem(String name, long sellingPrice, long purchasePrice, int bgColor)
	{
		//if(name.length()<8)
			itemName.setText(name);
		//else
		//{
		//	String cname = name.substring(0, 7)+"...";
		//	itemName.setText(cname);
	//	}
		price = sellingPrice;
		itemSellingPrice.setText("$"+Long.toString(sellingPrice));
		itemPurchasePrice.setText("$"+Long.toString(purchasePrice));
		itemBackground.setColor(0xff000000 + bgColor);
		itemBackground.invalidateSelf();
	}
	
	public long getLabelPrice()
	{
		return price;
	}
	
	
	@Override
	public void onBackPressed() 
	{
		
	}

}

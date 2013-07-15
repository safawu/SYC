package org.syc.android;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PurchaseDialog extends Dialog {	

	private TextView itemName, itemPurchasePrice;
	private GradientDrawable itemBackground;
	Button deal_btn, cancel_btn;
	
	public PurchaseDialog(Context context) {
		super(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		setContentView(R.layout.dialog_purchase);
		Window window= getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		window.setGravity(Gravity.CENTER);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.pricingItem);
		itemName = (TextView) layout.findViewById(R.id.itemName);
		itemPurchasePrice = (TextView) findViewById(R.id.itemPrice);
		itemBackground = (GradientDrawable) layout.getBackground();
				
		deal_btn = (Button) findViewById(R.id.button_deal);
		cancel_btn = (Button) findViewById(R.id.button_cancel);		
	}
	

	public void setItem(String name, long purchasePrice, int bgColor){
		//if(name.length()<8)
			itemName.setText(name);
		//else{
	//		String cname = name.substring(0, 7)+"...";
	//		itemName.setText(cname);
	//	}
		itemPurchasePrice.setText("$"+Long.toString(purchasePrice));
		itemBackground.setColor(0xff000000 + bgColor);
		itemBackground.invalidateSelf();
	}

	@Override
	public void onBackPressed() {
		dismiss();
	}

}

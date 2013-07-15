package org.syc.android;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {

	Button btn_billboard;
	SeekBar seekBar;

	String filepath = Environment.getExternalStorageDirectory()
			.getPath() + "/" + "Bach.mid";
	
	String itemName;
	long itemPrice, labelPrice;
	int color, pos;

	MediaPlayer player;

	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		Bundle data  = getIntent().getExtras();
		
		itemName = data.getString("itemName");
		itemPrice = data.getLong("itemPrice");
		labelPrice = data.getLong("labelPrice");
		color = data.getInt("color", Color.RED);
		pos = data.getInt("position");
		

		player = new MediaPlayer();
		try {
			player.setDataSource(filepath);
			player.prepare();
			player.setLooping(false);
			handler.post(start);
		} catch (Exception e) {
			Toast.makeText(this, "Music Service Failed to start",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
			onBackPressed();
		}

		TextView nameText = (TextView) findViewById(R.id.itemName);
		nameText.setText(itemName);
		LinearLayout seekBarLayout = (LinearLayout) findViewById(R.id.seekBarLayout); 
		seekBar = (SeekBar) seekBarLayout.findViewById(R.id.seekBar);
		seekBar.setMax(player.getDuration());
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				player.seekTo(seekBar.getProgress());
				player.start();
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				player.pause();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});

		btn_billboard = (Button) findViewById(R.id.btn_billboard);
		btn_billboard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (player.isPlaying()) {
					player.pause();
					showPricingDialog();
				} else {
					player.start();
				}
			}
		});
		
	}

	void showPricingDialog() {
		final PriceDialog pd = new PriceDialog(this);
		pd.setItem(itemName, labelPrice, itemPrice, 0xaf000000 + color);
		pd.done_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				labelPrice = pd.getLabelPrice();
				player.start();
				pd.dismiss();
			}
		});
		pd.show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(start);
		handler.removeCallbacks(updatesb);
		player.release();
	}

	@Override
	public void onStop() {
		super.onStop();
		player.stop();
	}

	@Override
	public void onPause() {
		super.onPause();
		player.pause();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("labelPrice", labelPrice);
		intent.putExtra("position", pos);
		setResult(10, intent);
		PlayActivity.this.finish();
	}

	Runnable start = new Runnable() {
		@Override
		public void run() {
			player.start();
			handler.post(updatesb);
		}
	};
	
	Runnable updatesb = new Runnable() {
		@Override
		public void run() {
			seekBar.setProgress(player.getCurrentPosition());
			handler.postDelayed(updatesb, 100);
		}
	};

}

package com.webgentechnologies.nepatextdeals;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends ApplicationActivity implements OnTouchListener {

	private Button Checkin, Redeem;
	View senceTouch;
	MyCount timerCount;
	ImageView imageView2;
	TextView welcome1, welcome2, footer;
	String imagelogo;
	private ProgressBar ProgressBar1;
	EditText textToProceed;
	boolean allowed = false;
	int push_button=2;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_mainscreen);
		addListenerOnButton();
		senceTouch = findViewById(R.id.layout_mainscreen);
		senceTouch.setOnTouchListener(this);





		SharedPreferences pref = this.getSharedPreferences("NepaTextDealsPref", Context.MODE_PRIVATE);
		String free_gift1 = pref.getString("free_gift", null);
		String no_of_checkin1 = pref.getString("no_of_checkin", null);
		String disclaimer_message1 = pref.getString("disclaimer_message", null);
		String business_logo1 = pref.getString("business_logo", null);
		push_button = Integer.parseInt(pref.getString("button_push_for_checkins","2"));
		imagelogo = business_logo1;

		timerCount = new MyCount(20 * 1000, 1000);
		timerCount.start();


		textToProceed = (EditText)findViewById(R.id.textToProceed);
		textToProceed.setInputType(InputType.TYPE_NULL);
		EditText.OnEditorActionListener exampleListener = new TextView.OnEditorActionListener() {

			public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL
						&& event.getAction() == KeyEvent.ACTION_DOWN && push_button == 1) {

					System.out.print("CLICKED");
					allowed = true;
					//match this behavior to your 'Send' (or Confirm) button
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							allowed = false;
						}
					},10000);
				}
				return true;
			}
		};
		textToProceed.setOnEditorActionListener(exampleListener);
		if(imagelogo == null){

			//imageView2 = (ImageView) findViewById(R.id.imageView2);
			ProgressBar1.setVisibility(View.VISIBLE);

		} else if(imagelogo != null) {

			ProgressBar1.setVisibility(View.GONE);
			new DownloadImageTask3((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);

		}

		footer = (TextView) findViewById(R.id.footer);
		footer.setText("By Signing Up You Agree To Receive Up To " +disclaimer_message1 + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");

		welcome2 = (TextView) findViewById(R.id.welcome2);
		welcome2.setText(free_gift1);
		welcome2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
		welcome2.setTypeface(null, Typeface.BOLD);

		welcome1 = (TextView) findViewById(R.id.welcome1);
		welcome1.setText(no_of_checkin1 + " Check In's Earns a FREE");

		String fontPath = "fonts/helvetica67medium.ttf";
		TextView txt2 = (TextView) findViewById(R.id.welcome2);
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		txt2.setTypeface(tf);

		TextView txt = (TextView) findViewById(R.id.welcome);
		Typeface.createFromAsset(getAssets(), fontPath);
		txt.setTypeface(tf);

		TextView txt1 = (TextView) findViewById(R.id.welcome1);
		Typeface.createFromAsset(getAssets(), fontPath);
		txt1.setTypeface(tf);

		TextView footer = (TextView) findViewById(R.id.footer);
		Typeface.createFromAsset(getAssets(), fontPath);
		footer.setTypeface(tf);

		TextView redeem = (TextView) findViewById(R.id.redeem);
		Typeface.createFromAsset(getAssets(), fontPath);
		redeem.setTypeface(tf);

		TextView checkin = (TextView) findViewById(R.id.checkin);
		Typeface.createFromAsset(getAssets(), fontPath);
		checkin.setTypeface(tf);


	}

	class DownloadImageTask3 extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask3(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
        /*String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;*/
			return GlobalClass.logo;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		textToProceed.requestFocus();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
			Log.i("", "Dispath event power");
			Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			sendBroadcast(closeDialog);
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		Log.d("Focus debug", "Focus changed !");

		if(!hasFocus) {
			Log.d("Focus debug", "Lost focus !");

			Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			sendBroadcast(closeDialog);
		}
	}


	private void  addListenerOnButton() {
		Checkin = (Button) findViewById(R.id.checkin);
		Redeem = (Button) findViewById(R.id.redeem);
		ProgressBar1 = (ProgressBar)findViewById(R.id.ProgressBar1);
		ProgressBar1.setVisibility(View.GONE);


		//Onclick start new Checkin up activity
		Checkin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if(push_button == 2)
				{
					allowed=true;
				}

				if(allowed)
				{
					Intent i = new Intent(MainScreenActivity.this, CheckinActivity.class);
					startActivity(i);
					MainScreenActivity.this.finish();
					timerCount.cancel();
				}
				else
				{
					Toast toast = Toast.makeText(MainScreenActivity.this,"Authorization Required, See Attendant", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					LinearLayout toastLayout = (LinearLayout) toast.getView();
					TextView toastTV = (TextView) toastLayout.getChildAt(0);
					toastTV.setTextSize(45);
					toastTV.setTextColor(Color.WHITE);
					toast.getView().setBackgroundResource(R.drawable.customtoast);
					toast.show();
					textToProceed.requestFocus();
				}

			}

		});

		//Onclick start new Checkin up activity
		Redeem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(MainScreenActivity.this, RedeemActivity.class);
				startActivity(i);
				MainScreenActivity.this.finish();
				timerCount.cancel();
			}

		});


	}




	public class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			//some script here
			Intent mainIntent = new Intent(MainScreenActivity.this, MainActivity.class);
			MainScreenActivity.this.startActivity(mainIntent);
			MainScreenActivity.this.finish();

		}

		@Override
		public void onTick(long millisUntilFinished) {
			//some script here

			//timerCount.cancel();
			//timerCount.start();
		}
	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		timerCount.cancel();
		//timerCount.start();

		return false;
	}



}

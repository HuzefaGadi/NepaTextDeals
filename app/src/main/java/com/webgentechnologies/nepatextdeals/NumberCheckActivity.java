package com.webgentechnologies.nepatextdeals;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
 
public class NumberCheckActivity extends ApplicationActivity implements OnTouchListener {
	
	private Button Buttonreturn;
	TextView messagenumbercheckin1, messagenumbercheckin11, messagenumbercheckin2, footer;
	View senceTouch;
	MyCount timerCount;
	ImageView imageView2;
	String imagelogo;
	private ProgressBar ProgressBar1;
	boolean kioskMode;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_numbercheckin);
		addListenerOnButton();
		StrictMode.enableDefaults();
		senceTouch = findViewById(R.id.layout_numbercheckin); 
	    senceTouch.setOnTouchListener(this);
	    timerCount = new MyCount(20 * 1000, 1000);
	    timerCount.start();
		
		SharedPreferences pref = this.getSharedPreferences("NepaTextDealsPref", Context.MODE_PRIVATE);
		String no_of_checkin1 = pref.getString("no_of_checkin", null);
		String subscriber_no_of_checkin1 = pref.getString("subscriber_no_of_checkin", null);
		String business_logo1 = pref.getString("business_logo", null);

		String kioskModeString = pref.getString("kiosk_mode", "1");
		if (kioskModeString.equals("2")) {
			kioskMode = false;
		} else {
			kioskMode = true;
		}

        imagelogo = business_logo1;
		
		if(imagelogo == null){
		
		//imageView2 = (ImageView) findViewById(R.id.imageView2);
		ProgressBar1.setVisibility(View.VISIBLE);
		
		} else if(imagelogo != null) {
		
		ProgressBar1.setVisibility(View.GONE);
		new DownloadImageTask4((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);
		
		}
		SharedPreferences pref1 = this.getSharedPreferences("NepaTextDealsPref", Context.MODE_PRIVATE);
		if(kioskMode)
		{
			int myNum = 0;

			try {
				myNum = Integer.parseInt(no_of_checkin1);
			} catch(NumberFormatException nfe) {
				// Handle parse error.
			}

			int myNum2 = 0;

			try {
				myNum2 = Integer.parseInt(subscriber_no_of_checkin1);
			} catch(NumberFormatException nfe) {
				// Handle parse error.
			}

			int myNum3 = myNum - myNum2 ;

			messagenumbercheckin1 = (TextView) findViewById(R.id.messagenumbercheckin1);
			messagenumbercheckin1.setText("You Have Checked In " +subscriber_no_of_checkin1 + " Times. You Have " +myNum3 +" More Check-Ins Before You Earn Your FREE Reward.");

			messagenumbercheckin11 = (TextView) findViewById(R.id.messagenumbercheckin11);
			messagenumbercheckin11.setText(no_of_checkin1 + " Check-Ins Earn a FREE");


			String free_gift1 = pref1.getString("free_gift", null);
			messagenumbercheckin2 = (TextView) findViewById(R.id.messagenumbercheckin2);
			messagenumbercheckin2.setText(free_gift1);
			messagenumbercheckin2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
			messagenumbercheckin2.setTypeface(null, Typeface.BOLD);

		}
		else
		{
			messagenumbercheckin11 = (TextView) findViewById(R.id.messagenumbercheckin11);
			messagenumbercheckin11.setText("");

			messagenumbercheckin2 = (TextView) findViewById(R.id.messagenumbercheckin2);
			messagenumbercheckin2.setText("");

			messagenumbercheckin1 = (TextView) findViewById(R.id.messagenumbercheckin1);
			messagenumbercheckin1.setText(R.string.messagenumbercheckin1ForSignUp);

		}





		String disclaimer_message1 = pref1.getString("disclaimer_message", null);

		footer = (TextView) findViewById(R.id.footer);
		footer.setText("By Signing Up You Agree To Receive Up To " +disclaimer_message1 + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");
		
		String fontPath = "fonts/helvetica67medium.ttf";
        TextView txt = (TextView) findViewById(R.id.messagenumbercheckin);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt.setTypeface(tf);

		txt.setText(kioskMode ? R.string.messagenumbercheckin : R.string.messagenumbercheckinForSignUp);
        
        TextView footer = (TextView) findViewById(R.id.footer);
        Typeface.createFromAsset(getAssets(), fontPath);
        footer.setTypeface(tf);
        
        TextView buttonreturn = (TextView) findViewById(R.id.buttonreturn);
        Typeface.createFromAsset(getAssets(), fontPath);
        buttonreturn.setTypeface(tf);
        
        TextView txt1 = (TextView) findViewById(R.id.messagenumbercheckin1);
        Typeface.createFromAsset(getAssets(), fontPath);
        txt1.setTypeface(tf);


        TextView txt11 = (TextView) findViewById(R.id.messagenumbercheckin11);
        Typeface.createFromAsset(getAssets(), fontPath);
        txt11.setTypeface(tf);

        
        TextView txt2 = (TextView) findViewById(R.id.messagenumbercheckin2);
        Typeface.createFromAsset(getAssets(), fontPath);
        txt2.setTypeface(tf);



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

	
	
	private void addListenerOnButton() {
		// TODO Auto-generated method stub
        Buttonreturn = (Button) findViewById(R.id.buttonreturn);
        ProgressBar1 = (ProgressBar)findViewById(R.id.ProgressBar1);
		ProgressBar1.setVisibility(View.GONE);
		
		Buttonreturn.setOnClickListener(new OnClickListener() {
       			 
                    @Override
                    public void onClick(View view) {
         
                    	Intent i = new Intent(NumberCheckActivity.this, MainActivity.class);
                    	startActivity(i);
                    	NumberCheckActivity.this.finish();
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
	        Intent mainIntent = new Intent(NumberCheckActivity.this, MainActivity.class);
	        NumberCheckActivity.this.startActivity(mainIntent);
	        NumberCheckActivity.this.finish();
            
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

class DownloadImageTask4 extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

	public DownloadImageTask4(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	protected Bitmap doInBackground(String... urls) {

		if (GlobalClass.logo != null) {
			return GlobalClass.logo;
		}
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		return mIcon11;


	}
//824516402
	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
		GlobalClass.logo = result;
	}
}
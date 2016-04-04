package com.webgentechnologies.nepatextdeals;



import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.webgentechnologies.nepatextdeals.beans.UrlResponse;
import com.webgentechnologies.nepatextdeals.utils.Constants;
import com.webgentechnologies.nepatextdeals.utils.GlobalClass;

public class SuccessCheckActivity extends ApplicationActivity implements OnTouchListener {
	
private Button buttonReturn;
TextView messagesuccesscheckin1, footer;
View senceTouch;
MyCount timerCount;
ImageView imageView2;
String imagelogo,disclaimer_message,business_background_img,no_of_checkin,business_logo;
private ProgressBar progressBar;
	RelativeLayout relativeLayout;
	UrlResponse urlResponse;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_successcheckin);
		addListenerOnButton();
		senceTouch = findViewById(R.id.layout_successcheckin); 
	    senceTouch.setOnTouchListener(this);
	    timerCount = new MyCount(20 * 1000, 1000);
	    timerCount.start();
		
		SharedPreferences pref = this.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		urlResponse = new Gson().fromJson(pref.getString(Constants.URL_RESPONSE_BEAN,""),UrlResponse.class);
		if(urlResponse!=null)
		{
			disclaimer_message = urlResponse.getDisclaimer_message();
			business_background_img = urlResponse.getBusiness_background_img();
			no_of_checkin = urlResponse.getNo_of_checkin();
			business_logo = urlResponse.getBusiness_logo();
		}
		relativeLayout = (RelativeLayout) findViewById(R.id.layout_successcheckin);
		if (business_background_img != null) {
			new DownloadBackGroundTask(relativeLayout, this, false).execute(business_background_img);
		}
		
		footer = (TextView) findViewById(R.id.footer);
		footer.setText("By Signing Up You Agree To Receive Up To " +disclaimer_message + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");


		
		imagelogo = business_logo;
		
		if(imagelogo == null){
			
		//imageView2 = (ImageView) findViewById(R.id.imageView2);
		progressBar.setVisibility(View.VISIBLE);
			
		} else if(imagelogo != null) {
			
		progressBar.setVisibility(View.GONE);
		new DownloadImageTask5((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);
			
		}
		
		messagesuccesscheckin1 = (TextView) findViewById(R.id.messagesuccesscheckin1);
		messagesuccesscheckin1.setText("You Have Checked In " +no_of_checkin + " Times And Earned Your Reward For Being Our Loyal Customer.");
		
		
		String fontPath = "fonts/helvetica67medium.ttf";
        TextView txt = (TextView) findViewById(R.id.messagesuccesscheckin);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt.setTypeface(tf);
        
        TextView footer = (TextView) findViewById(R.id.footer);
        footer.setTypeface(tf);
        
        TextView buttonreturn = (TextView) findViewById(R.id.buttonreturn);
        buttonreturn.setTypeface(tf);
        
        TextView txt1 = (TextView) findViewById(R.id.messagesuccesscheckin1);
        txt1.setTypeface(tf);
        
        TextView txt2 = (TextView) findViewById(R.id.messagesuccesscheckin2);
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
		buttonReturn = (Button) findViewById(R.id.buttonreturn);
		progressBar = (ProgressBar)findViewById(R.id.ProgressBar1);
		progressBar.setVisibility(View.GONE);
		
		buttonReturn.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View view) {

				Intent i = new Intent(SuccessCheckActivity.this, MainActivity.class);
				startActivity(i);
				SuccessCheckActivity.this.finish();
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
	    	  Intent mainIntent = new Intent(SuccessCheckActivity.this, MainActivity.class);
	    	  SuccessCheckActivity.this.startActivity(mainIntent);
	    	  SuccessCheckActivity.this.finish();
            
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

class DownloadImageTask5 extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask5(ImageView bmImage) {
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

	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
		GlobalClass.logo = result;
	}
	class DownloadBackGroundTask extends AsyncTask<String, Void, Bitmap> {
		RelativeLayout relativeLayout;
		Context context;
		boolean forceUpdate;

		public DownloadBackGroundTask(RelativeLayout relativeLayout,Context context,boolean forceUpdate) {
			this.relativeLayout = relativeLayout;
			this.context = context;
			this.forceUpdate = forceUpdate;
		}

		protected Bitmap doInBackground(String... urls) {

			if (GlobalClass.background != null && !forceUpdate) {
				return GlobalClass.background;
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

		protected void onPostExecute(Bitmap result) {
			Drawable drawable = new BitmapDrawable(context.getResources(), result);
			if(Build.VERSION.SDK_INT >=16)
			{
				relativeLayout.setBackground(drawable);
			}
			else
			{
				relativeLayout.setBackgroundDrawable(drawable);
			}

			GlobalClass.background = result;
		}
	}
}
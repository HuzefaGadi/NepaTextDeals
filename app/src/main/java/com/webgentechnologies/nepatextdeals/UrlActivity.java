package com.webgentechnologies.nepatextdeals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UrlActivity extends Activity implements OnTouchListener {
	
	private Button Buttonurl, Buttonreturn;
	private ProgressBar ProgressBar1;
	EditText edit_messageurl;
	private static final String TAG = "UrlActivity.java";
	View senceTouch;
	MyCount timerCount;
	String validurl;
	ConnectionDetector connectionDetector;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_url);
		addListenerOnButton();
		StrictMode.enableDefaults();
		senceTouch = findViewById(R.id.layout_url); 
	    senceTouch.setOnTouchListener(this);
	    senceTouch = findViewById(R.id.edit_messageurl);
	    senceTouch.setOnTouchListener(this);
	    timerCount = new MyCount(20 * 1000, 1000);
	    timerCount.start();
		connectionDetector = new ConnectionDetector(this);
		
		
		String fontPath = "fonts/helvetica67medium.ttf";
		TextView buttonurl = (TextView) findViewById(R.id.buttonurl);
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        buttonurl.setTypeface(tf);
        
        TextView edit_messageredeem = (TextView) findViewById(R.id.edit_messageurl);
        Typeface.createFromAsset(getAssets(), fontPath);
        edit_messageredeem.setTypeface(tf);
		   
        TextView buttonreturn = (TextView) findViewById(R.id.buttonreturn);
        Typeface.createFromAsset(getAssets(), fontPath);
        buttonreturn.setTypeface(tf);
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
		Buttonurl = (Button) findViewById(R.id.buttonurl);
		Buttonreturn = (Button) findViewById(R.id.buttonreturn);
		ProgressBar1 = (ProgressBar)findViewById(R.id.ProgressBar1);
		ProgressBar1.setVisibility(View.GONE);
		edit_messageurl = (EditText) findViewById(R.id.edit_messageurl);
		//edit_messageurl.setGravity(Gravity.CENTER);	
		
		Buttonurl.setOnClickListener(new Button.OnClickListener() {
			
	        public void onClick(View view) {
	        	
	        	timerCount.cancel();
	        	
	        	InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            	
	        	ProgressBar1.setVisibility(View.VISIBLE);
	        	
	        	try {
					if(connectionDetector.isConnectingToInternet()) {


						String url1 = edit_messageurl.getText().toString();
						validurl = url1;
						SharedPreferences pref2 = getApplicationContext().getSharedPreferences("NepaTextDealsPref10", MODE_PRIVATE);
						Editor editor2 = pref2.edit();
						editor2.putString("validurl", validurl);
						editor2.apply();

						HttpClient httpclient = new DefaultHttpClient();
						String httppostURL = "http://nepatextdeals.com/nepa/androidweb";
						//String httppostURL = "http://192.168.0.254/nepadeals/androidweb";
						//String httppostURL = "http://50.62.31.191/nepadeals/androidweb";
						HttpPost httppost = new HttpPost(httppostURL);
						Log.v(TAG, "postURL: " + httppost);

						JSONObject data1 = new JSONObject();
						data1.put("merchant_kiosk_url", url1);

						JSONArray jsonArray = new JSONArray();
						jsonArray.put(data1);

						List<NameValuePair> nvps = new ArrayList<NameValuePair>();
						nvps.add(new BasicNameValuePair("data", data1.toString()));
						httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));


						httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity resEntity = response.getEntity();
						if (resEntity != null) {
							String responseStr = EntityUtils.toString(resEntity).trim();
							Log.v(TAG, "Response: " + responseStr);
							Log.i("TAG", "" + response.getStatusLine().getStatusCode());
							//Toast.makeText(UrlActivity.this,  responseStr, Toast.LENGTH_LONG).show();
							// you can add an if statement here and do other actions based on the response
							{
								try {
									JSONObject mainObject = new JSONObject(responseStr);
									String url = mainObject.getString("url");
									String kiosk = mainObject.getString("kiosk");
									String merchant_kiosk_id = mainObject.getString("merchant_kiosk_id");
									String merchant_location_id = mainObject.getString("merchant_location_id");
									String user_id = mainObject.getString("user_id");
									String no_of_checkin = mainObject.getString("no_of_checkin");
									String free_gift = mainObject.getString("free_gift");
									String valid_days = mainObject.getString("valid_days");
									String checkin_time_limit = mainObject.getString("checkin_time_limit");
									String business_logo = mainObject.getString("business_logo");
									String coupon_id = mainObject.getString("coupon_id");
									String coupon_keyword = mainObject.getString("coupon_keyword");
									String merchant_id = mainObject.getString("merchant_id");
									String organization_name = mainObject.getString("organization_name");
									String status = mainObject.getString("status");
									String disclaimer_message = mainObject.getString("disclaimer_message");

									if (url.equals("valid")) {
										if (kiosk.equals("no")) {
											ProgressBar1.setVisibility(View.GONE);

											Toast toast2 = Toast.makeText(UrlActivity.this, "Kiosk Is Not Active", Toast.LENGTH_LONG);
											LinearLayout toastLayout2 = (LinearLayout) toast2.getView();
											TextView toastTV2 = (TextView) toastLayout2.getChildAt(0);
											toastTV2.setTextSize(40);
											toastTV2.setTextColor(Color.WHITE);
											toast2.getView().setBackgroundResource(R.drawable.customtoast);
											toast2.show();
										} else if (kiosk.equals("yes")) {

											SharedPreferences pref = getApplicationContext().getSharedPreferences("NepaTextDealsPref", MODE_PRIVATE);
											Editor editor = pref.edit();
											editor.putString("merchant_kiosk_id", merchant_kiosk_id);
											editor.putString("merchant_location_id", merchant_location_id);
											editor.putString("user_id", user_id);
											editor.putString("no_of_checkin", no_of_checkin);
											editor.putString("free_gift", free_gift);
											editor.putString("valid_days", valid_days);
											editor.putString("checkin_time_limit", checkin_time_limit);
											editor.putString("business_logo", business_logo);
											editor.putString("coupon_id", coupon_id);
											editor.putString("coupon_keyword", coupon_keyword);
											editor.putString("merchant_id", merchant_id);
											editor.putString("organization_name", organization_name);
											editor.putString("status", status);
											editor.putString("disclaimer_message", disclaimer_message);
											editor.apply();
											//editor.commit();

											ProgressBar1.setVisibility(View.GONE);

											Toast toast2 = Toast.makeText(UrlActivity.this, organization_name, Toast.LENGTH_LONG);
											LinearLayout toastLayout2 = (LinearLayout) toast2.getView();
											TextView toastTV2 = (TextView) toastLayout2.getChildAt(0);
											toastTV2.setTextSize(40);
											toastTV2.setTextColor(Color.WHITE);
											toast2.getView().setBackgroundResource(R.drawable.customtoast);
											toast2.show();

											//Toast.makeText(getBaseContext(), "Your Information Has Been Stored", Toast.LENGTH_LONG).show();
											Toast toast = Toast.makeText(UrlActivity.this, "Your Information Has Been Stored", Toast.LENGTH_LONG);
											//toast.setGravity(Gravity.CENTER, 0, 0);
											LinearLayout toastLayout = (LinearLayout) toast.getView();
											TextView toastTV = (TextView) toastLayout.getChildAt(0);
											toastTV.setTextSize(40);
											toastTV.setTextColor(Color.WHITE);
											toast.getView().setBackgroundResource(R.drawable.customtoast);
											toast.show();

											new DownloadImageTask2().execute(business_logo);
										}
									} else if (url.equals("invalid")) {
										ProgressBar1.setVisibility(View.GONE);
										//Toast.makeText(UrlActivity.this,"Invalid URL", Toast.LENGTH_LONG).show();
										Toast toast = Toast.makeText(UrlActivity.this, "Invalid URL", Toast.LENGTH_LONG);
										//toast.setGravity(Gravity.CENTER, 0, 0);
										LinearLayout toastLayout = (LinearLayout) toast.getView();
										TextView toastTV = (TextView) toastLayout.getChildAt(0);
										toastTV.setTextSize(40);
										toastTV.setTextColor(Color.WHITE);
										toast.getView().setBackgroundResource(R.drawable.customtoast);
										toast.show();
									}

								} catch (JSONException e) {

									Log.e("JSON Parser", "Error parsing data " + e.toString());
									ProgressBar1.setVisibility(View.GONE);
									//Toast.makeText(UrlActivity.this,"Invalid URL", Toast.LENGTH_LONG).show();
									Toast toast = Toast.makeText(UrlActivity.this, "Invalid URL", Toast.LENGTH_LONG);
									//toast.setGravity(Gravity.CENTER, 0, 0);
									LinearLayout toastLayout = (LinearLayout) toast.getView();
									TextView toastTV = (TextView) toastLayout.getChildAt(0);
									toastTV.setTextSize(40);
									toastTV.setTextColor(Color.WHITE);
									toast.getView().setBackgroundResource(R.drawable.customtoast);
									toast.show();
								}
							}
						}
						edit_messageurl.setText(""); //reset the message text field

						//Toast.makeText(UrlActivity.this, "Data: " +data1,Toast.LENGTH_LONG).show();

					}
					else
					{
						Toast.makeText(UrlActivity.this, "No Internet Connection Avialable" ,
								Toast.LENGTH_LONG).show();
					}
                                        
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Throwable t) {
                        Toast.makeText(UrlActivity.this, "Request failed: " + t.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                		
        	}
	        	
	    });  
		
		Buttonreturn.setOnClickListener(new OnClickListener() {   
  			 
            @Override
            public void onClick(View view) {
 
            	Intent i = new Intent(UrlActivity.this, MainActivity.class);
            	startActivity(i);
            	UrlActivity.this.finish();
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
	    	  Intent mainIntent = new Intent(UrlActivity.this, MainActivity.class);
	    	  UrlActivity.this.startActivity(mainIntent);
	    	  UrlActivity.this.finish();
            
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

	class DownloadImageTask2 extends AsyncTask<String, Void, Bitmap> {

		public DownloadImageTask2() {

		}

		protected Bitmap doInBackground(String... urls) {


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

			GlobalClass.logo = result;
		}
	}
}
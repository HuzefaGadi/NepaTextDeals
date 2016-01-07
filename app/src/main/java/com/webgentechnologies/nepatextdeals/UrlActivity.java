package com.webgentechnologies.nepatextdeals;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UrlActivity extends Activity implements OnTouchListener {

    private Button buttonUrl, buttonReturn;
    private ProgressDialog progressBar;
    EditText edit_messageurl;
    private static final String TAG = "UrlActivity.java";
    View senceTouch;
    MyCount timerCount;
    String validurl;
    ConnectionDetector connectionDetector;
    Typeface tf;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_url);
        addListenerOnButton();
        senceTouch = findViewById(R.id.layout_url);
        senceTouch.setOnTouchListener(this);
        senceTouch = findViewById(R.id.edit_messageurl);
        senceTouch.setOnTouchListener(this);
        timerCount = new MyCount(20 * 1000, 1000);
        timerCount.start();
        connectionDetector = new ConnectionDetector(this);
        String fontPath = "fonts/helvetica67medium.ttf";
        tf = Typeface.createFromAsset(getAssets(), fontPath);


        TextView buttonurl = (TextView) findViewById(R.id.buttonurl);
        buttonurl.setTypeface(tf);

        TextView edit_messageredeem = (TextView) findViewById(R.id.edit_messageurl);
        edit_messageredeem.setTypeface(tf);

        TextView buttonreturn = (TextView) findViewById(R.id.buttonreturn);
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

        if (!hasFocus) {
            Log.d("Focus debug", "Lost focus !");

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    private void addListenerOnButton() {
        // TODO Auto-generated method stub
        buttonUrl = (Button) findViewById(R.id.buttonurl);
        buttonReturn = (Button) findViewById(R.id.buttonreturn);
       /* progressBar = (ProgressBar) findViewById(R.id.ProgressBar1);
        progressBar.setVisibility(View.INVISIBLE);*/
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait..");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        edit_messageurl = (EditText) findViewById(R.id.edit_messageurl);
        //edit_messageurl.setGravity(Gravity.CENTER);

        buttonUrl.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view) {
                senceTouch.requestLayout();
                timerCount.cancel();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                new CallWebservice(edit_messageurl.getText().toString()).execute();
            }

        });

        buttonReturn.setOnClickListener(new OnClickListener() {

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


    class CallWebservice extends AsyncTask<Void, Void, HttpResponse> {

        String url;

        public CallWebservice(String url) {

            this.url = url;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.show();
        }

        protected HttpResponse doInBackground(Void... urls) {

            try {
                if (connectionDetector.isConnectedToInternet()) {
                    String url1 = url;
                    validurl = url;
                    HttpClient httpclient = new DefaultHttpClient();
                    String httppostURL = "http://nepatextdeals.com/nepa/androidweb";
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
                    return response;
                } else {
                    showToastGeneric("No Internet Connection Avialable");
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                Toast.makeText(UrlActivity.this, "Request failed: " + t.toString(),
                        Toast.LENGTH_LONG).show();
            }
            return null;


        }

        protected void onPostExecute(HttpResponse result) {

            try {
                HttpResponse response = result;
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String responseStr = EntityUtils.toString(resEntity).trim();
                    Log.v(TAG, "Response: " + responseStr);
                    Log.i("TAG", "" + response.getStatusLine().getStatusCode());

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
                        String button_push_for_checkins = mainObject.getString("button_push_for_checkins");
                        if (url.equals("valid")) {
                            if (kiosk.equals("no")) {
                                progressBar.cancel();
                                showToastGeneric("Kiosk Is Not Active");
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
                                editor.putString("button_push_for_checkins", button_push_for_checkins);
                                editor.apply();
                                progressBar.cancel();
                                showToastGeneric(organization_name);
                                showToastGeneric("Your Information Has Been Stored");
                                SharedPreferences pref2 = getApplicationContext().getSharedPreferences("NepaTextDealsPref10", MODE_PRIVATE);
                                Editor editor2 = pref2.edit();
                                editor2.putString("validurl", validurl);
                                editor2.apply();
                                new DownloadImageTask2().execute(business_logo);
                            }
                        } else if (url.equals("invalid")) {
                            progressBar.cancel();
                            showToastGeneric("Invalid URL");
                        }

                    } catch (JSONException e) {

                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                        progressBar.cancel();
                        showToastGeneric("Invalid URL");

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(UrlActivity.this, "Request failed: " + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(UrlActivity.this, "Request failed: " + e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            } catch (Throwable t) {
                Toast.makeText(UrlActivity.this, "Request failed: " + t.toString(),
                        Toast.LENGTH_LONG).show();
            }

            edit_messageurl.setText("");

        }


        public void showToastGeneric(String message) {
            Toast toast = Toast.makeText(UrlActivity.this, message, Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(40);
            toastTV.setTextColor(Color.WHITE);
            toast.getView().setBackgroundResource(R.drawable.customtoast);
            toast.show();
        }
    }
}
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CheckinActivity extends ApplicationActivity implements OnTouchListener {

    private Button Buttonsend, b1, b2, b3, b4, b5, b6, b7, b8, b9, clear, b0, back;
    EditText edit_message;
    ImageView imageView2;
    TextView footer;
    String imagelogo;
    String merchant_id2, merchant_location_id2, user_id2, merchant_kiosk_id2, phonenumber, age;
    View senceTouch;
    MyCount timerCount;
    private ProgressBar ProgressBar1;
    SoundPoolPlayer sound;
    SharedPreferences pref;//= getApplicationContext().getSharedPreferences("NepaTextDealsPref", MODE_PRIVATE);
    boolean kioskMode = false;
    private static final String TAG = "CheckinActivity.java";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_checkin);
        addListenerOnButton();
        StrictMode.enableDefaults();
        senceTouch = findViewById(R.id.layout_checkin);
        senceTouch.setOnTouchListener(this);
        pref = getApplicationContext().getSharedPreferences("NepaTextDealsPref", MODE_PRIVATE);
        timerCount = new MyCount(20 * 1000, 1000);
        timerCount.start();

        sound = new SoundPoolPlayer(this);
        SharedPreferences pref = this.getSharedPreferences("NepaTextDealsPref", Context.MODE_PRIVATE);
        String merchant_id1 = pref.getString("merchant_id", null);
        String merchant_location_id1 = pref.getString("merchant_location_id", null);
        String user_id1 = pref.getString("user_id", null);
        String merchant_kiosk_id1 = pref.getString("merchant_kiosk_id", null);
        String disclaimer_message1 = pref.getString("disclaimer_message", null);
        String business_logo1 = pref.getString("business_logo", null);
        //Toast.makeText(getBaseContext(), merchant_id1, Toast.LENGTH_LONG).show();
        merchant_id2 = merchant_id1;
        merchant_location_id2 = merchant_location_id1;
        user_id2 = user_id1;
        merchant_kiosk_id2 = merchant_kiosk_id1;
        imagelogo = business_logo1;

        if (imagelogo == null) {

            //imageView2 = (ImageView) findViewById(R.id.imageView2);
            ProgressBar1.setVisibility(View.VISIBLE);

        } else if (imagelogo != null) {

            ProgressBar1.setVisibility(View.GONE);
            new DownloadImageTask1((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);

        }
        String kioskModeString = pref.getString("kiosk_mode", "2");
        if (kioskModeString.equals("2")) {
            kioskMode = false;
        } else {
            kioskMode = true;
        }
        footer = (TextView) findViewById(R.id.footer);
        footer.setText("By Signing Up You Agree To Receive Up To " + disclaimer_message1 + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");

        String fontPath = "fonts/helvetica67medium.ttf";
        TextView txt = (TextView) findViewById(R.id.messagecheck);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt.setTypeface(tf);
        txt.setText(kioskMode ? R.string.messagecheck : R.string.messagecheckForSignUp);


        TextView footer = (TextView) findViewById(R.id.footer);
        Typeface.createFromAsset(getAssets(), fontPath);
        footer.setTypeface(tf);

        TextView edit_message = (TextView) findViewById(R.id.edit_message);
        Typeface.createFromAsset(getAssets(), fontPath);
        edit_message.setTypeface(tf);

        TextView b1 = (TextView) findViewById(R.id.b1);
        Typeface.createFromAsset(getAssets(), fontPath);
        b1.setTypeface(tf);

        TextView b2 = (TextView) findViewById(R.id.b2);
        Typeface.createFromAsset(getAssets(), fontPath);
        b2.setTypeface(tf);

        TextView b3 = (TextView) findViewById(R.id.b3);
        Typeface.createFromAsset(getAssets(), fontPath);
        b3.setTypeface(tf);

        TextView b4 = (TextView) findViewById(R.id.b4);
        Typeface.createFromAsset(getAssets(), fontPath);
        b4.setTypeface(tf);

        TextView b5 = (TextView) findViewById(R.id.b5);
        Typeface.createFromAsset(getAssets(), fontPath);
        b5.setTypeface(tf);

        TextView b6 = (TextView) findViewById(R.id.b6);
        Typeface.createFromAsset(getAssets(), fontPath);
        b6.setTypeface(tf);

        TextView b7 = (TextView) findViewById(R.id.b7);
        Typeface.createFromAsset(getAssets(), fontPath);
        b7.setTypeface(tf);

        TextView b8 = (TextView) findViewById(R.id.b8);
        Typeface.createFromAsset(getAssets(), fontPath);
        b8.setTypeface(tf);

        TextView b9 = (TextView) findViewById(R.id.b9);
        Typeface.createFromAsset(getAssets(), fontPath);
        b9.setTypeface(tf);

        TextView b0 = (TextView) findViewById(R.id.b0);
        Typeface.createFromAsset(getAssets(), fontPath);
        b0.setTypeface(tf);

        TextView clear = (TextView) findViewById(R.id.clear);
        Typeface.createFromAsset(getAssets(), fontPath);
        clear.setTypeface(tf);

        TextView back = (TextView) findViewById(R.id.back);
        Typeface.createFromAsset(getAssets(), fontPath);
        back.setTypeface(tf);

        TextView buttonsend = (TextView) findViewById(R.id.buttonsend);
        Typeface.createFromAsset(getAssets(), fontPath);
        buttonsend.setTypeface(tf);


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
        Buttonsend = (Button) findViewById(R.id.buttonsend);
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b5 = (Button) findViewById(R.id.b5);
        b6 = (Button) findViewById(R.id.b6);
        b7 = (Button) findViewById(R.id.b7);
        b8 = (Button) findViewById(R.id.b8);
        b9 = (Button) findViewById(R.id.b9);
        clear = (Button) findViewById(R.id.clear);
        b0 = (Button) findViewById(R.id.b0);
        back = (Button) findViewById(R.id.back);
        ProgressBar1 = (ProgressBar) findViewById(R.id.ProgressBar1);
        ProgressBar1.setVisibility(View.GONE);
        edit_message = (EditText) findViewById(R.id.edit_message);
        edit_message.setGravity(Gravity.CENTER);


        edit_message.addTextChangedListener(new TextWatcher() {

            int len = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = edit_message.getText().toString();
                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    edit_message.append("-");

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                String str = edit_message.getText().toString();
                len = str.length();

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        Buttonsend.setOnClickListener(new OnClickListener() {
                                          @Override
                                          public void onClick(View view) {

                                              timerCount.cancel();

                                              String check = edit_message.getText().toString();
                                              phonenumber = check;
                                              if (check.length() < 12) {
                                                  Toast toast = Toast.makeText(CheckinActivity.this, "Invalid Phone Number", Toast.LENGTH_LONG);
                                                  toast.setGravity(Gravity.CENTER, 0, 0);
                                                  LinearLayout toastLayout = (LinearLayout) toast.getView();
                                                  TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                                  toastTV.setTextSize(45);
                                                  toastTV.setTextColor(Color.WHITE);
                                                  toast.getView().setBackgroundResource(R.drawable.customtoast);
                                                  toast.show();
                                              } else {
                                                  Buttonsend.setEnabled(false);


                                                  try {

                                                      if (kioskMode) {
                                                          String checkin = edit_message.getText().toString();

                                                          HttpClient httpclient = new DefaultHttpClient();
                                                          String httppostURL = "http://nepatextdeals.com/nepa/androidweb/checkin";
                                                          //String httppostURL = "http://192.168.0.254/nepadeals/androidweb/checkin";
                                                          HttpPost httppost = new HttpPost(httppostURL);
                                                          Log.v(TAG, "postURL: " + httppost);

                                                          JSONObject data1 = new JSONObject();
                                                          data1.put("merchant_id", merchant_id2);
                                                          data1.put("merchant_location_id", merchant_location_id2);
                                                          data1.put("business_user_id", user_id2);
                                                          data1.put("merchant_kiosk_id", merchant_kiosk_id2);
                                                          data1.put("subscriber_phone", checkin);

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
                                                              //Toast.makeText(CheckinActivity.this,  responseStr, Toast.LENGTH_LONG).show();
                                                              //you can add an if statement here and do other actions based on the response
                                                              {
                                                                  JSONObject mainObject = new JSONObject(responseStr);
                                                                  String record = mainObject.getString("record");
                                                                  if (record.equals("No")) {
                                                                      String reason = mainObject.getString("reason");
                                                                      if (reason.equals("1")) {
                                                                          Toast toast = Toast.makeText(CheckinActivity.this, "Invalid Phone Number", Toast.LENGTH_LONG);
                                                                          toast.setGravity(Gravity.CENTER, 0, 0);
                                                                          LinearLayout toastLayout = (LinearLayout) toast.getView();
                                                                          TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                                                          toastTV.setTextSize(45);
                                                                          toastTV.setTextColor(Color.WHITE);
                                                                          toast.getView().setBackgroundResource(R.drawable.customtoast);
                                                                          toast.show();
                                                                      } else if (reason.equals("2")) {
                                                                          Toast toast = Toast.makeText(CheckinActivity.this, "Your Number Is Blacklisted/Inactive", Toast.LENGTH_LONG);

                                                                          LinearLayout layout = (LinearLayout) toast.getView();
                                                                          if (layout.getChildCount() > 0) {
                                                                              TextView tv = (TextView) layout.getChildAt(0);
                                                                              tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                                                                          }

                                                                          toast.setGravity(Gravity.CENTER, 0, 0);
                                                                          LinearLayout toastLayout = (LinearLayout) toast.getView();
                                                                          TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                                                          toastTV.setTextSize(45);
                                                                          toastTV.setTextColor(Color.WHITE);
                                                                          toast.getView().setBackgroundResource(R.drawable.customtoast);
                                                                          toast.show();
                                                                      } else if (reason.equals("0")) {
                                                                          Toast toast = Toast.makeText(CheckinActivity.this, "Kiosk Is Not Active", Toast.LENGTH_LONG);
                                                                          toast.setGravity(Gravity.CENTER, 0, 0);
                                                                          LinearLayout toastLayout = (LinearLayout) toast.getView();
                                                                          TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                                                          toastTV.setTextSize(45);
                                                                          toastTV.setTextColor(Color.WHITE);
                                                                          toast.getView().setBackgroundResource(R.drawable.customtoast);
                                                                          toast.show();
                                                                      } else if (reason.equals("3")) {
                                                                          String age_limit = mainObject.getString("age_limit");
                                                                          age = age_limit;

                                                                          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckinActivity.this);
                                                                          alertDialogBuilder.setMessage("To Participate With This Business You Must Be At Least " + age_limit + " Years Old. Do You Meet The Age Requirement?");
                                                                          alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                              public void onClick(DialogInterface dialog, int id) {

                                                                                  try {


                                                                                      HttpClient httpclient5 = new DefaultHttpClient();
                                                                                      String httppostURL5 = "http://nepatextdeals.com/nepa/androidweb/checkinage";
                                                                                      //String httppostURL5 = "http://192.168.0.254/nepadeals/androidweb/checkinage";
                                                                                      HttpPost httppost5 = new HttpPost(httppostURL5);
                                                                                      Log.v(TAG, "postURL: " + httppost5);

                                                                                      JSONObject data5 = new JSONObject();
                                                                                      data5.put("merchant_id", merchant_id2);
                                                                                      data5.put("merchant_location_id", merchant_location_id2);
                                                                                      data5.put("business_user_id", user_id2);
                                                                                      data5.put("merchant_kiosk_id", merchant_kiosk_id2);
                                                                                      data5.put("subscriber_phone", phonenumber);
                                                                                      data5.put("age_limit", age);

                                                                                      JSONArray jsonArray5 = new JSONArray();
                                                                                      jsonArray5.put(data5);

                                                                                      List<NameValuePair> nvps5 = new ArrayList<NameValuePair>();
                                                                                      nvps5.add(new BasicNameValuePair("data", data5.toString()));
                                                                                      httppost5.setEntity(new UrlEncodedFormEntity(nvps5, HTTP.UTF_8));

                                                                                      httppost5.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                                                                                      HttpResponse response5 = httpclient5.execute(httppost5);
                                                                                      HttpEntity resEntity5 = response5.getEntity();
                                                                                      if (resEntity5 != null) {
                                                                                          String responseStr5 = EntityUtils.toString(resEntity5).trim();
                                                                                          Log.v(TAG, "Response: " + responseStr5);
                                                                                          Log.i("TAG", "" + response5.getStatusLine().getStatusCode());
                                                                                          //Toast.makeText(CheckinActivity.this,  responseStr5, Toast.LENGTH_LONG).show();
                                                                                          //you can add an if statement here and do other actions based on the response
                                                                                          {
                                                                                              JSONObject mainObject = new JSONObject(responseStr5);
                                                                                              String record = mainObject.getString("record");
                                                                                              if (record.equals("Yes")) {
                                                                                                  String time_over = mainObject.getString("time_over");
                                                                                                  if (time_over.equals("Yes")) {
                                                                                                      String checkin_status = mainObject.getString("checkin_status");
                                                                                                      if (checkin_status.equals("1")) {
                                                                                                          String subscriber_no_of_checkin = mainObject.getString("subscriber_no_of_checkin");
                                                                                                          String no_of_checkin = mainObject.getString("no_of_checkin");
                                                                                                          String free_gift = mainObject.getString("free_gift");
                                                                                                          String disclaimer_message = mainObject.getString("disclaimer_message");

                                                                                                          SharedPreferences pref = getApplicationContext().getSharedPreferences("NepaTextDealsPref", MODE_PRIVATE);
                                                                                                          Editor editor = pref.edit();
                                                                                                          editor.putString("no_of_checkin", no_of_checkin);
                                                                                                          editor.putString("subscriber_no_of_checkin", subscriber_no_of_checkin);
                                                                                                          editor.putString("free_gift", free_gift);
                                                                                                          editor.putString("disclaimer_message", disclaimer_message);
                                                                                                          editor.apply();

                                                                                                          Intent i = new Intent(CheckinActivity.this, NumberCheckActivity.class);
                                                                                                          startActivity(i);
                                                                                                          CheckinActivity.this.finish();

                                                                                                      }
                                                                                                  }
                                                                                              }
                                                                                          }

                                                                                      }
                                                                                      //Toast.makeText(CheckinActivity.this, "Data: " +data5,Toast.LENGTH_LONG).show();

                                                                                  } catch (ClientProtocolException e) {
                                                                                      e.printStackTrace();
                                                                                  } catch (IOException e) {
                                                                                      e.printStackTrace();
                                                                                  } catch (Throwable t) {
                                                                                      Toast.makeText(CheckinActivity.this, "Request failed: " + t.toString(),
                                                                                              Toast.LENGTH_LONG).show();
                                                                                  }
                                                                              }

                                                                          });
                                                                          alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                              public void onClick(DialogInterface dialog, int id) {

                                                                                  Intent i = new Intent(CheckinActivity.this, MainActivity.class);
                                                                                  startActivity(i);
                                                                                  CheckinActivity.this.finish();

                                                                              }
                                                                          });
                                                                          AlertDialog alertDialog = alertDialogBuilder.create();
                                                                          alertDialog.show();
                                                                          alertDialog.setCancelable(false);
                                                                      }
                                                                  } else if (record.equals("Yes")) {
                                                                      String time_over = mainObject.getString("time_over");
                                                                      if (time_over.equals("No")) {
                                                                          Toast toast = Toast.makeText(CheckinActivity.this, "Next Check-In Time Not Over", Toast.LENGTH_LONG);
                                                                          toast.setGravity(Gravity.CENTER, 0, 0);
                                                                          LinearLayout toastLayout = (LinearLayout) toast.getView();
                                                                          TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                                                          toastTV.setTextSize(45);
                                                                          toastTV.setTextColor(Color.WHITE);
                                                                          toast.getView().setBackgroundResource(R.drawable.customtoast);
                                                                          toast.show();
                                                                      } else if (time_over.equals("Yes")) {
                                                                          String checkin_status = mainObject.getString("checkin_status");
                                                                          if (checkin_status.equals("1")) {

                                                                              String subscriber_no_of_checkin = mainObject.getString("subscriber_no_of_checkin");
                                                                              String no_of_checkin = mainObject.getString("no_of_checkin");
                                                                              String free_gift = mainObject.getString("free_gift");
                                                                              String disclaimer_message = mainObject.getString("disclaimer_message");

                                                                              SharedPreferences pref = getApplicationContext().getSharedPreferences("NepaTextDealsPref", MODE_PRIVATE);
                                                                              Editor editor = pref.edit();
                                                                              editor.putString("no_of_checkin", no_of_checkin);
                                                                              editor.putString("subscriber_no_of_checkin", subscriber_no_of_checkin);
                                                                              editor.putString("free_gift", free_gift);
                                                                              editor.putString("disclaimer_message", disclaimer_message);
                                                                              editor.apply();
                                                                              //editor.commit();

                        	   					/*Toast toast = Toast.makeText(CheckinActivity.this,"You Have Completed " +subscriber_no_of_checkin +" Check-In's Within " +no_of_checkin, Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
            				            		LinearLayout toastLayout = (LinearLayout) toast.getView();
            				            		TextView toastTV = (TextView) toastLayout.getChildAt(0);
            				            		toastTV.setTextSize(45);
            				            		toastTV.setTextColor(Color.WHITE);
            				            		toast.getView().setBackgroundResource(R.drawable.customtoast);
            				            		toast.show(); */
                                                                              //MediaPlayer mp;
                                                                              MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ping);
                                                                              mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                                                                  @Override
                                                                                  public void onCompletion(MediaPlayer mp) {
                                                                                      // TODO Auto-generated method stub
                                                                                      mp.reset();
                                                                                      mp.release();
                                                                                      mp = null;
                                                                                  }

                                                                              });
                                                                              mp.start();//sound.playShortResource(R.raw.ping);


                                                                              Intent i = new Intent(CheckinActivity.this, NumberCheckActivity.class);
                                                                              startActivity(i);
                                                                              CheckinActivity.this.finish();

                                                                          } else if (checkin_status.equals("2")) {
                                                                              MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ping);
                                                                              mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                                                                  @Override
                                                                                  public void onCompletion(MediaPlayer mp) {
                                                                                      // TODO Auto-generated method stub
                                                                                      mp.reset();
                                                                                      mp.release();
                                                                                      mp = null;
                                                                                  }

                                                                              });
                                                                              mp.start();

                                                                              Intent i = new Intent(CheckinActivity.this, SuccessCheckActivity.class);
                                                                              startActivity(i);
                                                                              CheckinActivity.this.finish();

                                                                          }
                                                                      }
                                                                  }
                                                              }
                                                          }
                                                          edit_message.setText("");
                                                          //Toast.makeText(getBaseContext(),"Sent",Toast.LENGTH_SHORT).show();
                                                          //Toast.makeText(CheckinActivity.this, "Data: " +data1,Toast.LENGTH_LONG).show();
                                                      } else {
                                                          String checkin = edit_message.getText().toString();

                                                          HttpClient httpclient = new DefaultHttpClient();
                                                          String httppostURL = "http://nepatextdeals.com/nepa/androidweb/signup";
                                                          //String httppostURL = "http://192.168.0.254/nepadeals/androidweb/checkin";
                                                          HttpPost httppost = new HttpPost(httppostURL);
                                                          Log.v(TAG, "postURL: " + httppost);

                                                          JSONObject data1 = new JSONObject();
                                                          data1.put("merchant_id", merchant_id2);
                                                          data1.put("merchant_location_id", merchant_location_id2);
                                                          data1.put("business_user_id", user_id2);
                                                          data1.put("merchant_kiosk_id", merchant_kiosk_id2);
                                                          data1.put("subscriber_phone", checkin);

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
                                                              //Toast.makeText(CheckinActivity.this,  responseStr, Toast.LENGTH_LONG).show();
                                                              //you can add an if statement here and do other actions based on the response

                                                              JSONObject mainObject = new JSONObject(responseStr);
                                                              String record = mainObject.getString("record");
                                                              if (record.equals("exist")) {

                                                                  Toast toast = Toast.makeText(CheckinActivity.this, "You Are Already Subscribed", Toast.LENGTH_LONG);
                                                                  toast.setGravity(Gravity.CENTER, 0, 0);
                                                                  LinearLayout toastLayout = (LinearLayout) toast.getView();
                                                                  TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                                                  toastTV.setTextSize(45);
                                                                  toastTV.setGravity(Gravity.CENTER);
                                                                  toastTV.setTextColor(Color.WHITE);
                                                                  toast.getView().setBackgroundResource(R.drawable.customtoast);
                                                                  toast.show();

                                                              } else if (record.equals("new")) {

                                                                  MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.ping);
                                                                  mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                                                      @Override
                                                                      public void onCompletion(MediaPlayer mp) {
                                                                          // TODO Auto-generated method stub
                                                                          mp.reset();
                                                                          mp.release();
                                                                          mp = null;
                                                                      }

                                                                  });
                                                                  mp.start();//sound.playShortResource(R.raw.ping);

                                                                  String disclaimer_message = mainObject.getString("disclaimer_message");
                                                                  SharedPreferences pref = getApplicationContext().getSharedPreferences("NepaTextDealsPref", MODE_PRIVATE);
                                                                  Editor editor = pref.edit();
                                                                  editor.putString("disclaimer_message", disclaimer_message);
                                                                  editor.apply();
                                                                  Intent i = new Intent(CheckinActivity.this, NumberCheckActivity.class);
                                                                  startActivity(i);
                                                                  CheckinActivity.this.finish();


                                                              }
                                                          }


                                                          edit_message.setText("");
                                                          //Toast.makeText(getBaseContext(),"Sent",Toast.LENGTH_SHORT).show();
                                                          //Toast.makeText(CheckinActivity.this, "Data: " +data1,Toast.LENGTH_LONG).show();
                                                      }

                                                  } catch (
                                                          ClientProtocolException e
                                                          )

                                                  {
                                                      e.printStackTrace();
                                                  } catch (
                                                          IOException e
                                                          )

                                                  {
                                                      e.printStackTrace();
                                                  } catch (
                                                          Throwable t
                                                          )

                                                  {
                                                      Toast.makeText(CheckinActivity.this, "Request failed: " + t.toString(),
                                                              Toast.LENGTH_LONG).show();
                                                  }

                                                  Buttonsend.setEnabled(true);

                                              }
                                          }
                                      }

        );


        b1.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "1");
                                      timerCount.cancel();
                                  }
                              }

        );
        b2.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "2");
                                      timerCount.cancel();
                                  }
                              }

        );
        b3.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "3");
                                      timerCount.cancel();
                                  }
                              }

        );
        b4.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "4");
                                      timerCount.cancel();
                                  }
                              }

        );
        b5.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "5");
                                      timerCount.cancel();
                                  }
                              }

        );
        b6.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "6");
                                      timerCount.cancel();
                                  }
                              }

        );
        b7.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "7");
                                      timerCount.cancel();
                                  }
                              }

        );
        b8.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "8");
                                      timerCount.cancel();
                                  }
                              }

        );
        b9.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "9");
                                      timerCount.cancel();
                                  }
                              }

        );
        clear.setOnClickListener(new Button.OnClickListener()

                                 {
                                     public void onClick(View view) {
                                         Editable editableText = edit_message.getEditableText();
                                         int length = editableText.length();
                                         if (length > 0) {
                                             editableText.delete(length - length, length);
                                         }
                                         timerCount.cancel();
                                         //edit_message.setText("");
                                     }
                                 }

        );
        b0.setOnClickListener(new Button.OnClickListener()

                              {
                                  public void onClick(View view) {
                                      edit_message.setText(edit_message.getText() + "0");
                                      timerCount.cancel();
                                  }
                              }

        );
        back.setOnClickListener(new Button.OnClickListener()

                                {
                                    public void onClick(View view) {

                                        if(kioskMode)
                                        {
                                            Intent i = new Intent(CheckinActivity.this, MainScreenActivity.class);
                                            startActivity(i);
                                        }
                                        else
                                        {
                                            Intent i = new Intent(CheckinActivity.this, MainActivity.class);
                                            startActivity(i);
                                        }

                                        CheckinActivity.this.finish();
                                        timerCount.cancel();
                                    }
                                }

        );
    }

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            //some script here
            Intent mainIntent = new Intent(CheckinActivity.this, MainActivity.class);
            CheckinActivity.this.startActivity(mainIntent);
            CheckinActivity.this.finish();

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //sound.release();
    }
}

class DownloadImageTask1 extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask1(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    /*protected Bitmap doInBackground(String... urls) {
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
    } */
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


}
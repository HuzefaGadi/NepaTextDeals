package com.webgentechnologies.nepatextdeals;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


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
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ApplicationActivity {

    private Button Getstarted;
    private ProgressBar ProgressBar1;
    TextView footer;
    Boolean shouldAllowAhead = false;

    Boolean isInternetPresent = false;

    ConnectionDetector cd;

    private static final String TAG = "MainActivity.java";

    String validurl2;
    ImageView imageView2;
    String imagelogo;
    int countOfClicks;
    boolean kioskMode = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (80 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        //TO CHANGE LATER
        customViewGroup view = new customViewGroup(this);

		//manager.addView(view, localLayoutParams);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        StrictMode.enableDefaults();


        String disclaimer_message1 = pref.getString("disclaimer_message", null);
        String business_logo1 = pref.getString("business_logo", null);

        imagelogo = business_logo1;
        countOfClicks = 0;
        if (imagelogo == null) {

            //imageView2 = (ImageView) findViewById(R.id.imageView2);
            ProgressBar1.setVisibility(View.VISIBLE);

        } else if (imagelogo != null) {

            ProgressBar1.setVisibility(View.GONE);
            new DownloadImageTask2((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);

        }

        footer = (TextView) findViewById(R.id.footer);
        footer.setText("By Signing Up You Agree To Receive Up To " + disclaimer_message1 + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");

        cd = new ConnectionDetector(getApplicationContext());

        SharedPreferences pref2 = this.getSharedPreferences("NepaTextDealsPref10", Context.MODE_PRIVATE);
        String validurl1 = pref2.getString("validurl", null);
        validurl2 = validurl1;
        //Toast.makeText(MainActivity.this, validurl2, Toast.LENGTH_LONG).show();
        extractData();
        //1 is kiosk mode the normal one else 2 : signup
        String kioskModeString = pref.getString("kiosk_mode","1");
        if(kioskModeString.equals("2"))
        {
            kioskMode = false;
        }
        else
        {
            kioskMode = true;
        }
        String fontPath = "fonts/helvetica67medium.ttf";
        TextView txt = (TextView) findViewById(R.id.start);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt.setTypeface(tf);
        txt.setText(kioskMode ? R.string.start : R.string.startForSignUp);


        TextView txt2 = (TextView) findViewById(R.id.start2);
        Typeface.createFromAsset(getAssets(), fontPath);
        txt2.setTypeface(tf);

        TextView txt3 = (TextView) findViewById(R.id.start3);
        Typeface.createFromAsset(getAssets(), fontPath);
        txt3.setTypeface(tf);

        TextView txt1 = (TextView) findViewById(R.id.start1);
        Typeface.createFromAsset(getAssets(), fontPath);
        txt1.setTypeface(tf);
        txt1.setText(kioskMode ? R.string.start1 : R.string.start1ForSignUp);

        TextView footer = (TextView) findViewById(R.id.footer);
        Typeface.createFromAsset(getAssets(), fontPath);
        footer.setTypeface(tf);

        TextView getstarted = (TextView) findViewById(R.id.getstarted);
        Typeface.createFromAsset(getAssets(), fontPath);
        getstarted.setTypeface(tf);

        getstarted.setText(kioskMode?R.string.getstarted:R.string.getstartedForSignUp);

        final ImageView imageToExit = (ImageView) findViewById(R.id.imageViewForExit);
        imageToExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("CICKED");
                if (countOfClicks == 0) {

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            countOfClicks = 0;
                            System.out.println("COUNT___0");
                        }
                    }, 7000);
                } else if (countOfClicks > 9) {

                    finish();
                }
                countOfClicks++;


            }
        });


    }


    public void extractData() {
        if (validurl2 != null) {
            {

                isInternetPresent = cd.isConnectingToInternet();

                if (isInternetPresent) {

                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        String httppostURL = "http://nepatextdeals.com/nepa/androidweb";
                        //String httppostURL = "http://192.168.0.254/nepadeals/androidweb";
                        //String httppostURL = "http://50.62.31.191/nepadeals/androidweb";
                        HttpPost httppost = new HttpPost(httppostURL);
                        Log.v(TAG, "postURL: " + httppost);

                        JSONObject data1 = new JSONObject();
                        data1.put("merchant_kiosk_url", validurl2);

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
                            //Toast.makeText(MainActivity.this,  responseStr, Toast.LENGTH_LONG).show();
                            {
                                try {
                                    JSONObject mainObject = new JSONObject(responseStr);

                                    String kiosk_mode = mainObject.getString("kiosk_mode");
                                    Editor editor = pref.edit();

                                    editor.putString("kiosk_mode", kiosk_mode);
                                    editor.apply();
                                    shouldAllowAhead = true;


                                } catch (JSONException e) {
                                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                                }
                            }
                        }
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Request failed: " + e.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Throwable t) {
                        Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
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

    @Override
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
        Getstarted = (Button) findViewById(R.id.getstarted);
        ProgressBar1 = (ProgressBar) findViewById(R.id.ProgressBar1);
        ProgressBar1.setVisibility(View.GONE);
        //OnClick start new sign up activity
        Getstarted.setOnClickListener(new OnClickListener() {

                                          @Override
                                          public void onClick(View view) {


                                              if (validurl2 == null) {

                                                  isInternetPresent = cd.isConnectingToInternet();

                                                  if (isInternetPresent) {

                                                      Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                      startActivity(i);
                                                      MainActivity.this.finish();

                                                  } else if (!isInternetPresent) {

                                                      //Toast.makeText(MainActivity.this,  "You Don't Have Internet Connection", Toast.LENGTH_LONG).show();
                                                      Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
                                                      toast.setGravity(Gravity.CENTER, 0, 0);
                                                      LinearLayout toastLayout = (LinearLayout) toast.getView();
                                                      TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                                      toastTV.setTextSize(45);
                                                      toastTV.setTextColor(Color.WHITE);
                                                      toast.getView().setBackgroundResource(R.drawable.customtoast);
                                                      toast.show();
                                                      Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                      startActivity(i);
                                                      MainActivity.this.finish();
                                                  }


                                              } else if (validurl2 != null) {

                                                  isInternetPresent = cd.isConnectingToInternet();

                                                  if (isInternetPresent) {


                                                      if (shouldAllowAhead) {

                                                          if(kioskMode)
                                                          {
                                                              Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                              startActivity(i);
                                                              shouldAllowAhead = false;
                                                              MainActivity.this.finish();
                                                          }
                                                          else {
                                                              Intent i = new Intent(MainActivity.this, CheckinActivity.class);
                                                              startActivity(i);
                                                              shouldAllowAhead = false;
                                                              MainActivity.this.finish();
                                                          }


                                                      } else {

                                                          try {
                                                              HttpClient httpclient = new DefaultHttpClient();
                                                              String httppostURL = "http://nepatextdeals.com/nepa/androidweb";
                                                              //String httppostURL = "http://192.168.0.254/nepadeals/androidweb";
                                                              //String httppostURL = "http://50.62.31.191/nepadeals/androidweb";
                                                              HttpPost httppost = new HttpPost(httppostURL);
                                                              Log.v(TAG, "postURL: " + httppost);

                                                              JSONObject data1 = new JSONObject();
                                                              data1.put("merchant_kiosk_url", validurl2);

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
                                                                  //Toast.makeText(MainActivity.this,  responseStr, Toast.LENGTH_LONG).show();
                                                                  {
                                                                      System.out.println("RESPONSE FROM SERVER -" + responseStr);
                                                                      try {
                                                                          JSONObject mainObject = new JSONObject(responseStr);
                                                                          String no_of_checkin = mainObject.getString("no_of_checkin");
                                                                          String free_gift = mainObject.getString("free_gift");
                                                                          String disclaimer_message = mainObject.getString("disclaimer_message");
                                                                          String business_logo = mainObject.getString("business_logo");
                                                                          String button_push_for_checkins = mainObject.getString("button_push_for_checkins");
                                                                          String kiosk_mode = mainObject.getString("kiosk_mode");
                                                                          SharedPreferences pref = getApplicationContext().getSharedPreferences("NepaTextDealsPref", MODE_PRIVATE);
                                                                          Editor editor = pref.edit();
                                                                          editor.putString("no_of_checkin", no_of_checkin);
                                                                          editor.putString("free_gift", free_gift);
                                                                          editor.putString("disclaimer_message", disclaimer_message);
                                                                          editor.putString("business_logo", business_logo);
                                                                          editor.putString("button_push_for_checkins", button_push_for_checkins);
                                                                          editor.putString("kiosk_mode", kiosk_mode);
                                                                          editor.apply();
                                                                          kioskMode=kiosk_mode.equals("1")?true:false;
                                                                          if(kioskMode)
                                                                          {
                                                                              Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                                              startActivity(i);
                                                                              shouldAllowAhead = false;
                                                                              MainActivity.this.finish();
                                                                          }
                                                                          else {
                                                                              Intent i = new Intent(MainActivity.this, CheckinActivity.class);
                                                                              startActivity(i);
                                                                              shouldAllowAhead = false;
                                                                              MainActivity.this.finish();
                                                                          }
                                                                      } catch (JSONException e) {
                                                                          Log.e("JSON Parser", "Error parsing data " + e.toString());
                                                                      }
                                                                  }
                                                              }
                                                          } catch (ClientProtocolException e) {
                                                              e.printStackTrace();
                                                          } catch (IOException e) {
                                                              e.printStackTrace();
                                                          } catch (Exception e) {
                                                              Toast.makeText(MainActivity.this, "Request failed: " + e.toString(),
                                                                      Toast.LENGTH_LONG).show();
                                                          } catch (Throwable t) {
                                                              Toast.makeText(MainActivity.this, "Request failed: " + t.toString(),
                                                                      Toast.LENGTH_LONG).show();
                                                          }
                                                      }
                                                  }
                                              } else if (!isInternetPresent)

                                              {

                                                  //Toast.makeText(MainActivity.this,  "You Don't Have Internet Connection", Toast.LENGTH_LONG).show();
                                                  Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
                                                  toast.setGravity(Gravity.CENTER, 0, 0);
                                                  LinearLayout toastLayout = (LinearLayout) toast.getView();
                                                  TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                                  toastTV.setTextSize(45);
                                                  toastTV.setTextColor(Color.WHITE);
                                                  toast.getView().setBackgroundResource(R.drawable.customtoast);
                                                  toast.show();
                                                  Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                  startActivity(i);
                                                  MainActivity.this.finish();
                                              }


                                          }


                                      }

        );
    }
}

class DownloadImageTask2 extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask2(ImageView bmImage) {
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
}
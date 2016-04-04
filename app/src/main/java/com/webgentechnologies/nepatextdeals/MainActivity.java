package com.webgentechnologies.nepatextdeals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.webgentechnologies.nepatextdeals.beans.UrlResponse;
import com.webgentechnologies.nepatextdeals.utils.Constants;
import com.webgentechnologies.nepatextdeals.utils.GlobalClass;

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

public class MainActivity extends ApplicationActivity {

    private Button getStarted;
    private ProgressBar progressBar;
    TextView footer;
    public Boolean shouldAllowAhead = false;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private static final String TAG = "MainActivity.java";
    String validurl2;
    ImageView imageView2;
    String imagelogo;
    int countOfClicks;
    boolean kioskMode = true;
    RelativeLayout relativeLayout;
    UrlResponse urlResponse;

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
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        String responseString = pref.getString(Constants.URL_RESPONSE_BEAN, "");
        urlResponse = new Gson().fromJson(responseString, UrlResponse.class);
        String business_background_img = null;
        String disclaimer_message1 = null;
        String business_logo1 = null;
        String kioskModeString = "";
        if (urlResponse != null) {
            business_background_img = urlResponse.getBusiness_background_img();
            disclaimer_message1 = urlResponse.getDisclaimer_message();
            business_logo1 = urlResponse.getBusiness_logo();
            kioskModeString = urlResponse.getKiosk_mode();
        }
        if (business_background_img != null) {
            new DownloadBackGroundTask(relativeLayout, this, false).execute(business_background_img);
        }

        imagelogo = business_logo1;
        countOfClicks = 0;
        if (imagelogo == null) {
            progressBar.setVisibility(View.VISIBLE);

        } else if (imagelogo != null) {

            progressBar.setVisibility(View.GONE);
            new DownloadImageTask2((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);

        }


        footer = (TextView) findViewById(R.id.footer);
        footer.setText("By Signing Up You Agree To Receive Up To " + disclaimer_message1 + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");
        cd = new ConnectionDetector(getApplicationContext());
        SharedPreferences pref2 = this.getSharedPreferences("NepaTextDealsPref10", Context.MODE_PRIVATE);
        String validurl1 = pref2.getString("validurl", null);
        validurl2 = validurl1;
        //Toast.makeText(MainActivity.this, validurl2, Toast.LENGTH_LONG).show();
        new CallWebService("CALL_FOR_EXTRACT_DATA").execute();
        //1 is kiosk mode the normal one else 2 : signup

        if (kioskModeString.equals("2")) {
            kioskMode = false;
        } else {
            kioskMode = true;
        }
        String fontPath = "fonts/helvetica67medium.ttf";
        TextView txt = (TextView) findViewById(R.id.start);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt.setTypeface(tf);
        txt.setText(kioskMode ? R.string.start : R.string.startForSignUp);
        TextView txt2 = (TextView) findViewById(R.id.start2);
        txt2.setTypeface(tf);
        TextView txt3 = (TextView) findViewById(R.id.start3);
        txt3.setTypeface(tf);
        TextView txt1 = (TextView) findViewById(R.id.start1);
        txt1.setTypeface(tf);
        txt1.setText(kioskMode ? R.string.start1 : R.string.start1ForSignUp);
        TextView footer = (TextView) findViewById(R.id.footer);
        footer.setTypeface(tf);
        TextView getstarted = (TextView) findViewById(R.id.getstarted);
        getstarted.setTypeface(tf);
        getstarted.setText(kioskMode ? R.string.getstarted : R.string.getstartedForSignUp);

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
        getStarted = (Button) findViewById(R.id.getstarted);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar1);
        progressBar.setVisibility(View.GONE);
        getStarted.setOnClickListener(new OnClickListener() {

                                          @Override
                                          public void onClick(View view) {
                                              isInternetPresent = cd.isConnectedToInternet();

                                              if (validurl2 == null) {
                                                  if (isInternetPresent) {
                                                      Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                      startActivity(i);
                                                      finish();
                                                  } else if (!isInternetPresent) {
                                                      showToastGeneric("No Internet Connection");
                                                      Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                      startActivity(i);
                                                      finish();
                                                  }
                                              } else if (validurl2 != null) {
                                                  if (isInternetPresent) {
                                                      if (shouldAllowAhead) {
                                                          if (kioskMode) {
                                                              Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                              startActivity(i);
                                                              shouldAllowAhead = false;
                                                              MainActivity.this.finish();
                                                          } else {
                                                              Intent i = new Intent(MainActivity.this, CheckinActivity.class);
                                                              startActivity(i);
                                                              shouldAllowAhead = false;
                                                              MainActivity.this.finish();
                                                          }
                                                      } else {
                                                          new CallWebService("CALL_FOR_ON_CLICK").execute();
                                                      }
                                                  }
                                              } else if (!isInternetPresent) {
                                                  showToastGeneric("No Internet Connection");
                                                  Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                                                  startActivity(i);
                                                  MainActivity.this.finish();
                                              }
                                          }


                                      }

        );
    }

    public void showToastGeneric(String message) {
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(45);
        toastTV.setTextColor(Color.WHITE);
        toast.getView().setBackgroundResource(R.drawable.customtoast);
        toast.show();

    }

    class CallWebService extends AsyncTask<Void, Void, String> {
        String callType;

        public CallWebService(String callType) {
            this.callType = callType;
        }

        protected String doInBackground(Void... urls) {

            if (callType.equals("CALL_FOR_EXTRACT_DATA")) {
                extractData();
            } else {
                return callOnClick();
            }
            return null;
        }

        protected void onPostExecute(String response) {

            if (response != null) {
                if (kioskMode) {
                    Intent i = new Intent(MainActivity.this, MainScreenActivity.class);
                    startActivity(i);
                    shouldAllowAhead = false;
                    MainActivity.this.finish();
                } else {
                    Intent i = new Intent(MainActivity.this, CheckinActivity.class);
                    startActivity(i);
                    shouldAllowAhead = false;
                    MainActivity.this.finish();
                }
            }
        }


    }

    public String callOnClick() {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            String httppostURL = "http://nepatextdeals.com/nepa/androidweb";
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
                        Editor editor = pref.edit();

                        UrlResponse urlResponse = new Gson().fromJson(pref.getString(Constants.URL_RESPONSE_BEAN, ""), UrlResponse.class);
                        if (urlResponse != null) {
                             urlResponse.setNo_of_checkin(no_of_checkin);
                            urlResponse.setFree_gift(free_gift);
                            urlResponse.setDisclaimer_message(disclaimer_message);
                            urlResponse.setBusiness_logo(business_logo);
                            urlResponse.setButton_push_for_checkins(button_push_for_checkins);
                            urlResponse.setKiosk_mode(kiosk_mode);
                        }
                        editor.putString(Constants.URL_RESPONSE_BEAN,new Gson().toJson(urlResponse));
                        editor.apply();
                        kioskMode = kiosk_mode.equals("1") ? true : false;
                        return kioskMode + "";
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
        return null;
    }

    public void extractData() {
        if (validurl2 != null) {
            {
                isInternetPresent = cd.isConnectedToInternet();
                if (isInternetPresent) {
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        String httppostURL = "http://nepatextdeals.com/nepa/androidweb";
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
                            {
                                try {
                                    UrlResponse urlResponseNew = new Gson().fromJson(responseStr, UrlResponse.class);
                                    /*String merchant_kiosk_id = mainObject.getString("merchant_kiosk_id");
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
                                    String kiosk_mode = mainObject.getString("kiosk_mode");
                                    String button_push_for_checkins = mainObject.getString("button_push_for_checkins");
                                    String business_background_img = mainObject.getString("business_background_img");*/
                                    SharedPreferences pref = this.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
                                    urlResponse = new Gson().fromJson(pref.getString(Constants.URL_RESPONSE_BEAN, ""), UrlResponse.class);
                                    if (urlResponse != null) {
                                        urlResponseNew.setSubscriber_no_of_checkin(urlResponse.getSubscriber_no_of_checkin());
                                        urlResponseNew.setCoupon_code_description(urlResponse.getCoupon_code_description());
                                        urlResponseNew.setFree_gift_redeem(urlResponse.getFree_gift_redeem());
                                    }
                                    String business_background_img = urlResponseNew.getBusiness_background_img();
                                    String business_background_img_old = urlResponse.getBusiness_background_img();
                                    if (!business_background_img.equals(business_background_img_old)) {
                                        if (business_background_img != null && !business_background_img.isEmpty())
                                            new DownloadBackGroundTask(relativeLayout, this, true).execute(business_background_img);
                                    }

                                    Editor editor = pref.edit();
                                    editor.putString(Constants.URL_RESPONSE_BEAN, new Gson().toJson(urlResponseNew));
                                    /*editor.putString("merchant_kiosk_id", merchant_kiosk_id);
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
                                    editor.putString("kiosk_mode", kiosk_mode);
                                    editor.putString("organization_name", organization_name);
                                    editor.putString("status", status);
                                    editor.putString("disclaimer_message", disclaimer_message);
                                    editor.putString("button_push_for_checkins", button_push_for_checkins);
                                    editor.putString("business_background_img", business_background_img);*/
                                    editor.apply();

                                    shouldAllowAhead = true;
                                } catch (Exception e) {
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

class DownloadBackGroundTask extends AsyncTask<String, Void, Bitmap> {
    RelativeLayout relativeLayout;
    Context context;
    boolean forceUpdate;

    public DownloadBackGroundTask(RelativeLayout relativeLayout, Context context, boolean forceUpdate) {
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
        if (Build.VERSION.SDK_INT >= 16) {
            relativeLayout.setBackground(drawable);
        } else {
            relativeLayout.setBackgroundDrawable(drawable);
        }

        GlobalClass.background = result;
    }
}


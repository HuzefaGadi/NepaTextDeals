package com.webgentechnologies.nepatextdeals;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
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
import android.widget.RelativeLayout;
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
import java.util.Date;
import java.util.List;

public class RedeemActivity extends ApplicationActivity implements OnTouchListener {

    private Button buttonEnter, b1, b2, b3, b4, b5, b6, b7, b8, b9, clear, b0, back;
    ImageView imageView2;
    TextView footer;
    EditText edit_messageredeem;
    String merchant_id2, merchant_location_id2, user_id2, merchant_kiosk_id2;
    String imagelogo;
    View senceTouch;
    MyCount timerCount;
    private ProgressBar progressBar;
    ProgressDialog progressDialog;
    private static final String TAG = "RedeemActivity.java";
    ConnectionDetector connectionDetector;
    RelativeLayout relativeLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_redeem);
        addListenerOnButton();
        relativeLayout = (RelativeLayout) findViewById(R.id.layout_redeem);
        String business_background_img = pref.getString("business_background_img", null);

        if(business_background_img!=null)
        {
            new DownloadBackGroundTask(relativeLayout,this,false).execute(business_background_img);
        }
        StrictMode.enableDefaults();
        senceTouch = findViewById(R.id.layout_redeem);
        senceTouch.setOnTouchListener(this);
        timerCount = new MyCount(20 * 1000, 1000);
        timerCount.start();
        connectionDetector = new ConnectionDetector(this);
        SharedPreferences pref = this.getSharedPreferences("NepaTextDealsPref", Context.MODE_PRIVATE);
        String merchant_id1 = pref.getString("merchant_id", null);
        String merchant_location_id1 = pref.getString("merchant_location_id", null);
        String user_id1 = pref.getString("user_id", null);
        String merchant_kiosk_id1 = pref.getString("merchant_kiosk_id", null);
        String business_logo1 = pref.getString("business_logo", null);
        String disclaimer_message1 = pref.getString("disclaimer_message", null);

        footer = (TextView) findViewById(R.id.footer);
        footer.setText("By Signing Up You Agree To Receive Up To " + disclaimer_message1 + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");

        //Toast.makeText(getBaseContext(), business_logo, Toast.LENGTH_LONG).show();
        merchant_id2 = merchant_id1;
        merchant_location_id2 = merchant_location_id1;
        user_id2 = user_id1;
        merchant_kiosk_id2 = merchant_kiosk_id1;
        imagelogo = business_logo1;


        if (imagelogo == null) {

            //imageView2 = (ImageView) findViewById(R.id.imageView2);
            progressBar.setVisibility(View.VISIBLE);

        } else if (imagelogo != null) {

            progressBar.setVisibility(View.GONE);
            new DownloadImageTask((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);

        }


        String fontPath = "fonts/helvetica67medium.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        TextView txt = (TextView) findViewById(R.id.messageredeem);
        txt.setTypeface(tf);

        TextView footer = (TextView) findViewById(R.id.footer);
        footer.setTypeface(tf);

        TextView edit_messageredeem = (TextView) findViewById(R.id.edit_messageredeem);
        edit_messageredeem.setTypeface(tf);

        TextView b1 = (TextView) findViewById(R.id.b1);
        b1.setTypeface(tf);

        TextView b2 = (TextView) findViewById(R.id.b2);
        b2.setTypeface(tf);

        TextView b3 = (TextView) findViewById(R.id.b3);
        b3.setTypeface(tf);

        TextView b4 = (TextView) findViewById(R.id.b4);
        b4.setTypeface(tf);

        TextView b5 = (TextView) findViewById(R.id.b5);
        b5.setTypeface(tf);

        TextView b6 = (TextView) findViewById(R.id.b6);
        b6.setTypeface(tf);

        TextView b7 = (TextView) findViewById(R.id.b7);
        b7.setTypeface(tf);

        TextView b8 = (TextView) findViewById(R.id.b8);
        b8.setTypeface(tf);

        TextView b9 = (TextView) findViewById(R.id.b9);
        b9.setTypeface(tf);

        TextView b0 = (TextView) findViewById(R.id.b0);
        b0.setTypeface(tf);

        TextView clear = (TextView) findViewById(R.id.clear);
        clear.setTypeface(tf);

        TextView back = (TextView) findViewById(R.id.back);
        back.setTypeface(tf);

        TextView buttonenter = (TextView) findViewById(R.id.buttonenter);
        buttonenter.setTypeface(tf);

        progressDialog = new ProgressDialog(this,R.style.NewDialog);
        progressDialog.setMessage("Please wait..");
        progressDialog.setTitle("Redeeming");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
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
        buttonEnter = (Button) findViewById(R.id.buttonenter);
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
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar1);
        progressBar.setVisibility(View.GONE);
        edit_messageredeem = (EditText) findViewById(R.id.edit_messageredeem);
        edit_messageredeem.setGravity(Gravity.CENTER);
        edit_messageredeem.addTextChangedListener(new TextWatcher() {

            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                String str = edit_messageredeem.getText().toString();
                len = str.length();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                String str = edit_messageredeem.getText().toString();
                if ((str.length() == 3 && len < str.length()) || (str.length() == 7 && len < str.length())) {
                    edit_messageredeem.append("-");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

        buttonEnter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                buttonEnter.setEnabled(false);
                timerCount.cancel();

                String pass = edit_messageredeem.getText().toString();
                if (pass.equals("198-3")) {
                    buttonEnter.setEnabled(true);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (pass.equals("999-99")) {
                    try {
                        Process proc = Runtime.getRuntime()
                                .exec(new String[]{"su", "-c", "reboot -p"});
                        proc.waitFor();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    buttonEnter.setEnabled(true);
                } else if (pass.equals("111-983-")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RedeemActivity.this);
                    alertDialogBuilder.setMessage("Enter Password:");
                    final EditText input = new EditText(RedeemActivity.this);
                    alertDialogBuilder.setView(input);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    int maxLength = 6;
                    input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String name = input.getText().toString();
                            if (name.equals("111983")) {
                                startActivity(new Intent(RedeemActivity.this, UrlActivity.class));
                                RedeemActivity.this.finish();
                            } else {
                                Toast.makeText(RedeemActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    buttonEnter.setEnabled(true);
                } else if ((pass.length() < 11) && (!pass.equals("198-3")) && (!pass.equals("111-983-")) && (!pass.equals("999-99"))) {
                    showToastGeneric("Enter Full Coupon Code");
                    buttonEnter.setEnabled(true);
                }
                else if (connectionDetector.isConnectedToInternet()) {
                    String redeem = edit_messageredeem.getText().toString();
                    new CallWebService(redeem).execute();
                } else {
                    Toast.makeText(RedeemActivity.this, "No Internet Connection",
                            Toast.LENGTH_LONG).show();
                    buttonEnter.setEnabled(true);
                }
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "1");
                                      timerCount.cancel();
                                  }
                              }
        );
        b2.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "2");
                                      timerCount.cancel();
                                  }
                              }
        );
        b3.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "3");
                                      timerCount.cancel();
                                  }
                              }
        );
        b4.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "4");
                                      timerCount.cancel();
                                  }
                              }
        );
        b5.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "5");
                                      timerCount.cancel();
                                  }
                              }
        );
        b6.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "6");
                                      timerCount.cancel();
                                  }
                              }
        );
        b7.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "7");
                                      timerCount.cancel();
                                  }
                              }
        );
        b8.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "8");
                                      timerCount.cancel();
                                  }
                              }
        );
        b9.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "9");
                                      timerCount.cancel();
                                  }
                              }
        );
        clear.setOnClickListener(new Button.OnClickListener() {
                                     public void onClick(View view) {
                                         timerCount.cancel();
                                         Editable editableText = edit_messageredeem.getEditableText();
                                         int length = editableText.length();
                                         if (length > 0) {
                                             editableText.delete(length - length, length);
                                         }
                                     }
                                 }
        );
        b0.setOnClickListener(new Button.OnClickListener() {
                                  public void onClick(View view) {
                                      edit_messageredeem.setText(edit_messageredeem.getText() + "0");
                                      timerCount.cancel();
                                  }
                              }
        );
        back.setOnClickListener(new Button.OnClickListener() {
                                    public void onClick(View view) {
                                        Intent i = new Intent(RedeemActivity.this, MainScreenActivity.class);
                                        startActivity(i);
                                        RedeemActivity.this.finish();
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
            Intent mainIntent = new Intent(RedeemActivity.this, MainActivity.class);
            RedeemActivity.this.startActivity(mainIntent);
            RedeemActivity.this.finish();

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

    private void CallWebserviceOnPostExecute(String result)
    {
        try {
            String responseStr = result;
            if (responseStr != null) {
                JSONObject mainObject = new JSONObject(responseStr);
                String code_valid = mainObject.getString("code_valid");
                buttonEnter.setEnabled(true);
                if (code_valid.equals("Yes")) {
                    String free_gift = mainObject.getString("free_gift");
                    String coupon_code_description = mainObject.getString("coupon_code_description");
                    Intent i = new Intent(RedeemActivity.this, SuccessRedeemActivity.class);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("NepaTextDealsPref", Context.MODE_PRIVATE);
                    pref.edit().putString("free_gift_for_redeem", free_gift).commit();
                    pref.edit().putString("coupon_code_description", coupon_code_description).commit();
                    startActivity(i);
                    RedeemActivity.this.finish();
                } else if (code_valid.equals("No")) {
                    String status = mainObject.getString("status");
                    if (status.equals("1")) {
                        showToastGeneric("Invalid Coupon Code");
                    } else if (status.equals("2")) {
                        showToastGeneric("This Reward Code Has Already Been Redeemed");
                    } else if (status.equals("3")) {
                        showToastGeneric("Redemption Time Over");

                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            Toast.makeText(RedeemActivity.this, "Request failed:"+ e.toString(), Toast.LENGTH_LONG).show();
            buttonEnter.setEnabled(true);
        } catch (Exception e) {
            Toast.makeText(RedeemActivity.this, "Request failed: " + e.toString(),
                    Toast.LENGTH_LONG).show();

        } catch (Throwable t) {
            Toast.makeText(RedeemActivity.this, "Request failed: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
        edit_messageredeem.setText("");
        buttonEnter.setEnabled(true);
    }

    class CallWebService extends AsyncTask<Void, Void, String> {
        String redeem;
        Date date;

        public CallWebService(String redeem) {
            this.redeem = redeem;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            date= new Date();
        }

        protected String doInBackground(Void... urls) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                String httppostURL = "http://nepatextdeals.com/nepa/androidweb/redeemp";
                //String httppostURL = "http://192.168.0.254/nepadeals/androidweb/redeemp";
                HttpPost httppost = new HttpPost(httppostURL);
                Log.v(TAG, "postURL: " + httppost);

                JSONObject data1 = new JSONObject();
                data1.put("merchant_id", merchant_id2);
                data1.put("merchant_location_id", merchant_location_id2);
                data1.put("business_user_id", user_id2);
                data1.put("merchant_kiosk_id", merchant_kiosk_id2);
                data1.put("coupon_code", redeem);

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
                    //Toast.makeText(RedeemActivity.this,  responseStr, Toast.LENGTH_LONG).show();
                    // you can add an if statement here and do other actions based on the response
                    return responseStr;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(RedeemActivity.this, "Request failed: " + e.toString(),
                        Toast.LENGTH_LONG).show();

            } catch (Throwable t) {
                Toast.makeText(RedeemActivity.this, "Request failed: " + t.toString(),
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }


        protected void onPostExecute(final String result) {
            long totalMillis = new Date().getTime() - date.getTime();
            if (totalMillis > 3 * 1000) {
                progressDialog.cancel();
                CallWebserviceOnPostExecute(result);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        CallWebserviceOnPostExecute(result);
                    }
                }, 3000 - totalMillis);
            }

        }
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

    public void showToastGeneric(String message) {
        Toast toast = Toast.makeText(RedeemActivity.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(45);
        toastTV.setGravity(Gravity.CENTER);
        toastTV.setTextColor(Color.WHITE);
        toast.getView().setBackgroundResource(R.drawable.customtoast);
        toast.show();
    }
}


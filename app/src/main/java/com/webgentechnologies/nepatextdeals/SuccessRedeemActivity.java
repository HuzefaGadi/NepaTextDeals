package com.webgentechnologies.nepatextdeals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;

public class SuccessRedeemActivity extends Activity {

    private Button buttonReturn;
    TextView messagesuccessredeem2, footer;
    ImageView imageView2;
    String imagelogo;
    private ProgressBar progressBar;
    RelativeLayout relativeLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_successredeem);
        addListenerOnButton();
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
        SharedPreferences pref = this.getSharedPreferences("NepaTextDealsPref", Context.MODE_PRIVATE);
        String free_gift1 = pref.getString("free_gift_for_redeem", null);
        String coupon_code_description = pref.getString("coupon_code_description", null);
        String disclaimer_message1 = pref.getString("disclaimer_message", null);
        String business_logo1 = pref.getString("business_logo", null);
        relativeLayout = (RelativeLayout) findViewById(R.id.layout_successredeem);
        String business_background_img = pref.getString("business_background_img", null);

        if (business_background_img != null) {
            new DownloadBackGroundTask(relativeLayout, this, false).execute(business_background_img);
        }
        imagelogo = business_logo1;

        if (imagelogo == null) {

            //imageView2 = (ImageView) findViewById(R.id.imageView2);
            progressBar.setVisibility(View.VISIBLE);

        } else if (imagelogo != null) {

            progressBar.setVisibility(View.GONE);
            new DownloadImageTask6((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);

        }

        footer = (TextView) findViewById(R.id.footer);
        footer.setText("By Signing Up You Agree To Receive Up To " + disclaimer_message1 + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");


        messagesuccessredeem2 = (TextView) findViewById(R.id.messagesuccessredeem2);
        messagesuccessredeem2.setText(coupon_code_description);
        messagesuccessredeem2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        messagesuccessredeem2.setTypeface(null, Typeface.BOLD);

        String fontPath = "fonts/helvetica67medium.ttf";
        TextView txt = (TextView) findViewById(R.id.messagesuccessredeem);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt.setTypeface(tf);

        TextView txt2 = (TextView) findViewById(R.id.messagesuccessredeem2);
        txt2.setTypeface(tf);

        TextView txt1 = (TextView) findViewById(R.id.messagesuccessredeem1);
        txt1.setTypeface(tf);

        TextView txt3 = (TextView) findViewById(R.id.messagesuccessredeem3);
        txt3.setTypeface(tf);

        TextView footer = (TextView) findViewById(R.id.footer);
        footer.setTypeface(tf);

        TextView buttonreturn = (TextView) findViewById(R.id.buttonreturn);
        buttonreturn.setTypeface(tf);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
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
        buttonReturn = (Button) findViewById(R.id.buttonreturn);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar1);
        progressBar.setVisibility(View.GONE);
        buttonReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlert(view);
            }

            private void openAlert(View view) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuccessRedeemActivity.this);
                alertDialogBuilder.setMessage("Make Sure You Have Shown The Coupon Redemption Screen To The Cashier. Once You Leave This Screen, Your Reward Will Be Gone.");
                alertDialogBuilder.setPositiveButton("Yes I Have Redeemed My Coupon", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SuccessRedeemActivity.this);
                        alertDialogBuilder.setMessage("Are You Sure You Have Redeemed Your Coupon?");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(SuccessRedeemActivity.this, MainActivity.class);
                                startActivity(i);
                                SuccessRedeemActivity.this.finish();
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().getAttributes();

                        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                        textView.setTextSize(20);
                        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        btn1.setTextSize(18);
                        Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        btn2.setTextSize(18);

                    }

                });
                alertDialogBuilder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().getAttributes();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btn1.setTextSize(18);
                Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn2.setTextSize(18);
            }

        });
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
}

class DownloadImageTask6 extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask6(ImageView bmImage) {
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
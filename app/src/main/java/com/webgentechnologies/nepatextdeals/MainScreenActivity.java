package com.webgentechnologies.nepatextdeals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.webgentechnologies.nepatextdeals.beans.KioskData;
import com.webgentechnologies.nepatextdeals.beans.UrlResponse;
import com.webgentechnologies.nepatextdeals.utils.Constants;
import com.webgentechnologies.nepatextdeals.utils.GlobalClass;

import java.io.InputStream;
import java.util.List;

public class MainScreenActivity extends ApplicationActivity implements OnTouchListener {

    private Button checkIn, redeem;
    View senceTouch;
    MyCount timerCount;
    ImageView imageView2;
    TextView welcome1, welcome2, footer;
    String imagelogo;
    private ProgressBar progressBar;
    EditText textToProceed;
    boolean allowed = false;
    int push_button = 2;
    UrlResponse urlResponse;
    RelativeLayout relativeLayout;
    ViewAnimator viewAnimator;
    ImageButton previous, next;
    List<KioskData> listOfOffers;
    Animation slide_in_left, slide_out_right, slide_out_left, slide_in_right;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mainscreen);
        viewAnimator = (ViewAnimator) findViewById(R.id.viewanimator);
        previous = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);
        urlResponse = new Gson().fromJson(pref.getString(Constants.URL_RESPONSE_BEAN, ""), UrlResponse.class);
        String kioskModeString = "1";
        String free_gift1 = null;
        String no_of_checkin1 = null;
        String disclaimer_message1 = null;
        String business_logo1 = null;
        String business_background_img = null;
        push_button = 2;
        if (urlResponse != null) {
            kioskModeString = urlResponse.getKiosk_mode();
            free_gift1 = urlResponse.getFree_gift();
            no_of_checkin1 = urlResponse.getNo_of_checkin();
            disclaimer_message1 = urlResponse.getDisclaimer_message();
            business_logo1 = urlResponse.getBusiness_logo();
            business_background_img = urlResponse.getBusiness_background_img();
            push_button = Integer.parseInt(urlResponse.getButton_push_for_checkins());
            listOfOffers = urlResponse.getLoyalty_kiosk_data();
        }

        if (kioskModeString.equals("2")) {
            kioskMode = false;
        } else {
            kioskMode = true;
        }
        if (!kioskMode) {

            Intent intent = new Intent(this, CheckinActivity.class);
            startActivity(intent);
            finish();
        }

        addListenerOnButton();
        senceTouch = findViewById(R.id.layout_mainscreen);
        senceTouch.setOnTouchListener(this);

        relativeLayout = (RelativeLayout) findViewById(R.id.layout_mainscreen);
        if (business_background_img != null) {
            new DownloadBackGroundTask(relativeLayout, this, false).execute(business_background_img);
        }

        imagelogo = business_logo1;
        timerCount = new MyCount(20 * 1000, 1000);
        timerCount.start();
        textToProceed = (EditText) findViewById(R.id.textToProceed);
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
                    }, 10000);
                }
                return true;
            }
        };
        textToProceed.setOnEditorActionListener(exampleListener);
        if (imagelogo == null) {

            //imageView2 = (ImageView) findViewById(R.id.imageView2);
            progressBar.setVisibility(View.VISIBLE);

        } else if (imagelogo != null) {

            progressBar.setVisibility(View.GONE);
            new DownloadImageTask3((ImageView) findViewById(R.id.imageView2)).execute(imagelogo);

        }

        footer = (TextView) findViewById(R.id.footer);
        footer.setText("By Signing Up You Agree To Receive Up To " + disclaimer_message1 + " Sent To Your Mobile Phone. Message & Data Rates May Apply. Reply STOP To Stop.");

        welcome2 = (TextView) findViewById(R.id.welcome2);
        welcome2.setText(free_gift1);
        welcome2.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        welcome2.setTypeface(null, Typeface.BOLD);

        welcome1 = (TextView) findViewById(R.id.welcome1);
        welcome1.setText(no_of_checkin1 + " Check-Ins Earn a FREE");

        String fontPath = "fonts/helvetica67medium.ttf";
        TextView txt2 = (TextView) findViewById(R.id.welcome2);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        txt2.setTypeface(tf);

        TextView txt = (TextView) findViewById(R.id.welcome);
        txt.setTypeface(tf);

        TextView txt1 = (TextView) findViewById(R.id.welcome1);
        txt1.setTypeface(tf);

        TextView footer = (TextView) findViewById(R.id.footer);
        footer.setTypeface(tf);

        TextView redeem = (TextView) findViewById(R.id.redeem);
        redeem.setTypeface(tf);

        TextView checkin = (TextView) findViewById(R.id.checkin);
        checkin.setTypeface(tf);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (listOfOffers != null && !listOfOffers.isEmpty()) {
            for (KioskData offer : listOfOffers) {

                View ticket = View.inflate(this, R.layout.ticket_layout, null);
                ticket.setDrawingCacheEnabled(true);
                TextView offerText = (TextView) ticket.findViewById(R.id.offer);
                offerText.setText(offer.getFree_gift());
                TextView creditText = (TextView) ticket.findViewById(R.id.credits);
                creditText.setText(offer.getCheckin_credit());
                /*Bitmap imageBitmap = createBitmapFromLayoutWithText(ticket);
                View ticketImage = View.inflate(this, R.layout.ticket_layout_image, null);
                ImageView image = (ImageView) ticketImage.findViewById(R.id.imageView);
                image.setImageBitmap(imageBitmap);*/
                viewAnimator.addView(ticket);

            }
        }


        slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);


        viewAnimator.setInAnimation(slide_in_left);
        viewAnimator.setOutAnimation(slide_out_right);


        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                timerCount.cancel();
                viewAnimator.setInAnimation(slide_in_right);
                viewAnimator.setOutAnimation(slide_out_left);
                viewAnimator.showPrevious();
            }
        });

        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                timerCount.cancel();
                viewAnimator.setInAnimation(slide_in_left);
                viewAnimator.setOutAnimation(slide_out_right);
                viewAnimator.showNext();
            }
        });


    }

    /*public Bitmap createBitmapFromLayoutWithText(View view) {


        //Provide it with a layout params. It should necessarily be wrapping the
        //content as we not really going to have a parent for it.
        view.setLayoutParams(new RelativeLayout.LayoutParams(view.getWidth(),view.getHeight()));

        //Pre-measure the view so that height and width don't remain null.
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //Assign a size and position to the view and all of its descendants
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        //Create a canvas with the specified bitmap to draw into
        Canvas c = new Canvas(bitmap);

        //Render this view (and all of its children) to the given Canvas
        view.draw(c);
        return bitmap;
    }
*/
    class DownloadImageTask3 extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask3(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {

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

        if (!hasFocus) {
            Log.d("Focus debug", "Lost focus !");

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }


    private void addListenerOnButton() {
        checkIn = (Button) findViewById(R.id.checkin);
        redeem = (Button) findViewById(R.id.redeem);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar1);
        progressBar.setVisibility(View.GONE);


        //Onclick start new checkIn up activity
        checkIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (push_button == 2) {
                    allowed = true;
                }
                if (allowed) {
                    Intent i = new Intent(MainScreenActivity.this, CheckinActivity.class);
                    startActivity(i);
                    MainScreenActivity.this.finish();
                    timerCount.cancel();
                } else {
                    Toast toast = Toast.makeText(MainScreenActivity.this, "Authorization Required, See Attendant", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(45);
                    toastTV.setGravity(Gravity.CENTER);
                    toastTV.setTextColor(Color.WHITE);
                    toast.getView().setBackgroundResource(R.drawable.customtoast);
                    toast.show();
                    textToProceed.requestFocus();

                }

            }

        });

        //Onclick start new checkIn up activity
        redeem.setOnClickListener(new OnClickListener() {

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

package com.webgentechnologies.nepatextdeals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.webgentechnologies.nepatextdeals.beans.UrlResponse;
import com.webgentechnologies.nepatextdeals.utils.Constants;

/**
 * Created by huzefaasger on 19-08-2015.
 */

public class ApplicationActivity extends Activity {

    public static final long DISCONNECT_TIMEOUT = 1000*20; // 5 min = 5 * 60 * 1000 ms
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    boolean kioskMode;
    UrlResponse urlResponse;


    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            int activityToShow = pref.getInt("ACTIVITY_TO_SHOW",1);
            switch (activityToShow)
            {
                case 1:
                    if(kioskMode)
                    {
                        edit.putInt("ACTIVITY_TO_SHOW",2);
                    }
                    else
                    {
                        edit.putInt("ACTIVITY_TO_SHOW", 3);
                    }
                    edit.commit();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    break;
                case 2:
                    edit.putInt("ACTIVITY_TO_SHOW", 1);
                    edit.commit();
                    startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                    finish();
                    break;
                case 3:
                    edit.putInt("ACTIVITY_TO_SHOW", 1);
                    edit.commit();

                    startActivity(new Intent(getApplicationContext(), CheckinActivity.class));
                    finish();
                    break;
            }
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref= this.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        edit = pref.edit();
        String kioskModeString ="2";
        urlResponse = new Gson().fromJson(pref.getString(Constants.URL_RESPONSE_BEAN,""),UrlResponse.class);
        if(urlResponse!=null)
        {
            kioskModeString = urlResponse.getKiosk_mode();
        }
        if (kioskModeString.equals("2")) {
            kioskMode = false;
        } else {
            kioskMode = true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }
}

package com.precloud.deliverystar.Utility;

import android.content.Context;

import com.precloud.deliverystar.Api.ApiInterface;
import com.precloud.deliverystar.Api.RetrofitClient;

import androidx.multidex.MultiDexApplication;

public class DeliveryStarApplication extends MultiDexApplication {

    private static ApiInterface mAPIService;
    private static Context contextInstance;


    private static ApiInterface getAPIService() {
        return RetrofitClient.getClient().create(ApiInterface.class);
    }


    public static ApiInterface getmAPIService() {
        if (mAPIService == null) {
            mAPIService = getAPIService();
        }
        return mAPIService;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        contextInstance = getApplicationContext();
        //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public static Context getContextInstance() {
        return contextInstance;
    }

}


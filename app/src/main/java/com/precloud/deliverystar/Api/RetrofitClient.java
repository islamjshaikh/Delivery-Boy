package com.precloud.deliverystar.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.precloud.deliverystar.Utility.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder().
                    readTimeout(5, TimeUnit.MINUTES).
                    connectTimeout(5, TimeUnit.MINUTES).

                    retryOnConnectionFailure(true).
                    //cache(cache).
                            followSslRedirects(true).
                    //connectionSpecs(Collections.singletonList(spec)).
                            build();

            Gson gson = new GsonBuilder().setLenient().create();


            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)

                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}


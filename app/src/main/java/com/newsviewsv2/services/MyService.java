package com.newsviewsv2.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.newsviewsv2.model.Article;
import com.newsviewsv2.model.DataItem;
import com.newsviewsv2.model.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        Make the web service request
        MyWebService webService =
                MyWebService.retrofit.create(MyWebService.class);
        Call<DataItem> call = webService.dataItems();

        DataItem dataItem;
        ArrayList<Article> articles;

        try {
            dataItem = call.execute().body();
            articles = (ArrayList<Article>) dataItem.getArticles();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onHandleIntent: " + e.getMessage());
            return;
        }

//        Return the data to MainActivity
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD,  articles);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

}
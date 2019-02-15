package com.newsviewsv2.services;



import com.newsviewsv2.model.DataItem;
import com.newsviewsv2.utils.AppConstants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface MyWebService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET(AppConstants.FEED)
    Call<DataItem> dataItems();



}

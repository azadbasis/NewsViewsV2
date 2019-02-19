package com.newsviewsv2.services;


import com.newsviewsv2.model.DataItem;
import com.newsviewsv2.utils.AppConstants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyWebService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_NEWS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit retrofitForNumber = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_NUMBER)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();


    @GET(AppConstants.NEWS_FEED)
    Call<DataItem> dataItems();

    @GET("{number}/math")
    Call<String> math(@Path("number") String number);

    @GET("{number}")
    Call<String> trivia(@Path("number") String number);

    @GET("{month}/{day}/date")
    Call<String> date(@Path("month") String month, @Path("day") String day);

    @GET("{random}/date")
    Call<String> randomDate(@Path("random") String random);

    @GET("{random}/trivia")
    Call<String> randomTrivia(@Path("random") String random);

    @GET("{random}/year")
    Call<String> randomYear(@Path("random") String random);

    @GET("{random}/math")
    Call<String> randomMath(@Path("random") String random);


}

package com.example.navigationdrawerdemo.api;


import com.example.navigationdrawerdemo.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(

            @Query("country") String country,
            @Query("apiKey") String apiKey
            //@Query("category") String category
//自己加cate

    );
    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") String keyword,
            /*@Query("country") String country,*/
            @Query("language") String language,

            /*@Query("sources") String sources,*/

            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );
    @GET("top-headlines")
    Call<News> getCateNews(

            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("category") String category);
//自己加cate

}

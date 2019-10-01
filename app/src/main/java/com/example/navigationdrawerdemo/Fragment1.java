package com.example.navigationdrawerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.navigationdrawerdemo.api.ApiClient;
import com.example.navigationdrawerdemo.api.ApiInterface;
import com.example.navigationdrawerdemo.models.Article;
import com.example.navigationdrawerdemo.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View view;
    private RecyclerView recyclerView1;
    private List<Article> articles;
    RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter();
    public static final String API_KEY="a57a7724ff304cc1b9931aa252e4145a";
    private  int languageswitch=0;

    private SwipeRefreshLayout swipeRefreshLayout;


    public Fragment1() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment1,container,false);
        recyclerView1=(RecyclerView) view.findViewById(R.id.recycleView);
        recyclerViewAdapter.RecyclerViewAdaptersetter(getContext(),articles);//articles空的所以adapter是空的
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                intent = new Intent(getContext(),DetailActivity.class);

                Article article=articles.get(position);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("img",article.getUrlToImage());
                intent.putExtra("date",article.getPublishedAt());
                intent.putExtra("source",article.getSource().getName());
                intent.putExtra("author",article.getAuthor());

                startActivity(intent);
            }
        });


        recyclerView1.setAdapter(recyclerViewAdapter);


        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        onLoadingSwipeRefresh("");


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        articles = new ArrayList<>();

      /*  articles=new ArrayList<>();
        Article A=new Article(null, "123", "123", "123","123", "123", "123");
        articles.add(A);
        *///測試確定這樣RecyclerView會有東西

    }

    public void LoadJson(final String keyword){
        swipeRefreshLayout.setRefreshing(true);


        ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        String country="";
        String language="";

        //changelanguage;
        if (languageswitch == 0) {
            country=Utils.getCountry();
            language=Utils.getLanguage();
        }else if(languageswitch == 1){
            country=Utils.getUSA();
            language=Utils.getEnglish();
        }


        String sources=Utils.getSources();

        Call<News> call;
        if(keyword.length()>0){
            call =apiInterface.getNewsSearch(keyword,language,"publishedAt",API_KEY);
        }else{
            call=apiInterface.getNews(country,API_KEY);
        }
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()&&response.body().getArticle()!=null){
                    if(!articles.isEmpty()){
                        articles.clear();
                    }
                    articles=response.body().getArticle();
                    recyclerViewAdapter.RecyclerViewAdaptersetter(getContext(),articles);//裝進新文章
                    recyclerView1.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();


                    swipeRefreshLayout.setRefreshing(false);
                }else{
                    Toast.makeText(getContext(),"No Result!",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
}


    @Override
    public void onRefresh() {
        LoadJson("");
    //    initListener();
    }
    private void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);

                    }
                }
        );
    }


}

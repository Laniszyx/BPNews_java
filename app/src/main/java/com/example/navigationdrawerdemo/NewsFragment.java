package com.example.navigationdrawerdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.navigationdrawerdemo.models.Article;
import com.example.navigationdrawerdemo.models.Article;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private TabLayout tablayout;
    private ViewPager viewPager;
    public static final String API_KEY="a57a7724ff304cc1b9931aa252e4145a";
    private RecyclerView recyclerView;
    private List<Article> articles=new ArrayList<>();

    private String Tag=MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private  int languageswitch=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_newsapp,container,false);
        tablayout=(TabLayout)view.findViewById(R.id.tablayout_id);
        viewPager=(ViewPager)view.findViewById(R.id.viewpager_id);

        ViewPagerAdapter vadapter=new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        vadapter.AddFragment(new Fragment1(),"頭條");
        vadapter.AddFragment(new Fragment2(),"財經");
        vadapter.AddFragment(new Fragment3(),"健康");
        vadapter.AddFragment(new Fragment4(),"科技");
        vadapter.AddFragment(new Fragment5(),"運動");
        vadapter.AddFragment(new Fragment6(),"娛樂");
        vadapter.AddFragment(new Fragment7(),"學術");

        //adapter Setup
        viewPager.setAdapter(vadapter);
        tablayout.setupWithViewPager(viewPager);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}

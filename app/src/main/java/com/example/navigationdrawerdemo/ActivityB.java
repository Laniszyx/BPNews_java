package com.example.navigationdrawerdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class ActivityB extends AppCompatActivity {
    public static final int DEFAULT=0;
    public static final String TIMEDEFAULT="N/A";
    TextView TextView ;
    public Button loadbutton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        TextView=(TextView)findViewById(R.id.textview);
        TextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        loadbutton=findViewById(R.id.loadbutton);


        loadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load(v);
            }
        });


    }

    public void load(View view){
        SharedPreferences sharedPreS=this.getSharedPreferences("DataListS",MODE_PRIVATE);
        SharedPreferences sharedPreD=this.getSharedPreferences("DataListD",MODE_PRIVATE);
        SharedPreferences sharedPreT=this.getSharedPreferences("DataListT",MODE_PRIVATE);
        SharedPreferences sharedPreC=this.getSharedPreferences("DataListC",MODE_PRIVATE);

        StringBuilder stringBuilder=new StringBuilder();

        Map<String,?> countmap= sharedPreC.getAll();//以下取出count
        Set countset=countmap.keySet();
        List countarray= Arrays.asList(countset.toArray());
        //Object[] countarray=countset.toArray();
        Collections.sort(countarray, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {

                return -(Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString())));
            }
        });
        int count=0;
        for(int i=0;i<countarray.size();i++) {
            count = sharedPreC.getInt((String) countarray.get(i), 0);
            String time=sharedPreT.getString(String.valueOf(count),TIMEDEFAULT);
            int Svalue=sharedPreS.getInt(String.valueOf(count),0);
            int Dvalue=sharedPreD.getInt(String.valueOf(count),0);

            stringBuilder.append(count+" ,"+time+" ,"+"血壓:"+Svalue+"/"+Dvalue+"\n");
        }
//        for(int i=0;i<countarray.length;i++) {
//            count = sharedPreC.getInt((String) countarray[i], 0);
//            String time=sharedPreT.getString(String.valueOf(count),TIMEDEFAULT);
//            int Svalue=sharedPreS.getInt(String.valueOf(count),0);
//            int Dvalue=sharedPreD.getInt(String.valueOf(count),0);
//
//            stringBuilder.append(count+" ,"+time+" ,"+"血壓:"+Svalue+"/"+Dvalue+"\n");
//        }

        if(count==0){

            Toast.makeText(this,"尚無血壓記錄",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"讀取成功",Toast.LENGTH_LONG).show();
            TextView.setText(stringBuilder);

        }


    }
}

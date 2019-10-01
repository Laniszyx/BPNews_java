package com.example.navigationdrawerdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.navigationdrawerdemo.App.CHANNEL_1_ID;
import static com.example.navigationdrawerdemo.App.CHANNEL_2_ID;

public class RecordFragment extends Fragment {
    private static final String TAG = "MainActivity";
    private LineChart chart;
    private EditText textS;
    private EditText textD;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd H:mm");
    private Date date = new Date();

    List<String> Xvalues = new ArrayList<>();
    XAxis xAxis;

    ArrayList<Entry> valuesS = new ArrayList<>();
    ArrayList<Entry> valuesD = new ArrayList<>();

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    //new0722 test SharedPerferences
    public int count = 0;
    public ArrayList<Integer> CountList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        chart = view.findViewById(R.id.Linechart);

        textS = view.findViewById(R.id.textViewS);
        textD = view.findViewById(R.id.textViewD);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(11f);//字體大小

        xAxis.setTextColor(Color.RED);//顏色
        xAxis.setDrawLabels(true);//繪製該軸標籤
        //格線類設定
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸位置
        xAxis.setAxisMinimum(0);
        xAxis.setValueFormatter(new MyAxisValueFormatter(Xvalues));


        //設定Y軸
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMaximum(200f);
        yAxis.setAxisMinimum(0f);
        yAxis.enableGridDashedLine(10f, 10f, 0);
        chart.getAxisRight().setEnabled(false);//關閉右方y軸

        Button button = view.findViewById(R.id.buttonadd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Xvalues.add(dateFormat.format(new Date()));
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setTitle("注意");
                    builder.setMessage("登入血壓記錄"+textS.getText().toString()+"/"+textD.getText().toString());
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setData();
                            textD.getText().clear();
                            textS.getText().clear();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();

                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getBaseContext(), "請填入數值", Toast.LENGTH_LONG).show();
                }

            }
        });

        Button buttonclean = view.findViewById(R.id.buttonclean);
        buttonclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("注意");
                builder.setMessage("確定要刪除所有血壓記錄?");
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initialize();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });

        Button buttonnext = view.findViewById(R.id.buttonnext);
        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next(v);
            }
        });


        Xvalues.add("0");//開啟app不要讓x軸 nullpoint
//取出SP資料///////
        SharedPreferences sharedPreS = this.getActivity().getSharedPreferences("DataListS", MODE_PRIVATE);
        SharedPreferences sharedPreD = this.getActivity().getSharedPreferences("DataListD", MODE_PRIVATE);
        SharedPreferences sharedPreT = this.getActivity().getSharedPreferences("DataListT", MODE_PRIVATE);
        SharedPreferences sharedPreC = this.getActivity().getSharedPreferences("DataListC", MODE_PRIVATE);
        Map<String, ?> countmap = sharedPreC.getAll();//以下取出count
        Set countset = countmap.keySet();
        Object[] countarray = countset.toArray();
        if (countarray.length != 0) {
            int mcount = 0;
            for (int i = 0; i < countarray.length; i++) {
                mcount = sharedPreC.getInt((String) countarray[i], 0);
                String time = sharedPreT.getString(String.valueOf(mcount), "N/A");
                int Svalue = sharedPreS.getInt(String.valueOf(mcount), 0);
                int Dvalue = sharedPreD.getInt(String.valueOf(mcount), 0);

                Xvalues.add(time);
                valuesS.add(new Entry(valuesS.size(), Svalue));
                valuesD.add(new Entry(valuesD.size(), Dvalue));
                count = mcount;
            }
        }
        if (countarray.length != 0) {
            String temp = dateFormat.format(new Date());//這兩行是方便x軸產生，本來就有
            Xvalues.add(temp);
            //
            LineDataSet setS;
            LineDataSet setD;
            setS = new LineDataSet(valuesS, "收縮壓");//圖例的名稱
            setS.setColor(Color.RED);
            setS.setLineWidth(2f);
            setS.setCircleColors(Color.RED);

            setD = new LineDataSet(valuesD, "舒張壓");
            setD.setColor(Color.BLUE);
            setD.setLineWidth(2f);
            setD.setCircleColors(Color.BLUE);


            dataSets.add(setS);
            dataSets.add(setD);

            LineData mdata = new LineData(dataSets);

            mdata.setValueTextColor(Color.BLACK);//設定線圖上資料數字的顏色
            mdata.setValueTextSize(14f);//線圖資料數字的大小
            //完成塞入
            chart.setData(mdata);
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(5);//設置顯示數量
            chart.moveViewToX(mdata.getXMax());//讓畫面更新移往最新資料
            chart.bringToFront();
        }



        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //this.getActivity().setContentView(R.layout.fragment_record);




    }

    private void setData() {

        //test SharedPerferences

        SharedPreferences sharedPreS = this.getActivity().getSharedPreferences("DataListS", MODE_PRIVATE);
        SharedPreferences.Editor editorS = sharedPreS.edit();
        SharedPreferences sharedPreD = this.getActivity().getSharedPreferences("DataListD", MODE_PRIVATE);
        SharedPreferences.Editor editorD = sharedPreD.edit();
        SharedPreferences sharedPreT = this.getActivity().getSharedPreferences("DataListT", MODE_PRIVATE);
        SharedPreferences.Editor editorT = sharedPreT.edit();
        SharedPreferences sharedPreC = this.getActivity().getSharedPreferences("DataListC", MODE_PRIVATE);
        SharedPreferences.Editor editorC = sharedPreC.edit();
        //
        float sy = Integer.parseInt(textS.getText().toString());
        float dy = Integer.parseInt(textD.getText().toString());
        valuesS.add(new Entry(valuesS.size(), sy));
        valuesD.add(new Entry(valuesD.size(), dy));

        LineDataSet setS;
        LineDataSet setD;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            //new0722 放入sharedpreferences
            count++;
            CountList.add(count);
            String temp = dateFormat.format(new Date());
            Xvalues.set(Xvalues.size() - 2, temp);
            editorS.putInt(String.valueOf(count), (int) sy);
            editorS.commit();
            editorD.putInt(String.valueOf(count), (int) dy);
            editorD.commit();
            editorT.putString(String.valueOf(count), temp);
            editorT.commit();
            editorC.putInt(String.valueOf(count), count);
            editorC.commit();
            Toast.makeText(this.getActivity().getBaseContext(), "Saved", Toast.LENGTH_LONG).show();

            //
            // Xvalues.set(Xvalues.size()-2,dateFormat.format(new Date()));

            setS = (LineDataSet) chart.getData().getDataSetByIndex(0);
            setD = (LineDataSet) chart.getData().getDataSetByIndex(1);
            setS.setValues(valuesS);
            setD.setValues(valuesD);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(5);//設置顯示數量
            chart.moveViewToX(setS.getXMax());//讓畫面更新移往最新資料
        } else {
            //new0722 test SharedPerferences
            count++;
            CountList.add(count);
            String temp = dateFormat.format(new Date());
            Xvalues.add(temp);
            //
            editorS.putInt(String.valueOf(count), (int) sy);
            editorD.putInt(String.valueOf(count), (int) dy);
            editorT.putString(String.valueOf(count), temp);
            editorC.putInt(String.valueOf(count), count);
            editorS.commit();
            editorD.commit();
            editorT.commit();
            editorC.commit();
            Toast.makeText(this.getActivity().getBaseContext(), "Saved", Toast.LENGTH_LONG).show();
            //
            // Xvalues.add(dateFormat.format(new Date()));
            // create a dataset and give it a type
            setS = new LineDataSet(valuesS, "收縮壓");//圖例的名稱
            setS.setColor(Color.RED);
            setS.setLineWidth(2f);
            setS.setCircleColors(Color.RED);

            setD = new LineDataSet(valuesD, "舒張壓");
            setD.setColor(Color.BLUE);
            setD.setLineWidth(2f);
            setD.setCircleColors(Color.BLUE);


            dataSets.add(setS);
            dataSets.add(setD);

            LineData mdata = new LineData(dataSets);

            mdata.setValueTextColor(Color.BLACK);//設定線圖上資料數字的顏色
            mdata.setValueTextSize(14f);//線圖資料數字的大小
            //完成塞入
            chart.setData(mdata);
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(5);//設置顯示數量
            chart.moveViewToX(mdata.getXMax());//讓畫面更新移往最新資料
            chart.bringToFront();

        }
    }

    public class MyAxisValueFormatter implements IAxisValueFormatter {
        private List<String> mvalues = new ArrayList<String>();

        public MyAxisValueFormatter(List<String> values) {
            this.mvalues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mvalues.get((int) value + 1);
        }
    }

    public void next(View view){
       // Toast.makeText(this.getActivity().getBaseContext(),"Next",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this.getActivity(), ActivityB.class);
        startActivity(intent);
    }

    public void initialize(){
        SharedPreferences sharedPreS=this.getActivity().getSharedPreferences("DataListS",MODE_PRIVATE);
        SharedPreferences.Editor editorS=sharedPreS.edit();
        SharedPreferences sharedPreD=this.getActivity().getSharedPreferences("DataListD",MODE_PRIVATE);
        SharedPreferences.Editor editorD=sharedPreD.edit();
        SharedPreferences sharedPreT=this.getActivity().getSharedPreferences("DataListT",MODE_PRIVATE);
        SharedPreferences.Editor editorT=sharedPreT.edit();
        SharedPreferences sharedPreC=this.getActivity().getSharedPreferences("DataListC",MODE_PRIVATE);
        SharedPreferences.Editor editorC=sharedPreC.edit();

        editorS.clear();
        editorS.commit();
        editorD.clear();
        editorD.commit();
        editorT.clear();
        editorT.commit();
        editorC.clear();
        editorC.commit();


        Toast.makeText(this.getActivity().getBaseContext(),"已清除血壓記錄",Toast.LENGTH_LONG).show();
        Restart();

    }
    public void Restart(){
        Intent intent=this.getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(this.getActivity().getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.getActivity().finish();
        Toast.makeText(this.getActivity().getBaseContext(),"已清除血壓記錄",Toast.LENGTH_LONG).show();
    }




}

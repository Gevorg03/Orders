package com.example.orders.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.orders.AlertReceiver;
import com.example.orders.ListItem;
import com.example.orders.MainAdapter;
import com.example.orders.MyOrdersShowActivity;
import com.example.orders.NotificationHelper;
import com.example.orders.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomeFragment extends Fragment implements MainAdapter.OnItemClickListener{
    Toolbar toolbar;
    TextView tv_toolbar_text_price,tv_toolbar_text,tv_error;
    RecyclerView recyclerView;
    private static List<ListItem> listItems,listItems1;
    static MainAdapter myAdapter;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadMyOrders.php";
    private Context mContext;
    static int b;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView img_not_wifi;
    ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tv_toolbar_text_price = root.findViewById(R.id.tv_toolbar_text_price);
        tv_toolbar_text = root.findViewById(R.id.tv_toolbar_text);
        recyclerView = root.findViewById(R.id.recyclerview);
        swipeRefreshLayout = root.findViewById(R.id.swipe);
        img_not_wifi = root.findViewById(R.id.img_not_wifi_home);
        tv_error = root.findViewById(R.id.tv_error_home);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerViewData();

                myAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();

        toolbar = root.findViewById(R.id.toolbar_home);
        tv_toolbar_text.setText("Supermarket");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        listItems1 = new ArrayList<>();
        loadRecyclerViewData();
        //loadRecyclerViewDataUpdate();

        return root;
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getActivity()
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String datetime = simpleDateFormat.format(calendar.getTime());
        NotificationHelper notificationHelper =
                new NotificationHelper(getContext(),datetime);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent,
                0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    private void loadRecyclerViewData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        tv_error.setText("");
                        img_not_wifi.setImageResource(R.drawable.ic_white);
                        progressDialog.dismiss();
                        try {
                            b = 0;
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            listItems.clear();
                            for(int j = 0; j< array.length(); j++){
                                JSONObject o = array.getJSONObject(j);

                                 String name = o.getString("name");
                                 String price = o.getString("price");
                                 String ordering_address = o.getString("ordering_name");
                                 String ordering_phone = o.getString("ordering_phone");
                                 String datetime = o.getString("datetime");

                                String a = "";
                                for(int i = 0; i < 10;i++){
                                    String c = datetime;
                                    char b = c.charAt(i);
                                    String d = String.valueOf(b);
                                    a +=d;
                                }
                                a += "T";
                                for(int i = 11;i<19;i++){
                                    String c = datetime;
                                    char b = c.charAt(i);
                                    String d = String.valueOf(b);
                                    a +=d;
                                }
                                a+=".111Z";
                                //Toast.makeText(context,a,Toast.LENGTH_SHORT).show();
                                SimpleDateFormat readDate = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
                                Date date = null;
                                try {
                                    date = readDate.parse(a);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat writeDate = new SimpleDateFormat(
                                        "dd-MM-yyyy HH:mm:ss");
                                writeDate.setTimeZone(TimeZone.getTimeZone("GMT+04:00"));
                                String s2 = writeDate.format(date);
                                String s1 = "";
                                for(int i = 0;i<16;i++){
                                    String c = s2;
                                    char b = c.charAt(i);
                                    String d = String.valueOf(b);
                                    s1 +=d;
                                }
                                ListItem item = new ListItem(name,ordering_phone,ordering_address,
                                         datetime,s1);
                                listItems.add(item);
                                b = b + Integer.parseInt(price);
                            }
                            tv_toolbar_text_price.setText("Գումար: " + String.valueOf(b) + " AMD");
                            myAdapter = new MainAdapter(listItems,getContext());
                            recyclerView.setAdapter(myAdapter);
                            myAdapter.setOnItemClickListener(HomeFragment.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        listItems.clear();
                        tv_error.setText("Կապի խափանում");
                        img_not_wifi.setImageResource(R.drawable.ic_not_wifi);

                    }
                });
        if(mContext != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }

    private void loadRecyclerViewDataUpdate(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            b = 0;
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            listItems1.clear();
                            for(int j = 0; j< array.length(); j++){
                                JSONObject o = array.getJSONObject(j);

                                String name = o.getString("name");
                                String price = o.getString("price");
                                String ordering_address = o.getString("ordering_name");
                                String ordering_phone = o.getString("ordering_phone");
                                String datetime = o.getString("datetime");

                                String a = "";
                                for(int i = 0; i < 10;i++){
                                    String c = datetime;
                                    char b = c.charAt(i);
                                    String d = String.valueOf(b);
                                    a +=d;
                                }
                                a += "T";
                                for(int i = 11;i<19;i++){
                                    String c = datetime;
                                    char b = c.charAt(i);
                                    String d = String.valueOf(b);
                                    a +=d;
                                }
                                a+=".111Z";
                                //Toast.makeText(context,a,Toast.LENGTH_SHORT).show();
                                SimpleDateFormat readDate = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
                                Date date = null;
                                try {
                                    date = readDate.parse(a);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat writeDate = new SimpleDateFormat(
                                        "dd-MM-yyyy HH:mm:ss");
                                writeDate.setTimeZone(TimeZone.getTimeZone("GMT+04:00"));
                                String s2 = writeDate.format(date);
                                String s1 = "";
                                for(int i = 0;i<16;i++){
                                    String c = s2;
                                    char b = c.charAt(i);
                                    String d = String.valueOf(b);
                                    s1 +=d;
                                }
                                ListItem item = new ListItem(name,ordering_phone,ordering_address,
                                        datetime,s1);
                                listItems1.add(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        listItems1.clear();
                    }
                });
        if(mContext != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
        if(listItems.size() < listItems1.size()){
            Calendar c = Calendar.getInstance();
            String second = "",minute = "",hour = "";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String datetime = simpleDateFormat.format(c.getTime());
            for(int i = 11; i < 13; i++)
                hour +=String.valueOf(datetime.charAt(i));
            for(int i = 14; i < 16; i++)
                minute +=String.valueOf(datetime.charAt(i));
            for(int i = 17; i < 19; i++)
                second +=String.valueOf(datetime.charAt(i));

            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            c.set(Calendar.MINUTE, Integer.parseInt(minute));
            c.set(Calendar.SECOND, Integer.parseInt(second) + 1);
            startAlarm(c);
            loadRecyclerViewData();
        }
        refresh(1000);
    }

    public void refresh(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //loadRecyclerViewDataUpdate();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }

    @Override
    public void onItemClick(int position) {
        ListItem listItem = listItems.get(position);
        Intent intent = new Intent(getActivity(), MyOrdersShowActivity.class);
        intent.putExtra("datetime",String.valueOf(listItem.getDatetime3_without_second3()));
        startActivity(intent);
    }
}
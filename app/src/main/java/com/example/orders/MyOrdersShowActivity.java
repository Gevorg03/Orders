package com.example.orders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.orders.ui.AddProductFragment;
import com.example.orders.ui.AddProductTypeFragment;
import com.example.orders.ui.DeleteProductFragment;
import com.example.orders.ui.DeleteProductTypeFragment;
import com.example.orders.ui.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MyOrdersShowActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static List<ListItem> listItems;
    RecyclerView recyclerView;
    ImageView img_not_wifi;
    TextView tv_error;
    ProgressDialog progressDialog;
    List<ListItem> outputList;
    static MyOrdersShowAdapter myOrdersShowAdapter;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/Read.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_show);

        toolbar = findViewById(R.id.toolbar_my_orders_show);
        toolbar.setTitle("Supermarket");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_add_product:{
                        AddProductFragment cartFragment = new AddProductFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,cartFragment).commit();
                        return true;
                    }case R.id.navigation_home:{
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,homeFragment).commit();
                        return true;
                    }case R.id.navigation_add_product_type:{
                        AddProductTypeFragment myOrdersFragment = new AddProductTypeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,myOrdersFragment).commit();
                        return true;
                    }case R.id.navigation_delete_product:{
                        DeleteProductFragment accountFragment = new DeleteProductFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,accountFragment).commit();
                        return true;
                    }case R.id.navigation_delete_product_type:{
                        DeleteProductTypeFragment accountFragment = new DeleteProductTypeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,accountFragment).commit();
                        return true;
                    }
                }
                return false;
            }
        });

        img_not_wifi = findViewById(R.id.img_not_wifi_my_orders_show);
        tv_error = findViewById(R.id.tv_error_my_orders_show);
        recyclerView = findViewById(R.id.recyclerview_my_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        //refresh(1000);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        tv_error.setText("");
                        img_not_wifi.setImageResource(R.drawable.ic_white);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            listItems.clear();
                            Intent intent = getIntent();
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject productsJsonObject = array.getJSONObject(j);
                                String datetime = productsJsonObject.getString("datetime");
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
                                SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
                                Date date = null;
                                try {
                                    date = readDate.parse(a);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat writeDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                writeDate.setTimeZone(TimeZone.getTimeZone("GMT+04:00"));
                                String s2 = writeDate.format(date);
                                String s1 = "";
                                for(int i = 0;i<16;i++){
                                    String c = s2;
                                    char b = c.charAt(i);
                                    String d = String.valueOf(b);
                                    s1 +=d;
                                }
                                if (intent.getStringExtra("datetime").equals(s1)) {
                                    String price = productsJsonObject.getString("price");
                                    String count = productsJsonObject.getString("count");
                                    String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                    String price1 = String.valueOf(Integer.parseInt(price) / Integer.parseInt(count));
                                    ListItem item = new ListItem(count, price1, price, img, 0);
                                    listItems.add(item);
                                }
                            }
                            myOrdersShowAdapter = new MyOrdersShowAdapter(getApplicationContext(), listItems);
                            recyclerView.setAdapter(myOrdersShowAdapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void refresh(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadRecyclerViewData();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }
}


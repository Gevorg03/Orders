package com.example.orders.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.orders.BottomNavigationActivity;
import com.example.orders.DeleteProductTypeAdapter;
import com.example.orders.ListItem;
import com.example.orders.R;
import com.example.orders.retrofits.RetrofitClientDeleteProduct;
import com.example.orders.retrofits.RetrofitClientDeleteProductType;
import com.example.orders.services.APIServiceDeleteProduct;
import com.example.orders.services.APIServiceDeleteProductType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class DeleteProductTypeFragment extends Fragment implements DeleteProductTypeAdapter.OnItemClickListener{
    Toolbar toolbar;
    ProgressDialog progressDialog;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadProductsType.php";
    RecyclerView recyclerView;
    static DeleteProductTypeAdapter deleteProductTypeAdapter;
    private static List<ListItem> listItems;
    private Context mContext;
    TextView tv_error;
    ImageView img_not_wifi;

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
        View root = inflater.inflate(R.layout.fragment_delete_product_type, container, false);

        toolbar = root.findViewById(R.id.toolbar_delete_product_type);
        toolbar.setTitle("Supermarket");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        img_not_wifi = root.findViewById(R.id.img_not_wifi_delete_product_type);
        tv_error = root.findViewById(R.id.tv_error_delete_product_type);
        recyclerView = root.findViewById(R.id.recyclerview_type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        loadRecyclerViewData();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();

        return root;
    }

    public void deletePostProductType(long id) {
        Retrofit client = RetrofitClientDeleteProductType.getClient();
        final APIServiceDeleteProductType mAPIService = client.create(APIServiceDeleteProductType.class);
        mAPIService.deletePost(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i("tag", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    private void loadRecyclerViewData() {
        //refresh(1000);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        tv_error.setText("");
                        img_not_wifi.setImageResource(R.drawable.ic_white);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            listItems.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject productsJsonObject = array.getJSONObject(i);
                                String id = productsJsonObject.getString("id");
                                String description = productsJsonObject.getString("description");
                                String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                String img1 = productsJsonObject.getString("img");
                                ListItem item = new ListItem(id,description, img,img1);
                                listItems.add(item);
                            }
                            deleteProductTypeAdapter = new DeleteProductTypeAdapter(getContext(), listItems);
                            deleteProductTypeAdapter.setOnItemClickListener(DeleteProductTypeFragment.this);
                            recyclerView.setAdapter(deleteProductTypeAdapter);

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
        if (mContext != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onDeleteClick(final int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        Button btn_ok = view.findViewById(R.id.btn_dialog_ok);
        Button btn_cancel = view.findViewById(R.id.btn_dialog_cancel);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ListItem listItem = listItems.get(position);
                deletePostProductType(Long.parseLong(listItem.getId2()));
                Toast.makeText(getContext(),"Ապրանքատեսակը ջնջված է",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                refresh(1000);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onEditClick(int position) {
        ListItem listItem = listItems.get(position);
        AddProductTypeFragment addProductTypeFragment = new AddProductTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("edit","edit");
        bundle.putString("id",listItem.getId2());
        bundle.putString("description",listItem.getDescription());
        bundle.putString("img",listItem.getImg());
        bundle.putString("img_short",listItem.getImg4());
        addProductTypeFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.delete_product_type_fragment,
                addProductTypeFragment).commit();
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

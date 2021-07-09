package com.example.orders.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.orders.BottomNavigationActivity;
import com.example.orders.R;
import com.example.orders.retrofits.RetrofitClientAddProduct;
import com.example.orders.retrofits.RetrofitClientUpdateProductMarket;
import com.example.orders.retrofits.RetrofitClientUpdateProductMarket1;
import com.example.orders.services.APIServiceAddProduct;
import com.example.orders.services.APIServiceAddProductType;
import com.example.orders.services.APIServiceUpdateProductMarket;
import com.example.orders.services.APIServiceUpdateProductMarket1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends Fragment {
    Toolbar toolbar;
    private static EditText ed_name,ed_price,ed_count,ed_type;
    private static String name,price1,count1,type;
    private Context mContext;
    private ImageView img_choose;
    Button btn_choose;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    ImageButton img_btn_done;
    TextView tv_toolbar_text;
    int a = 0;
    String edit = "",id = "",name2 = "",price2 = "",count2 = "",description2 = "",img2 = "",img_short = "";

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
        final View root = inflater.inflate(R.layout.fragment_add_product, container, false);

        tv_toolbar_text = root.findViewById(R.id.tv_toolbar_text_add_product);
        toolbar = root.findViewById(R.id.toolbar_add_product);
        tv_toolbar_text.setText("Supermarket");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ed_name = root.findViewById(R.id.ed_name);
        ed_price = root.findViewById(R.id.ed_price);
        ed_count = root.findViewById(R.id.ed_count);
        ed_type = root.findViewById(R.id.ed_type);

        img_choose = root.findViewById(R.id.img_choose_product);
        btn_choose = root.findViewById(R.id.btn_choose_product);
        img_btn_done = root.findViewById(R.id.btn_add_product);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            edit = bundle.getString("edit");
            id = bundle.getString("id");
            name2 = bundle.getString("name");
            price2 = bundle.getString("price");
            count2 = bundle.getString("count");
            img2 = bundle.getString("img");
            img_short = bundle.getString("img_short");
            description2 = bundle.getString("description");
            ed_name.setText(name2);
            ed_price.setText(price2);
            ed_count.setText(count2);
            ed_type.setText(description2);
            Glide
                    .with(root)
                    .load(img2)
                    .centerCrop()
                    //.placeholder(R.drawable.imagesign)
                    .into(img_choose);
            img_choose.setVisibility(View.VISIBLE);
        }
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        img_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ed_name.getText().toString();
                price1 = ed_price.getText().toString();
                count1 = ed_count.getText().toString();
                type = ed_type.getText().toString();
                if(!name.equals("")){
                    if(!price1.equals("")){
                        if(!count1.equals("")) {
                            if (!type.equals("")) {
                                openDialog();
                            } else {
                                Toast.makeText(mContext, "Լրացնել Բաժինը", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(mContext,"Լրացնել քանակը",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(mContext,"Լրացնել գինը",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext,"Լրացնել անունը",Toast.LENGTH_SHORT).show();
                }

            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                a = 1;
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
                img_choose.setImageBitmap(bitmap);
                img_choose.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }

    private void uploadImage(){
        String Image = imageToString();

        Retrofit client = RetrofitClientAddProduct.getClient();
        APIServiceAddProduct mAPIService = client.create(APIServiceAddProduct.class);
        mAPIService.savePost(ed_name.getText().toString(),Double.parseDouble(ed_price.getText()
                .toString()),Integer.parseInt(ed_count.getText().toString()),
                Image,ed_type.getText().toString(),"dont").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    public void updateProductMarket(long id,String name,double price,int count,String img,
                                    String type,String img_update){
        Retrofit client = RetrofitClientUpdateProductMarket.getClient();
        APIServiceUpdateProductMarket mAPIService = client.create(APIServiceUpdateProductMarket.class);
        mAPIService.savePost(id,name,price,count,img,type,img_update).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    public void updateProductMarket1(long id,String name,double price,int count,String img,
                                    String type,String img_update){
        Retrofit client = RetrofitClientUpdateProductMarket1.getClient();
        APIServiceUpdateProductMarket1 mAPIService = client.create(APIServiceUpdateProductMarket1.class);
        mAPIService.savePost(id,name,price,count,img,type,img_update).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    private void openDialog() {
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
                if(edit.equals("")) {
                    uploadImage();
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Ապրանքն ավելացված է", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), BottomNavigationActivity.class));
                }else{
                    if(a == 1) {
                        img_short = imageToString();
                        String img_update = "update";
                        updateProductMarket(Long.parseLong(id),ed_name.getText().toString(),
                                Double.parseDouble(ed_price.getText().toString()),
                                Integer.parseInt(ed_count.getText().toString()),
                                img_short,ed_type.getText().toString(),img_update);
                    }else{
                        String img_update = "dont";
                        updateProductMarket1(Long.parseLong(id),ed_name.getText().toString(),
                                Double.parseDouble(ed_price.getText().toString()),
                                Integer.parseInt(ed_count.getText().toString()),
                                img_short,ed_type.getText().toString(),img_update);
                    }
                    Toast.makeText(getContext(), "Ապրանքը փոփոխված է", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), BottomNavigationActivity.class));
                    dialog.dismiss();
                }

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
}
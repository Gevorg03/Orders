package com.example.orders.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.orders.retrofits.RetrofitClientAddProductType;
import com.example.orders.retrofits.RetrofitClientUpdateProductMarket;
import com.example.orders.retrofits.RetrofitClientUpdateProductType;
import com.example.orders.retrofits.RetrofitClientUpdateProductType1;
import com.example.orders.services.APIServiceAddProductType;
import com.example.orders.services.APIServiceUpdateProductMarket;
import com.example.orders.services.APIServiceUpdateProductType;
import com.example.orders.services.APIServiceUpdateProductType1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class AddProductTypeFragment extends Fragment {
    Toolbar toolbar;
    private static EditText ed_type;
    private static String description;
    private ImageView img_choose;
    Button btn_choose;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private Context mContext;
    ImageButton img_btn_done;
    TextView tv_toolbar_text;
    int a = 0;
    String edit = "",id = "",description1 = "",img = "",img_short;

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
        View root = inflater.inflate(R.layout.fragment_add_product_type, container, false);

        tv_toolbar_text = root.findViewById(R.id.tv_toolbar_text_add_product_type);
        toolbar = root.findViewById(R.id.toolbar_add_product_type);
        tv_toolbar_text.setText("Supermarket");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        img_btn_done = root.findViewById(R.id.btn_add_product_type);
        ed_type = root.findViewById(R.id.ed_type_type);
        img_choose = root.findViewById(R.id.img_choose);
        btn_choose = root.findViewById(R.id.btn_choose);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            edit = bundle.getString("edit");
            id = bundle.getString("id");
            img = bundle.getString("img");
            img_short = bundle.getString("img_short");
            description1 = bundle.getString("description");
            ed_type.setText(description1);
            Glide
                    .with(root)
                    .load(img)
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
                description = ed_type.getText().toString();
                if(!description.equals("")){
                    openDialog();
                }else
                    Toast.makeText(getContext(),"Լրացնել բաժինը",Toast.LENGTH_SHORT).show();
            }
        });

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
        scaleDown(bitmap, 700f, true);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }

    private void uploadImage(){
        String Image = imageToString();
        Retrofit client = RetrofitClientAddProductType.getClient();
        APIServiceAddProductType mAPIService = client.create(APIServiceAddProductType.class);
        mAPIService.savePost(description,Image,"dont").enqueue(new Callback<ResponseBody>() {
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

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public void updateProductType(long id, String type,String img,String img_update){
        Retrofit client = RetrofitClientUpdateProductType.getClient();
        APIServiceUpdateProductType mAPIService = client.create(APIServiceUpdateProductType.class);
        mAPIService.savePost(id,type,img,img_update).enqueue(new Callback<ResponseBody>() {
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
    public void updateProductType1(long id, String type,String img,String img_update){
        Retrofit client = RetrofitClientUpdateProductType1.getClient();
        APIServiceUpdateProductType1 mAPIService = client.create(APIServiceUpdateProductType1.class);
        mAPIService.savePost(id,type,img,img_update).enqueue(new Callback<ResponseBody>() {
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
                    Toast.makeText(getContext(),"Բաժինը ավելացված է",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), BottomNavigationActivity.class));
                }else {
                    if (a == 1) {
                        img_short = imageToString();
                        String img_update = "update";
                        updateProductType(Long.parseLong(id), ed_type.getText().toString(),
                                img_short, img_update);
                    } else {
                        String img_update = "dont";
                        updateProductType1(Long.parseLong(id), ed_type.getText().toString(),
                                img_short, img_update);
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
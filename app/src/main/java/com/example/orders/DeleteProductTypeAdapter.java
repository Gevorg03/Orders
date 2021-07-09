package com.example.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DeleteProductTypeAdapter extends RecyclerView.Adapter<DeleteProductTypeAdapter.DeleeteTypeViewHolder> {

    private OnItemClickListener mListener;
    Context context;
    private List<ListItem> listItems;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(DeleteProductTypeAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public DeleteProductTypeAdapter(Context ct, List<ListItem> listItems){
        context = ct;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public DeleeteTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_type_product,parent,false);
        return new DeleeteTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleeteTypeViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.tv_title.setText(listItem.getDescription());

        Glide
                .with(context)
                .load(listItem.getImg())
                .centerCrop()
                //.placeholder(R.drawable.imagesign)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class DeleeteTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView img,img_delete,img_edit;
        TextView tv_title;
        public DeleeteTypeViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title_product_type);
            img = itemView.findViewById(R.id.img_type);
            img_delete = itemView.findViewById(R.id.btn_img_delete);
            img_edit = itemView.findViewById(R.id.btn_img_edit);

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });

            img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }
}

package com.example.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DeleteProductAdapter extends RecyclerView.Adapter<DeleteProductAdapter.DeleteProductViewHolder> implements Filterable {

    private OnItemClickListener mListener;
    Context context;
    private List<ListItem> listItems;
    private List<ListItem> listItems1;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(DeleteProductAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public DeleteProductAdapter(Context ct, List<ListItem> listItems){
        context = ct;
        this.listItems = listItems;
        listItems1 = new ArrayList<>(listItems);
    }
    @NonNull
    @Override
    public DeleteProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_product,parent,false);
        return new DeleteProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteProductViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.tv_title.setText(listItem.getName1());
        holder.tv_count.setText(listItem.getCount1() + " հատ");
        holder.tv_price.setText(listItem.getPrice1() + " AMD");

        Glide
                .with(context)
                .load(listItem.getImg1())
                .centerCrop()
                //.placeholder(R.drawable.imagesign)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class DeleteProductViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageButton img_delete,img_edit;
        TextView tv_title,tv_count,tv_price;
        public DeleteProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_name_product);
            tv_count = itemView.findViewById(R.id.tv_count_product);
            tv_price = itemView.findViewById(R.id.tv_price_product);
            img = itemView.findViewById(R.id.img_product);
            img_delete = itemView.findViewById(R.id.img_delete_product);
            img_edit = itemView.findViewById(R.id.img_edit_product);

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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ListItem> filteredList = new ArrayList<>();
            if(constraint== null || constraint.length() == 0){
                filteredList.addAll(listItems1);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(ListItem item: listItems1){
                    if(item.getName1().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listItems.clear();
            listItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

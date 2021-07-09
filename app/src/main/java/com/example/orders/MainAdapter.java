package com.example.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private OnItemClickListener mListener;
    private List<ListItem> listItems;
    private Context context;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public MainAdapter(List<ListItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.tv_ordering_name.setText("Պատվիրող: " + listItem.getOrderingName3());
        holder.tv_ordering_phone.setText("Հեռախոսահամար: " + listItem.getOrderingPhone3());
        holder.tv_ordering_address.setText("Հասցե: " + listItem.getAddress3());

        String a = "";
        for(int i = 0; i < 10;i++){
            String c = listItem.getDatetime3();
            char b = c.charAt(i);
            String d = String.valueOf(b);
            a +=d;
        }
        a += "T";
        for(int i = 11;i<19;i++){
            String c = listItem.getDatetime3();
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
        String s = writeDate.format(date);
        String s1 = "";
        for(int i = 0;i<16;i++){
            String c = s;
            char b = c.charAt(i);
            String d = String.valueOf(b);
            s1 +=d;
        }

        holder.tv_ordering_datetime.setText("Ժամանակ: " + s1);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_ordering_name,tv_ordering_address,tv_ordering_phone,tv_ordering_datetime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ordering_name = itemView.findViewById(R.id.tv_ordering_name);
            tv_ordering_phone = itemView.findViewById(R.id.tv_ordering_phone);
            tv_ordering_address = itemView.findViewById(R.id.tv_ordering_address);
            tv_ordering_datetime = itemView.findViewById(R.id.tv_ordering_datetime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
}

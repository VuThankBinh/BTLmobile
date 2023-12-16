package com.example.myapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.Models.PopulartModel;
import com.example.myapp.R;
import com.example.myapp.TrangChu;
import com.example.myapp.adapter.RecyclerViewSPClickListener;
import com.example.myapp.infoItemView;

import java.util.ArrayList;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private Context context;
    private RecyclerViewSPClickListener mlistener;
    public PopularAdapter(Context context, ArrayList<PopulartModel> list,RecyclerViewSPClickListener m) {
        this.context = context;
        this.list = list;
        this.mlistener=m;
    }

    private ArrayList<PopulartModel> list;
    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.popular_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        String imageUrl =list.get(position).getImg(); // Assuming 'position' is the index of the image in the ArrayList
        Glide.with(context)
                .load(imageUrl)
                .into(holder.popImg);
//        Glide.with(context).load(list.get(position).getImg()).into(holder.popImg);
        holder.name.setText(list.get(position).getName());
        holder.des.setText(list.get(position).getDescription());
        holder.ratingtext.setText(list.get(position).getRating());
        holder.rat.setRating(Float.parseFloat(list.get(position).getRating()));
        holder.countcolor.setText(list.get(position).getCount_color()+" color");
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onItemClick(position);
//                infoItemView a=new infoItemView();
                String idsp=list.get(position).getId().toString().trim();
                Intent intent=new Intent(v.getContext(),infoItemView.class);
//                String selectedItemId = list.get(position).getId().toString().trim();
//                intent.putExtra("spid",idsp);
                // Tạo Intent để mở một Activity mới (infoItemView)
//                Intent intent = new Intent(context, infoItemView.class);

                // Truyền ID của item đến Activity mới
                intent.putExtra("selectedItemId", idsp);
//                Toast.makeText(v.getContext(), idsp, Toast.LENGTH_SHORT).show();
                // Bắt đầu Activity mới
                v.getContext().startActivity(intent);
            }
        });
//        try {
//            float ratingValue = Float.parseFloat(list.get(position).getRating());
//            holder.rat.setRating(ratingValue);
//        } catch (NumberFormatException e) {
//            // Handle the case where the string cannot be parsed as a float
//            e.printStackTrace(); // or log an error message
//        }
//        holder.popImg.set
//        holder.rat.setRating(Float.parseFloat(list.get(position).getRating().toString().trim()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView popImg;
        TextView name, des,ratingtext,countcolor;
        LinearLayout ln;
        RatingBar rat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.pop_name);
            popImg=itemView.findViewById(R.id.pop_img);
            des=itemView.findViewById(R.id.pop_des);
            ratingtext=itemView.findViewById(R.id.pop_ratText);
            rat=itemView.findViewById(R.id.pop_rat);
            countcolor=itemView.findViewById(R.id.pop_countcolor);
            ln=itemView.findViewById(R.id.spID);
        }
    }
}

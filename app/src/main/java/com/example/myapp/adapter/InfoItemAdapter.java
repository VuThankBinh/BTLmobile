package com.example.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.Models.InFoItemModel;
import com.example.myapp.Models.InfoItem_colorModel;
import com.example.myapp.Models.PopulartModel;
import com.example.myapp.R;

import java.util.ArrayList;

public class InfoItemAdapter extends RecyclerView.Adapter<InfoItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<InFoItemModel> list;
    public InfoItemAdapter(Context context, ArrayList<InFoItemModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public InfoItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.info_item_nav,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoItemAdapter.ViewHolder holder, int position) {
        String imageUrl =list.get(position).getImgD(); // Assuming 'position' is the index of the image in the ArrayList
        Glide.with(context)
                .load(imageUrl)
                .into(holder.img);
//        Glide.with(context).load(list.get(position).getImg()).into(holder.popImg);
        holder.name.setText(list.get(position).getName());
        holder.giaC.setText(list.get(position).getGiaC());
        holder.giaM.setText(list.get(position).getGiaM());
        holder.ratingtext.setText(list.get(position).getRating());
        holder.rat.setRating(Float.parseFloat(list.get(position).getRating()));
        holder.countcolor.setText(list.get(position).getCount_color()+" color");
        holder.color.setHasFixedSize(true);
//        poprec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//        GridLayoutManager layoutManager;
//        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
//        holder.color.setLayoutManager(layoutManager);
//        holder.color.setAdapter();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, giaC,giaM,ratingtext,countcolor;

        RatingBar rat;

        RecyclerView color,size;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.infoItemName);
            giaC=itemView.findViewById(R.id.infoItemGiaC);
            giaM=itemView.findViewById(R.id.infoItemGiaM);
            ratingtext=itemView.findViewById(R.id.infoItem_ratText);
            rat=itemView.findViewById(R.id.infoItem_rat);
            countcolor=itemView.findViewById(R.id.infoItemcountcolor);
            img=itemView.findViewById(R.id.infoItemImg);
            color=itemView.findViewById(R.id.colorbt);
            size=itemView.findViewById(R.id.sizebt);
        }
    }
}
class InfoItem_colorAdapter extends RecyclerView.Adapter<InfoItem_colorAdapter.ViewHolder>{

    @NonNull
    @Override
    public InfoItem_colorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull InfoItem_colorAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

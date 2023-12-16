package com.example.myapp.oder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.Models.oderProduct;
import com.example.myapp.R;

import java.util.ArrayList;

public class oderAdapter extends RecyclerView.Adapter<oderAdapter.ViewHoler> {
    private Context context;
    private ArrayList<oderProduct> list;

    public oderAdapter(Context context, ArrayList<oderProduct> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public oderAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.oder_item,parent,false);
        return new ViewHoler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull oderAdapter.ViewHoler holder, int position) {
        String imageUrl =list.get(position).getImg(); // Assuming 'position' is the index of the image in the ArrayList
        Glide.with(context)
                .load(imageUrl)
                .into(holder.img);
        holder.name.setText(list.get(position).getName());
        holder.color.setText(list.get(position).getColor());
        holder.size.setText(list.get(position).getSize());
        holder.price.setText(list.get(position).getPrice());
        holder.quan.setText(list.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,color,size,price,quan;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgProductOder);
            name=itemView.findViewById(R.id.nameProductOder);
            color=itemView.findViewById(R.id.colorProductOder);
            size=itemView.findViewById(R.id.sizeProductOder);
            price=itemView.findViewById(R.id.priceProductOder);
            quan=itemView.findViewById(R.id.quanSLProductOder);
        }
    }
}

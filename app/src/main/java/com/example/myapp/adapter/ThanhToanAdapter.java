package com.example.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.R;
import com.example.myapp.cart.CartItemProduct;

import java.util.ArrayList;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.ViewHoler>{
    private Context context;
    private ArrayList<CartItemProduct> list;

    public ThanhToanAdapter(Context context, ArrayList<CartItemProduct> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ThanhToanAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cartitem,parent,false);

        return new ViewHoler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhToanAdapter.ViewHoler holder, int position) {
        String imageUrl =list.get(position).getImgD(); // Assuming 'position' is the index of the image in the ArrayList
        Glide.with(context)
                .load(imageUrl)
                .into(holder.img);

        holder.name.setText(list.get(position).getName());
        holder.color.setText(list.get(position).getColor());
        holder.size.setText(list.get(position).getSize());
        holder.price.setText(list.get(position).getPrice());
        holder.quan.setText(list.get(position).getQuantity());
        holder.cong.setVisibility(View.GONE);
        holder.tru.setVisibility(View.GONE);
        holder.quan.setVisibility(View.GONE);
        holder.quanText.setText(list.get(position).getQuantity());
        holder.chk.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
//        ImageView img;
//        TextView name;
//        TextView size;
//        TextView color;
//        TextView quan;
//        TextView price;
TextView chk;
        ImageView img;
        TextView name,color,size,price,quan,cong,tru,quanText;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
//            name=itemView.findViewById(R.id.imgThanhToan);
//            size=itemView.findViewById(R.id.kichthuocText);
//            color=itemView.findViewById(R.id.mausacText);
//            quan=itemView.findViewById(R.id.soluongText);
//            price=itemView.findViewById(R.id.giasp);
//            name=itemView.findViewById(R.id.tensp);
            chk=itemView.findViewById(R.id.checkbuy);
            img=itemView.findViewById(R.id.imgProductCart);
            name=itemView.findViewById(R.id.nameProductCart);
            color=itemView.findViewById(R.id.colorProductCart);
            size=itemView.findViewById(R.id.sizeProductCart);
            price=itemView.findViewById(R.id.priceProductCart);
            quan=itemView.findViewById(R.id.quantityProductCart);
            cong=itemView.findViewById(R.id.congquancart);
            tru=itemView.findViewById(R.id.truquancart);
            quanText=itemView.findViewById(R.id.quanSLProductCart);
        }
    }
}

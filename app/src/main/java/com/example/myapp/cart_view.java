package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.adapter.CartitemAdapter;
import com.example.myapp.adapter.updaterThongTinCart;
import com.example.myapp.cart.CartItemProduct;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class cart_view extends AppCompatActivity  implements updaterThongTinCart {

    int countItemCart=0;
    TextView countItem,tv;
    double tong=0;
    FirebaseFirestore db;
    CartitemAdapter adapter;
    ArrayList<CartItemProduct> arr;
    RecyclerView cart;
    Button ThanhToanBtn;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        countItem=findViewById(R.id.countItem);
        cart=findViewById(R.id.recyclerviewCart);
        cart.setHasFixedSize(true);
        arr=new ArrayList<CartItemProduct>();
        adapter=new CartitemAdapter(cart_view.this,arr);
        cart.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter.setOnDataChangeListener(this);
        cart.setAdapter(adapter);
        back=findViewById(R.id.backhome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cart_view.this,TrangChu.class));
            }
        });
        db= FirebaseFirestore.getInstance();

        ThanhToanBtn=findViewById(R.id.ThanhToanBtn);
        tv=findViewById(R.id.khongsp11);
        showCart();
        ThanhToanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cart_view.this,ThanhToan.class));
            }
        });
    }

    private void showCart() {

        if(MainActivity.loginWithAcc==true){
            Query query = db.collection("AddToCart")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("CurrentUser");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document !=null){
                                String id=document.getId().toString();
                                String idsp=document.getData().get("ProductId").toString();
                                String name= document.getData().get("PrductName").toString();
                                String imgD= document.getData().get("ProductImg").toString();
                                String price= document.getData().get("ProductPrice").toString();
                                String quantity= document.getData().get("ProductQuantity").toString();
                                String sz= document.getData().get("ProductSize").toString();
                                String color= document.getData().get("ProductColor").toString();
                                countItemCart+=Integer.parseInt(quantity);
                                tong+= Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
//                            System.out.println(name+"color: "+ color +"size: "+sz);
                                CartItemProduct c=new CartItemProduct(id, name, imgD, color, sz, price, quantity,idsp);
                                arr.add(c);
                                System.out.println(countItemCart);
                                System.out.println(tong);
                                adapter.notifyDataSetChanged();
                                countItem.setText(countItemCart +" item | "+ tong +"$");
                                System.out.println("size mảng: "+countItemCart + ",  "+arr.size());
                                if(arr.size()==0){
                                    ThanhToanBtn.setVisibility(View.GONE);
                                    tv.setVisibility(View.VISIBLE);
                                }
                                else {
                                    ThanhToanBtn.setVisibility(View.VISIBLE);
                                    tv.setVisibility(View.GONE);
                                }
                            }

                        }
                    }
                }
            });
        }
        if(MainActivity.loginWithGG==true){
            System.out.println(MainActivity.auth.getUid()+"acrrt");
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
            Query query = db.collection("AddToCart")
                    .document(account.getId())
                    .collection("CurrentUser");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document !=null){
                                String id=document.getId().toString();
                                String idsp=document.getData().get("ProductId").toString();
                                String name= document.getData().get("PrductName").toString();
                                String imgD= document.getData().get("ProductImg").toString();
                                String price= document.getData().get("ProductPrice").toString();
                                String quantity= document.getData().get("ProductQuantity").toString();
                                String sz= document.getData().get("ProductSize").toString();
                                String color= document.getData().get("ProductColor").toString();
                                countItemCart+=Integer.parseInt(quantity);
                                tong+= Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
//                            System.out.println(name+"color: "+ color +"size: "+sz);
                                CartItemProduct c=new CartItemProduct(id, name, imgD, color, sz, price, quantity,idsp);
                                arr.add(c);
                                adapter.notifyDataSetChanged();
                                countItem.setText(countItemCart +" item | "+ tong +"$");
                                System.out.println("size mảng: "+countItemCart + ",  "+arr.size());
                                if(arr.size()==0){
                                    ThanhToanBtn.setVisibility(View.GONE);
                                    tv.setVisibility(View.VISIBLE);
                                }
                                else {
                                    ThanhToanBtn.setVisibility(View.VISIBLE);
                                    tv.setVisibility(View.GONE);
                                }
                            }

                        }
                    }
                }
            });
        }

//

    }

    @Override
    public void onDataChanged(int newSize, int tong) {
        TextView tv=findViewById(R.id.countItem);
        tv.setText(newSize +" item | "+tong+"$" );
        if(newSize==0){
            ThanhToanBtn.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        }
        else {
            ThanhToanBtn.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }
    }
}
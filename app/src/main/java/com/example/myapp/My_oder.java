package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Models.oderProduct;
import com.example.myapp.oder.oderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class My_oder extends AppCompatActivity {

    RecyclerView rec;
    oderAdapter adapter;
    ArrayList<oderProduct> list;
    FirebaseFirestore db;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_oder);
        rec=findViewById(R.id.recyclerviewCart2);
        list=new ArrayList<oderProduct>();
        adapter=new oderAdapter(My_oder.this,list);
        rec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rec.setAdapter(adapter);
        back=findViewById(R.id.backhome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(My_oder.this,TrangChu.class));
            }
        });
        db=FirebaseFirestore.getInstance();
        showOder();
    }

    private void showOder() {
        if(MainActivity.loginWithAcc==true){
            CollectionReference query = db.collection("AddToOder")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("CurrentUser");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document != null) {
//                                String id=document.getId().toString();
//                                String idsp=document.getData().get("ProductId").toString();
//                                String name= document.getData().get("PrductName").toString();
//                                String imgD= document.getData().get("ProductImg").toString();
//                                String price= document.getData().get("ProductPrice").toString();
//                                String quantity= document.getData().get("ProductQuantity").toString();
//                                String sz= document.getData().get("ProductSize").toString();
//                                String color= document.getData().get("ProductColor").toString();
                                List<Map<String, Object>> yourArray = (List<Map<String, Object>>) document.get("OrderDetails");

                                // Kiểm tra xem mảng có tồn tại và không rỗng
                                if (yourArray != null && !yourArray.isEmpty()) {
                                    // Lặp qua mỗi map trong mảng
                                    for (Map<String, Object> map : yourArray) {
                                        // Xử lý dữ liệu từ map, ví dụ:
                                        String name = (String) map.get("PrductName");
                                        String img = (String) map.get("ProductImg");
                                        String quan = (String) map.get("ProductQuantity");
                                        String price = (String) map.get("ProductPrice");
                                        String color = (String) map.get("ProductColor");
                                        String size = (String) map.get("ProductSize");
                                        oderProduct product=new oderProduct(name,img,price,quan,size,color);
                                        list.add(product);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            });

        }
        if(MainActivity.loginWithGG==true){
            CollectionReference query = db.collection("AddToOder")
                    .document(MainActivity.auth.getUid())
                    .collection("CurrentUser");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document != null) {

                                List<Map<String, Object>> yourArray = (List<Map<String, Object>>) document.get("OrderDetails");

                                // Kiểm tra xem mảng có tồn tại và không rỗng
                                if (yourArray != null && !yourArray.isEmpty()) {
                                    // Lặp qua mỗi map trong mảng
                                    for (Map<String, Object> map : yourArray) {
                                        // Xử lý dữ liệu từ map, ví dụ:
                                        String name = (String) map.get("PrductName");
                                        String img = (String) map.get("ProductImg");
                                        String quan = (String) map.get("ProductQuantity");
                                        String price = (String) map.get("ProductPrice");
                                        String color = (String) map.get("ProductColor");
                                        String size = (String) map.get("ProductSize");
                                        oderProduct product=new oderProduct(name,img,price,quan,size,color);
                                        list.add(product);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
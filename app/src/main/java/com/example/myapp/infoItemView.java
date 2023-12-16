package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Models.InfoItem_colorModel;
import com.example.myapp.Models.PopulartModel;
import com.example.myapp.Models.Sizee;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.myapp.Models.InFoItemModel;
import com.example.myapp.adapter.InfoItemAdapter;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class infoItemView extends AppCompatActivity implements RecyclerViewClickListener, RecyclerViewSizeClickListener {
    String idsp = "";
    Boolean chonSize=false;
    String colorAdd="";
    String sizeAdd="";
    String imgAdd="";
    String nameAdd="";
    String priceAdd="";
    int countAdd=0;
    int quan=0;
    LinearLayout quantityty;
    Button addtocart;
    ImageView backHome;
    FirebaseFirestore db;
    RecyclerView re;
    InfoItemAdapter adapter;
    ArrayList<InFoItemModel> modelss;
    Intent intent;
    ImageView img;
    TextView name, giaC, giaM, ratingtext, countcolor;
    TextView siezzz;
    Button cong,tru;
    EditText countaddcart;
    RatingBar rat;

    RecyclerView color, size;
    // Lấy Intent từ Activity gọi
//    Intent intent = getIntent();
//
//    // Lấy giá trị của selectedItemId từ Intent
//    String selectedItemId = intent.getStringExtra("selectedItemId");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_item_view);
        intent = getIntent();

        // Kiểm tra xem Intent có chứa key "selectedItemId" không
        if (intent.hasExtra("selectedItemId")) {
            // Lấy giá trị của selectedItemId từ Intent
            String selectedItemId = intent.getStringExtra("selectedItemId");

            idsp = selectedItemId;
        }
//        Toast.makeText(this, idsp, Toast.LENGTH_SHORT).show();
        backHome = findViewById(R.id.backhome);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(infoItemView.this, TrangChu.class));
            }
        });

        db = FirebaseFirestore.getInstance();
        re = findViewById(R.id.infosp);
        modelss = new ArrayList<InFoItemModel>();
        adapter = new InfoItemAdapter(infoItemView.this, modelss);
        re.setAdapter(adapter);
        showInfo();
        countaddcart=findViewById(R.id.countaddCart);
        countAdd=Integer.parseInt(countaddcart.getHint().toString().trim());
        cong=findViewById(R.id.congcart);
        cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(countaddcart.getText().toString().trim())){
                    System.out.println(countaddcart.getHint().toString().trim());
                    int value=Integer.parseInt(countaddcart.getHint().toString().trim());
                    value++;
                    System.out.println("sau cộng: "+value);
                    System.out.println("có: "+quan);
                    if(value>quan){
                        Toast.makeText(infoItemView.this, "Không có đủ hàng", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        countaddcart.setText(String.valueOf(value));
                        System.out.println(value);
                        countAdd=value;
                    }
                }
                else {
//                    System.out.println(countaddcart.getHint().toString().trim());
                    int value=Integer.parseInt(countaddcart.getText().toString().trim());
                    value++;
                    System.out.println("sau trừ: "+value);
                    System.out.println("có: "+quan);
                    if(value>quan){
                        Toast.makeText(infoItemView.this, "Không đủ hàng", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        countaddcart.setText(String.valueOf(value));
                        countAdd=value;
                        System.out.println(value);
                    }
                }
            }
        });
        tru=findViewById(R.id.trucart);
        tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(countaddcart.getText().toString().trim())){
                    System.out.println(countaddcart.getHint().toString().trim());
                    int value=Integer.parseInt(countaddcart.getHint().toString().trim());
                    value--;
                    System.out.println("sau trừ: "+value);
                    System.out.println("có: "+quan);
                    if(value<=0){
                        Toast.makeText(infoItemView.this, "Tối thiểu 1 sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        countaddcart.setText(String.valueOf(value));
                        countAdd=value;
                        System.out.println(value);
                    }
                }
                else {
//                    System.out.println(countaddcart.getHint().toString().trim());
                    int value=Integer.parseInt(countaddcart.getText().toString().trim());
                    value--;
                    System.out.println("sau trừ: "+value);
                    System.out.println("có: "+quan);
                    if(value<=0){
                        Toast.makeText(infoItemView.this, "Tối thiểu 1 sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        countaddcart.setText(String.valueOf(value));
                        countAdd=value;
                        System.out.println(value);
                    }
                }

            }
        });

        addtocart=findViewById(R.id.addtocart);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    private void addToCart() {
        String saveDate,saveTime;
        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        saveDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveTime=currentTime.format(calendar.getTime());

        HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("ProductId",idsp);
        cartMap.put("ProductImg",imgAdd);
        cartMap.put("PrductName",nameAdd);
        cartMap.put("ProductColor",colorAdd);
        cartMap.put("ProductSize",sizeAdd);
        cartMap.put("ProductPrice",priceAdd);
        cartMap.put("ProductQuantity",countAdd);
        cartMap.put("currentDate",saveDate);
        cartMap.put("currentTime",saveTime);
        db=FirebaseFirestore.getInstance();
        if(MainActivity.loginWithAcc==true){

            System.out.println("id: "+idsp+"\ncolor: "+colorAdd+"\nsize: "+sizeAdd);
            Query query = db.collection("AddToCart")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("CurrentUser")
                    .whereEqualTo("ProductId",idsp).whereEqualTo("ProductColor",colorAdd).whereEqualTo("ProductSize",sizeAdd)
                    .limit(1);


            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Lấy bản ghi đầu tiên
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                            // Log để kiểm tra giá trị của ProductQuantity trước khi cập nhật
                            Log.d("Debug", "ProductQuantity before update: " + document.getLong("ProductQuantity"));

                            // Sản phẩm đã có trong giỏ hàng, thực hiện cập nhật ProductQuantity
                            int currentQuantity = document.getLong("ProductQuantity").intValue();
                            int newQuantity = currentQuantity + countAdd;
                            if(newQuantity<=quan){
                                Log.d("Tag", "New ProductQuantity: " + newQuantity);

                                // Cập nhật ProductQuantity trong cùng một bản ghi
                                document.getReference().update("ProductQuantity", newQuantity)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(infoItemView.this, "Update Cart Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            else {
                                Toast.makeText(infoItemView.this, "Sản phẩm đã có trong giỏ nếu thêm thì hàng không đủ", Toast.LENGTH_SHORT).show();
                            }

                            // Log để kiểm tra giá trị mới của ProductQuantity

                        } else {
                            // Sản phẩm chưa có trong giỏ hàng, thêm mới
                            db.collection("AddToCart")
                                    .document(MainActivity.auth.getCurrentUser().getUid())
                                    .collection("CurrentUser")
                                    .add(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            Toast.makeText(infoItemView.this, "Add to Cart Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Xử lý khi có lỗi trong quá trình kiểm tra giỏ hàng
                        Toast.makeText(infoItemView.this, "Error checking Cart", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
        if(MainActivity.loginWithGG==true) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//            db.collection("AddToCart")
//                    .document(account.getId())
//                    .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            Toast.makeText(infoItemView.this, "Add to Cart Successful", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
            System.out.println("id: "+idsp+"\ncolor: "+colorAdd+"\nsize: "+sizeAdd);
            System.out.println(account.getId().toString());
            Query query = db.collection("AddToCart")
                    .document(account.getId())
                    .collection("CurrentUser")
                    .whereEqualTo("ProductId",idsp).whereEqualTo("ProductColor",colorAdd).whereEqualTo("ProductSize",sizeAdd)
                    .limit(1);


            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Lấy bản ghi đầu tiên
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                            // Log để kiểm tra giá trị của ProductQuantity trước khi cập nhật
                            Log.d("Debug", "ProductQuantity before update: " + document.getLong("ProductQuantity"));

                            // Sản phẩm đã có trong giỏ hàng, thực hiện cập nhật ProductQuantity
                            int currentQuantity = document.getLong("ProductQuantity").intValue();
                            int newQuantity = currentQuantity + countAdd;
                            if(newQuantity<=quan){
                                Log.d("Tag", "New ProductQuantity: " + newQuantity);

                                // Cập nhật ProductQuantity trong cùng một bản ghi
                                document.getReference().update("ProductQuantity", newQuantity)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(infoItemView.this, "Update Cart Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            else {
                                Toast.makeText(infoItemView.this, "Sản phẩm đã có trong giỏ nếu thêm thì hàng không đủ", Toast.LENGTH_SHORT).show();
                            }

                            // Log để kiểm tra giá trị mới của ProductQuantity

                        } else {
                            // Sản phẩm chưa có trong giỏ hàng, thêm mới
                            db.collection("AddToCart")
                                    .document(account.getId())
                                    .collection("CurrentUser")
                                    .add(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            Toast.makeText(infoItemView.this, "Add to Cart Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Xử lý khi có lỗi trong quá trình kiểm tra giỏ hàng
                        Toast.makeText(infoItemView.this, "Error checking Cart", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    ArrayList<InfoItem_colorModel> colorModels = new ArrayList<InfoItem_colorModel>();
    InFoItemModel model = new InFoItemModel();
    //        InfoItem_colorModel colorModel=new InfoItem_colorModel();


    public void  showInfoGiay() {
        db.collection("giay")
                .document(idsp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document
                                String name = document.getString("name");
                                String giaM = document.getString("priceM");
                                String giaC = document.getString("priceC"); // Make sure to use the correct field name
                                String rating = document.getString("rating");
                                String type = document.getString("loai");
                                String img = document.getString("imgD");

                                String countColor = "";
                                if (document.contains("color")) {
                                    // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                    List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                    // Kiểm tra xem colorList có giá trị và không rỗng không
                                    if (colorList != null && !colorList.isEmpty()) {
                                        // Lấy độ dài của mảng colorList
                                        int arrayLength = colorList.size();

                                        // Sử dụng arrayLength theo nhu cầu của bạn
//                                        System.out.println("Độ dài của mảng color: " + arrayLength);
                                        countColor = String.valueOf(arrayLength);
                                    } else {
                                        // Xử lý khi mảng colorList là null hoặc rỗng
                                        System.out.println("Mảng color là null hoặc rỗng");
                                    }
                                } else {
                                    // Xử lý khi trường "color" không tồn tại trong document
                                    System.out.println("Trường 'color' không tồn tại trong document");
                                }

                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {
                                                String imgdd = colorMapDes.get("colorDes").toString();
                                                String imgV = colorMapDes.get("img").toString();
                                                System.out.println("Mau:" + imgdd);
                                                modelss.setColorDes(imgdd);
                                                modelss.setImg(imgV);
                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
                                                        sizee.setSizeDes(sizeDesValue);
                                                        sizee.setQuantity(quantityValue);
                                                        sz.add(sizee);
                                                        System.out.println("Size2:" + sizeDesValue + " quantity2: " + quantityValue);
                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                modelss.setSize(sz);
                                            }

                                            colorModels.add(modelss);
                                            model.setModels(colorModels);
                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data

                                model.setName(name);
                                model.setGiaM(giaM);
                                model.setGiaC(giaC);
                                model.setLoai(type);
                                model.setRating(rating);
                                model.setImgD(img);
                                model.setCount_color(countColor);
//                        Toast.makeText(infoItemView.this, name, Toast.LENGTH_SHORT).show();
                                // Add the model to your list or perform other operations
                                modelss.add(model);
                                loadsp(model);
                                // Notify adapter if needed
                                adapter.notifyDataSetChanged();
                                Log.d("TAG", "Object information: " + modelss.toString());
//                        System.out.println("Object information: " +toString());
                                System.out.println("count color:"+model.getModels().size());
                                for (int ii = 0; ii < model.getModels().size(); ii++) {
//            Toast.makeText(infoItemView.this, model.getModels().size(), Toast.LENGTH_SHORT).show();
                                    System.out.println("color:"+model.getModels().get(ii).getColorDes());

                                    Toast.makeText(infoItemView.this, "Color: " + model.getModels().get(ii).getColorDes(), Toast.LENGTH_SHORT).show();

                                    for (int j = 0; j < model.getModels().get(ii).getSize().size(); j++) {
//                Toast.makeText(infoItemView.this, "Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes(), Toast.LENGTH_SHORT).show();
                                        System.out.println("Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes()+" quantity: "+model.getModels().get(ii).getSize().get(j).getQuantity());
                                    }
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    }
                });
//        Toast.makeText(infoItemView.this,model.getModels().size() , Toast.LENGTH_SHORT).show();

    }


    public void showInfo() {

        String specificDocumentId = "r6rB5cp7MQKfhYQ6nTRg"; // Replace with the actual document ID

        db.collection("giay")
                .document(idsp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document
                                String name = document.getString("name");
                                String giaM = document.getString("priceM");
                                String giaC = document.getString("priceC"); // Make sure to use the correct field name
                                String rating = document.getString("rating");
                                String type = document.getString("loai");
                                String img = document.getString("imgD");
                                priceAdd=giaM;
                                nameAdd=name;
                                String countColor = "";
                                if (document.contains("color")) {
                                    // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                    List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                    // Kiểm tra xem colorList có giá trị và không rỗng không
                                    if (colorList != null && !colorList.isEmpty()) {
                                        // Lấy độ dài của mảng colorList
                                        int arrayLength = colorList.size();

                                        // Sử dụng arrayLength theo nhu cầu của bạn
//                                        System.out.println("Độ dài của mảng color: " + arrayLength);
                                        countColor = String.valueOf(arrayLength);
                                    } else {
                                        // Xử lý khi mảng colorList là null hoặc rỗng
                                        System.out.println("Mảng color là null hoặc rỗng");
                                    }
                                } else {
                                    // Xử lý khi trường "color" không tồn tại trong document
                                    System.out.println("Trường 'color' không tồn tại trong document");
                                }

                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {
                                                String imgdd = colorMapDes.get("colorDes").toString();
                                                String imgV = colorMapDes.get("img").toString();
//                                                Toast.makeText(infoItemView.this, "color: " +imgdd, Toast.LENGTH_SHORT).show();
                                                System.out.println("Mau:" + imgdd);
                                                modelss.setColorDes(imgdd);
                                                modelss.setImg(imgV);
                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
                                                        sizee.setSizeDes(sizeDesValue);
                                                        sizee.setQuantity(quantityValue);
                                                        sz.add(sizee);
                                                        System.out.println("Size2:" + sizeDesValue + " quantity2: " + quantityValue);
                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                modelss.setSize(sz);
                                            }

                                            colorModels.add(modelss);
                                            model.setModels(colorModels);
                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data

                                model.setName(name);
                                model.setGiaM(giaM);
                                model.setGiaC(giaC);
                                model.setLoai(type);
                                model.setRating(rating);
                                model.setImgD(img);
                                model.setCount_color(countColor);
//                        Toast.makeText(infoItemView.this, name, Toast.LENGTH_SHORT).show();
                                // Add the model to your list or perform other operations
                                modelss.add(model);
                                loadsp(model);
                                // Notify adapter if needed
                                adapter.notifyDataSetChanged();
                                Log.d("TAG", "Object information: " + modelss.toString());
//                        System.out.println("Object information: " +toString());
                                System.out.println("count color:"+model.getModels().size());
                                for (int ii = 0; ii < model.getModels().size(); ii++) {
//            Toast.makeText(infoItemView.this, model.getModels().size(), Toast.LENGTH_SHORT).show();
                                    System.out.println("color:"+model.getModels().get(ii).getColorDes());

//                                    Toast.makeText(infoItemView.this, "Color: " + model.getModels().get(ii).getColorDes(), Toast.LENGTH_SHORT).show();

                                    for (int j = 0; j < model.getModels().get(ii).getSize().size(); j++) {
//                Toast.makeText(infoItemView.this, "Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes(), Toast.LENGTH_SHORT).show();
                                        System.out.println("Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes()+" quantity: "+model.getModels().get(ii).getSize().get(j).getQuantity());
                                    }
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    }
                });
        db.collection("QuanAo")
                .document(idsp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document
                                String name = document.getString("name");
                                String giaM = document.getString("priceM");
                                String giaC = document.getString("priceC"); // Make sure to use the correct field name
                                String rating = document.getString("rating");
                                String type = document.getString("loai");
                                String img = document.getString("imgD");
                                priceAdd=giaM;
                                nameAdd=name;
                                String countColor = "";
                                if (document.contains("color")) {
                                    // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                    List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                    // Kiểm tra xem colorList có giá trị và không rỗng không
                                    if (colorList != null && !colorList.isEmpty()) {
                                        // Lấy độ dài của mảng colorList
                                        int arrayLength = colorList.size();

                                        // Sử dụng arrayLength theo nhu cầu của bạn
//                                        System.out.println("Độ dài của mảng color: " + arrayLength);
                                        countColor = String.valueOf(arrayLength);
                                    } else {
                                        // Xử lý khi mảng colorList là null hoặc rỗng
                                        System.out.println("Mảng color là null hoặc rỗng");
                                    }
                                } else {
                                    // Xử lý khi trường "color" không tồn tại trong document
                                    System.out.println("Trường 'color' không tồn tại trong document");
                                }

                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {
                                                String imgdd = colorMapDes.get("colorDes").toString();
                                                String imgV = colorMapDes.get("img").toString();
//                                                Toast.makeText(infoItemView.this, "color: " +imgdd, Toast.LENGTH_SHORT).show();
                                                System.out.println("Mau:" + imgdd);
                                                modelss.setColorDes(imgdd);
                                                modelss.setImg(imgV);
                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
                                                        sizee.setSizeDes(sizeDesValue);
                                                        sizee.setQuantity(quantityValue);
                                                        sz.add(sizee);
                                                        System.out.println("Size2:" + sizeDesValue + " quantity2: " + quantityValue);
                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                modelss.setSize(sz);
                                            }

                                            colorModels.add(modelss);
                                            model.setModels(colorModels);
                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data

                                model.setName(name);
                                model.setGiaM(giaM);
                                model.setGiaC(giaC);
                                model.setLoai(type);
                                model.setRating(rating);
                                model.setImgD(img);
                                model.setCount_color(countColor);
//                        Toast.makeText(infoItemView.this, name, Toast.LENGTH_SHORT).show();
                                // Add the model to your list or perform other operations
                                modelss.add(model);
                                loadsp(model);
                                // Notify adapter if needed
                                adapter.notifyDataSetChanged();
                                Log.d("TAG", "Object information: " + modelss.toString());
//                        System.out.println("Object information: " +toString());
                                System.out.println("count color:"+model.getModels().size());
                                for (int ii = 0; ii < model.getModels().size(); ii++) {
//            Toast.makeText(infoItemView.this, model.getModels().size(), Toast.LENGTH_SHORT).show();
                                    System.out.println("color:"+model.getModels().get(ii).getColorDes());

//                                    Toast.makeText(infoItemView.this, "Color: " + model.getModels().get(ii).getColorDes(), Toast.LENGTH_SHORT).show();

                                    for (int j = 0; j < model.getModels().get(ii).getSize().size(); j++) {
//                Toast.makeText(infoItemView.this, "Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes(), Toast.LENGTH_SHORT).show();
                                        System.out.println("Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes()+" quantity: "+model.getModels().get(ii).getSize().get(j).getQuantity());
                                    }
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    }
                });
        db.collection("ball")
                .document(idsp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document
                                String name = document.getString("name");
                                String giaM = document.getString("priceM");
                                String giaC = document.getString("priceC"); // Make sure to use the correct field name
                                String rating = document.getString("rating");
                                String type = document.getString("loai");
                                String img = document.getString("imgD");
                                priceAdd=giaM;
                                nameAdd=name;
                                String countColor = "";
                                if (document.contains("color")) {
                                    // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                    List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                    // Kiểm tra xem colorList có giá trị và không rỗng không
                                    if (colorList != null && !colorList.isEmpty()) {
                                        // Lấy độ dài của mảng colorList
                                        int arrayLength = colorList.size();

                                        // Sử dụng arrayLength theo nhu cầu của bạn
//                                        System.out.println("Độ dài của mảng color: " + arrayLength);
                                        countColor = String.valueOf(arrayLength);
                                    } else {
                                        // Xử lý khi mảng colorList là null hoặc rỗng
                                        System.out.println("Mảng color là null hoặc rỗng");
                                    }
                                } else {
                                    // Xử lý khi trường "color" không tồn tại trong document
                                    System.out.println("Trường 'color' không tồn tại trong document");
                                }

                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {
                                                String imgdd = colorMapDes.get("colorDes").toString();
                                                String imgV = colorMapDes.get("img").toString();
//                                                Toast.makeText(infoItemView.this, "color: " +imgdd, Toast.LENGTH_SHORT).show();
                                                System.out.println("Mau:" + imgdd);
                                                modelss.setColorDes(imgdd);
                                                modelss.setImg(imgV);
                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
                                                        sizee.setSizeDes(sizeDesValue);
                                                        sizee.setQuantity(quantityValue);
                                                        sz.add(sizee);
                                                        System.out.println("Size2:" + sizeDesValue + " quantity2: " + quantityValue);
                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                modelss.setSize(sz);
                                            }

                                            colorModels.add(modelss);
                                            model.setModels(colorModels);
                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data

                                model.setName(name);
                                model.setGiaM(giaM);
                                model.setGiaC(giaC);
                                model.setLoai(type);
                                model.setRating(rating);
                                model.setImgD(img);
                                model.setCount_color(countColor);
//                        Toast.makeText(infoItemView.this, name, Toast.LENGTH_SHORT).show();
                                // Add the model to your list or perform other operations
                                modelss.add(model);
                                loadsp(model);
                                // Notify adapter if needed
                                adapter.notifyDataSetChanged();
                                Log.d("TAG", "Object information: " + modelss.toString());
//                        System.out.println("Object information: " +toString());
                                System.out.println("count color:"+model.getModels().size());
                                for (int ii = 0; ii < model.getModels().size(); ii++) {
//            Toast.makeText(infoItemView.this, model.getModels().size(), Toast.LENGTH_SHORT).show();
                                    System.out.println("color:"+model.getModels().get(ii).getColorDes());


                                    for (int j = 0; j < model.getModels().get(ii).getSize().size(); j++) {
//                Toast.makeText(infoItemView.this, "Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes(), Toast.LENGTH_SHORT).show();
                                        System.out.println("Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes()+" quantity: "+model.getModels().get(ii).getSize().get(j).getQuantity());
                                    }
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    }
                });
        db.collection("baoho")
                .document(idsp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data from the document
                                String name = document.getString("name");
                                String giaM = document.getString("priceM");
                                String giaC = document.getString("priceC"); // Make sure to use the correct field name
                                String rating = document.getString("rating");
                                String type = document.getString("loai");
                                String img = document.getString("imgD");
                                priceAdd=giaM;
                                nameAdd=name;
                                String countColor = "";
                                if (document.contains("color")) {
                                    // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                    List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                    // Kiểm tra xem colorList có giá trị và không rỗng không
                                    if (colorList != null && !colorList.isEmpty()) {
                                        // Lấy độ dài của mảng colorList
                                        int arrayLength = colorList.size();

                                        // Sử dụng arrayLength theo nhu cầu của bạn
//                                        System.out.println("Độ dài của mảng color: " + arrayLength);
                                        countColor = String.valueOf(arrayLength);
                                    } else {
                                        // Xử lý khi mảng colorList là null hoặc rỗng
                                        System.out.println("Mảng color là null hoặc rỗng");
                                    }
                                } else {
                                    // Xử lý khi trường "color" không tồn tại trong document
                                    System.out.println("Trường 'color' không tồn tại trong document");
                                }

                                Map<String, Object> data = document.getData();

                                // Access the array field
                                if (data != null && data.containsKey("color")) {
                                    List<Object> colorArray = (List<Object>) data.get("color");

                                    // Iterate through each item in the "color" array
                                    for (Object colorItem : colorArray) {
                                        // Check if the color item has a "size" array
                                        InfoItem_colorModel modelss = new InfoItem_colorModel();
                                        ArrayList<Sizee> sz = new ArrayList<Sizee>();
                                        if (colorItem instanceof Map) {

                                            Map<String, Object> colorMapDes = (Map<String, Object>) colorItem;
                                            if (colorMapDes.containsKey("colorDes")) {
                                                String imgdd = colorMapDes.get("colorDes").toString();
                                                String imgV = colorMapDes.get("img").toString();
//                                                Toast.makeText(infoItemView.this, "color: " +imgdd, Toast.LENGTH_SHORT).show();
                                                System.out.println("Mau:" + imgdd);
                                                modelss.setColorDes(imgdd);
                                                modelss.setImg(imgV);
                                            }
                                            Map<String, Object> colorMap = (Map<String, Object>) colorItem;

                                            if (colorMap.containsKey("size")) {
                                                // Access the "size" array
                                                List<Map<String, Object>> sizeArray = (List<Map<String, Object>>) colorMap.get("size");

                                                // Iterate through each map in the "size" array
                                                for (Map<String, Object> sizeMap : sizeArray) {
                                                    Sizee sizee = new Sizee();
                                                    // Access properties like "sizeDes" and "quantity" within each map
                                                    if (sizeMap.containsKey("sizeDes") && sizeMap.containsKey("quantity")) {
                                                        String sizeDesValue = sizeMap.get("sizeDes").toString();
                                                        String quantityValue = sizeMap.get("quantity").toString();
                                                        sizee.setSizeDes(sizeDesValue);
                                                        sizee.setQuantity(quantityValue);
                                                        sz.add(sizee);
                                                        System.out.println("Size2:" + sizeDesValue + " quantity2: " + quantityValue);
                                                        // Process the "sizeDes" and "quantity" values
//                                                            Toast.makeText(infoItemView.this, "SizeDes: " + sizeDesValue + ", Quantity: " + quantityValue, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                modelss.setSize(sz);
                                            }

                                            colorModels.add(modelss);
                                            model.setModels(colorModels);
                                        }

                                    }

                                } else {
                                    Log.d("TAG", "Document doesn't contain the 'exampleArray' field");
                                }

                                // Create your model and populate data

                                model.setName(name);
                                model.setGiaM(giaM);
                                model.setGiaC(giaC);
                                model.setLoai(type);
                                model.setRating(rating);
                                model.setImgD(img);
                                model.setCount_color(countColor);
//                        Toast.makeText(infoItemView.this, name, Toast.LENGTH_SHORT).show();
                                // Add the model to your list or perform other operations
                                modelss.add(model);
                                loadsp(model);
                                // Notify adapter if needed
                                adapter.notifyDataSetChanged();
                                Log.d("TAG", "Object information: " + modelss.toString());
                                System.out.println("count color:"+model.getModels().size());
                                for (int ii = 0; ii < model.getModels().size(); ii++) {
//            Toast.makeText(infoItemView.this, model.getModels().size(), Toast.LENGTH_SHORT).show();
                                    System.out.println("color:"+model.getModels().get(ii).getColorDes());

                                    Toast.makeText(infoItemView.this, "Color: " + model.getModels().get(ii).getColorDes(), Toast.LENGTH_SHORT).show();

                                    for (int j = 0; j < model.getModels().get(ii).getSize().size(); j++) {
//                Toast.makeText(infoItemView.this, "Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes(), Toast.LENGTH_SHORT).show();
                                        System.out.println("Size: " + model.getModels().get(ii).getSize().get(j).getSizeDes()+" quantity: "+model.getModels().get(ii).getSize().get(j).getQuantity());
                                    }
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    }
                });

    }

    public void loadsp(InFoItemModel model) {

        name = findViewById(R.id.infoItemName);
        giaC = findViewById(R.id.infoItemGiaC);
        giaM = findViewById(R.id.infoItemGiaM);
        ratingtext = findViewById(R.id.infoItem_ratText);
        rat = findViewById(R.id.infoItem_rat);
        countcolor = findViewById(R.id.infoItemcountcolor);
        img = findViewById(R.id.infoItemImg);
        color = findViewById(R.id.colorbt);
        size = findViewById(R.id.sizebt);


        String imageUrl = model.getImgD(); // Assuming 'position' is the index of the image in the ArrayList
        Glide.with(this)
                .load(imageUrl)
                .into(img);
//        Glide.with(context).load(list.get(position).getImg()).into(holder.popImg);
        name.setText(model.getName());
        giaC.setText(model.getGiaC());
        giaM.setText(model.getGiaM());
        ratingtext.setText(model.getRating());
        rat.setRating(Float.parseFloat(model.getRating()));
        countcolor.setText(model.getCount_color() + " color");
        colorAdapter ad = new colorAdapter(infoItemView.this, colorModels, this);
        color.setHasFixedSize(true);
//        poprec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        color.setLayoutManager(layoutManager);
        color.setAdapter(ad);


    }

    int idcolor=0;
    @Override
    public void onItemClick(int position) {
        colorAdd=colorModels.get(position).getColorDes().toString().trim();
        idcolor=position;
        imgAdd=colorModels.get(position).getImg().toString().trim();
        System.out.println("Chọn màu: "+colorAdd);
        Glide.with(this)
                .load(imgAdd)
                .into(img);

//        Toast.makeText(this, "Clicked on color at position: " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSizeClick(int position) {
//        quan=Integer.getInteger(colorModels.get(idcolor).getSize().get(position).getQuantity());
        quan=Integer.parseInt(colorModels.get(idcolor).getSize().get(position).getQuantity());
        sizeAdd=colorModels.get(idcolor).getSize().get(position).getSizeDes().trim();
        System.out.println("Chọn size: "+sizeAdd);
        System.out.println("còn: "+quan);
        siezzz=findViewById(R.id.szzz);
        siezzz.setText("Size: "+ sizeAdd+" còn: "+quan+" sản phẩm");
        quantityty=findViewById(R.id.quantityty);
        quantityty.setVisibility(View.VISIBLE);
        addtocart=findViewById(R.id.addtocart);
        addtocart.setVisibility(View.VISIBLE);
//        Toast.makeText(infoItemView.this, "123", Toast.LENGTH_SHORT).show();
    }



    public class colorAdapter extends RecyclerView.Adapter<colorAdapter.ViewHolder2> implements RecyclerViewSizeClickListener {
        private Context context;
        private ArrayList<InfoItem_colorModel> list;
        private RecyclerViewClickListener mListener;

        public colorAdapter(Context context, ArrayList<InfoItem_colorModel> list,RecyclerViewClickListener m) {
            this.context = context;
            this.list = list;
            this.mListener=m;
        }

        @NonNull
        @Override
        public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(context).inflate(R.layout.buttoncolor,parent,false);
            return new ViewHolder2(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
            String imageUrl =list.get(position).getImg(); // Assuming 'position' is the index of the image in the ArrayList
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.img);
            holder.tv.setText(list.get(position).getColorDes().trim());
            if (list.get(position).isSelected()) {
                holder.cv.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_dark_background));
                holder.tv.setTextColor(ContextCompat.getColor(context, R.color.black));
            } else {
                holder.cv.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                holder.tv.setTextColor(ContextCompat.getColor(context,android.R.color.transparent));
            }
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.system_neutral1_100);
//                mListener.onItemClick(position);
                    resetSelection();
                    siezzz=findViewById(R.id.szzz);
                    siezzz.setText("Size:");
                    quantityty=findViewById(R.id.quantityty);
                    quantityty.setVisibility(View.GONE);
                    addtocart=findViewById(R.id.addtocart);
                    addtocart.setVisibility(View.GONE);
                    // Đánh dấu item được chọn
                    list.get(position).setSelected(true);

                    // Cập nhật lại giao diện
                    notifyDataSetChanged();
//                    ArrayList<Sizee> sz=colorModels.getSiz
                    ArrayList<Sizee>aa=list.get(position).getSize();
//                    for(int i=0;i<aa.size();i++){
////                        Toast.makeText((Context) infoItemView.this,"Size: "+aa.get(i).getSizeDes() +" Quantity: "+aa.get(i).getQuantity(),Toast.LENGTH_SHORT).show();
//                    }
                    sizeAdapter ad1 = new sizeAdapter((Context) infoItemView.this, list.get(position).getSize(), (RecyclerViewSizeClickListener) infoItemView.this);
                    size.setHasFixedSize(true);
//        poprec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                    GridLayoutManager layoutManager = new GridLayoutManager(infoItemView.this, 2, GridLayoutManager.VERTICAL, false);
                    size.setLayoutManager(layoutManager);
                    size.setAdapter(ad1);
                    mListener.onItemClick(position);
                }
            });
        }
        private void resetSelection() {
            for (InfoItem_colorModel item : list) {
                item.setSelected(false);
            }
        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onSizeClick(int position) {
            Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
        }

        public class ViewHolder2 extends RecyclerView.ViewHolder {
            ImageView img;
            TextView tv;
            CardView cv;
            public ViewHolder2(@NonNull View itemView) {
                super(itemView);
                img=itemView.findViewById(R.id.btnimgcolor);
                tv=itemView.findViewById(R.id.colorText);
                cv=itemView.findViewById(R.id.cardv);
            }
        }
    }

    public class sizeAdapter extends RecyclerView.Adapter<sizeAdapter.ViewHolder> {

        private Context context;
        private ArrayList<Sizee> list;
        private RecyclerViewSizeClickListener mListener;

        public sizeAdapter(Context context, ArrayList<Sizee> list, RecyclerViewSizeClickListener mListener) {
            this.context = context;
            this.list = list;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public sizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(context).inflate(R.layout.buttonsize,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull sizeAdapter.ViewHolder holder, int position) {
            if(Integer.parseInt(list.get(position).getQuantity().trim())>0){
                holder.sz.setText(list.get(position).getSizeDes().trim());
                holder.sz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSizeClick(position);
                    }
                });
            }
            else {
                holder.sz.setTextColor(com.google.android.material.R.color.cardview_dark_background);
                holder.sz.setBackground(getDrawable(R.drawable.botron_img2));
                holder.sz.setEnabled(false);
                holder.sz.setText(list.get(position).getSizeDes().trim());
                holder.sz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSizeClick(position);
                    }
                });
            }

//            Toast.makeText(infoItemView.this,list.get(position).getSizeDes(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        private void resetSelection() {
            for (Sizee item : list) {
                item.setSelected(false);
            }
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView sz;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                sz=itemView.findViewById(R.id.btncolorsize);
            }
        }
    }
}
interface RecyclerViewClickListener {
    void onItemClick(int position);
}
interface RecyclerViewSizeClickListener{
    void onSizeClick(int position);
}
//    public void onItemClick(int position) {
//        // Xử lý sự kiện click tại vị trí position
//        Toast.makeText(infoItemView.this, "Item clicked at position: " + position, Toast.LENGTH_SHORT).show();
//    }

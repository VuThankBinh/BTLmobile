package com.example.myapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.adapter.ThanhToanAdapter;
import com.example.myapp.cart.CartItemProduct;
import com.example.myapp.spinner.ApiService;
import com.example.myapp.spinner.District;
import com.example.myapp.spinner.Province;
import com.example.myapp.spinner.RetrofitService;
import com.example.myapp.spinner.Ward;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class ThanhToan extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView recThanhToan;
    ArrayList<CartItemProduct> arr;
    ThanhToanAdapter adapter;
    Button thanhtoan;
    TextView doiDC,nameNhan,sdtNhan,diaChiNhan;
    static String sdt="",name="",diachi="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        db= FirebaseFirestore.getInstance();

        recThanhToan=findViewById(R.id.recThanhToan);
        recThanhToan.setHasFixedSize(true);
        arr=new ArrayList<CartItemProduct>();
        adapter=new ThanhToanAdapter(this,arr);
        recThanhToan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recThanhToan.setAdapter(adapter);

        showCart();
        thanhtoan=findViewById(R.id.ThanhToanBtn2);
        doiDC=findViewById(R.id.doiDiaChi);
        nameNhan=findViewById(R.id.nameNgNhan);
        sdtNhan=findViewById(R.id.sdtNhan);
        diaChiNhan=findViewById(R.id.diaChi);
        nameNhan.setText(name);
        sdtNhan.setText(sdt);
        diaChiNhan.setText(diachi);
        doiDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        doiDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Dialog dialog = new Dialog(ThanhToan.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
//                View view = getLayoutInflater().inflate(R.layout.activity_test_spinner, null);
//                dialog.setContentView(view);
//
//                Button back = view.findViewById(R.id.btnXN);
//                back.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Đóng dialog trước khi chuyển đến ThanhToan
//                        dialog.dismiss();
//
//                        // Chuyển đến ThanhToan
//                        startActivity(new Intent(v.getContext(), ThanhToan.class));
//                    }
//                });
//
//// Hiển thị dialog
//                dialog.show();
                dialogDiaChiShow();
//                startActivity(new Intent(ThanhToan.this,TestSpinner.class));
            }
        });
        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameNhan.getText().toString().trim())){
                    Toast.makeText(ThanhToan.this, "Tên người nhận không được bỏ trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(sdtNhan.getText().toString().trim())){
                    Toast.makeText(ThanhToan.this, "SĐT người nhận không được bỏ trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(diaChiNhan.getText().toString().trim())){
                    Toast.makeText(ThanhToan.this, "Địa chỉ nhận không được bỏ trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(arr.size()==0){
                    Toast.makeText(ThanhToan.this, "Bạn chưa có sản phẩm nào trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    return;
                }
                oder();
            }
        });
    }
    private Spinner provinceSpinner, districtSpinner, wardSpinner;
    private List<Province> provinces;
    private List<District> districts;
    private List<Ward> wards;
    EditText nameDoi,sdtDoi;
    Button ok;
    private void dialogDiaChiShow() {
        Dialog dialog = new Dialog(ThanhToan.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.activity_test_spinner);
        provinceSpinner = dialog.findViewById(R.id.provinceSpinner);
        districtSpinner = dialog.findViewById(R.id.districtSpinner);
        wardSpinner = dialog.findViewById(R.id.wardSpinner);
        nameDoi=dialog.findViewById(R.id.nameNhan);
        sdtDoi=dialog.findViewById(R.id.sdtNhan);
        ok=dialog.findViewById(R.id.btnXN);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameDoi.getText().toString().trim())
                        ||TextUtils.isEmpty(sdtDoi.getText().toString().trim())
                        ||TextUtils.isEmpty(wardSpinner.getSelectedItem().toString().trim())
                        ||TextUtils.isEmpty(districtSpinner.getSelectedItem().toString().trim())
                        ||TextUtils.isEmpty(provinceSpinner.getSelectedItem().toString().trim())){
                    Toast.makeText(v.getContext(), "Bạn phải nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isValidPhoneNumber(sdtDoi.getText().toString().trim())==false){
                    Toast.makeText(v.getContext(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                sdt=sdtDoi.getText().toString().trim();
                name=nameDoi.getText().toString().trim();
                diachi=wards.get(wardSpinner.getSelectedItemPosition()).toString().trim()
                        +", "+districts.get(districtSpinner.getSelectedItemPosition()).toString().trim()
                        +", "+provinces.get(provinceSpinner.getSelectedItemPosition()).toString().trim();
//                startActivity(new Intent(v.getContext(),ThanhToan.class));
                nameNhan.setText(name);
                sdtNhan.setText(sdt);
                diaChiNhan.setText(diachi);
                dialog.dismiss();
            }
        });
        // Gọi AsyncTask để tải dữ liệu từ API
        new LoadDataAsyncTask().execute();
        dialog.show();
    }
    private void updateProvinceSpinner() {
        if (provinces != null) {
            ArrayAdapter<Province> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
            adapter.setDropDownViewResource(R.layout.diachiitem);
            provinceSpinner.setAdapter(adapter);

            provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (provinces.size() > position) {
                        int provinceCode = provinces.get(position).getCode();
                        new LoadDistrictsAsyncTask().execute(provinceCode);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không cần xử lý gì cả
                }
            });
        }
    }

    private void updateDistrictSpinner() {
        if (districts != null) {
            ArrayAdapter<District> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
            adapter.setDropDownViewResource(R.layout.diachiitem);
            districtSpinner.setAdapter(adapter);

            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (districts.size() > position) {
                        int districtCode = districts.get(position).getCode();
                        new LoadWardsAsyncTask().execute(districtCode);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không cần xử lý gì cả
                }
            });
        }
    }

    private void updateWardSpinner() {
        if (wards != null) {
            ArrayAdapter<Ward> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wards);
            adapter.setDropDownViewResource(R.layout.diachiitem);
            wardSpinner.setAdapter(adapter);
        }
    }

    class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ApiService apiService = RetrofitService.getRetrofitInstance().create(ApiService.class);

            // Lấy danh sách tỉnh
            Call<List<Province>> provinceCall = apiService.getProvinces();
            try {
                Response<List<Province>> provinceResponse = provinceCall.execute();
                provinces = provinceResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Cập nhật Spinner tỉnh
            updateProvinceSpinner();
        }
    }

    private class LoadDistrictsAsyncTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            if (params.length > 0) {
                int provinceCode = params[0];
                ApiService apiService = RetrofitService.getRetrofitInstance().create(ApiService.class);

                // Lấy danh sách huyện của tỉnh được chọn
                Call<List<District>> districtCall = apiService.getDistricts();
                try {
                    Response<List<District>> districtResponse = districtCall.execute();
                    List<District> listDistricts = districtResponse.body();
                    districts = new ArrayList<>();
                    listDistricts.forEach(district -> {
                        if(district.getProvinceCode() == provinceCode) {
                            districts.add(district);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Cập nhật Spinner huyện
            updateDistrictSpinner();
        }
    }

    private class LoadWardsAsyncTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            if (params.length > 0) {
                int districtCode = params[0];
                ApiService apiService = RetrofitService.getRetrofitInstance().create(ApiService.class);

                // Lấy danh sách xã của huyện được chọn
                Call<List<Ward>> wardCall = apiService.getWards();
                try {
                    Response<List<Ward>> wardResponse = wardCall.execute();
                    List<Ward> listWard = wardResponse.body();
                    wards = new ArrayList<>();
                    listWard.forEach(ward -> {
                        if(ward.getDistrictCode() == districtCode) {
                            wards.add(ward);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Cập nhật Spinner xã
            updateWardSpinner();
        }
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy cho số điện thoại, ví dụ: +84xxxxxxxxx hoặc 0xxxxxxxxx
        String regex = "^(\\+84|0)\\d{9,10}$";

        // Tạo Pattern từ biểu thức chính quy
        Pattern pattern = Pattern.compile(regex);

        // Kiểm tra xem chuỗi nhập vào có khớp với biểu thức chính quy hay không
        return pattern.matcher(phoneNumber).matches();
    }
    int tong=0;
    private void oder(){

        db = FirebaseFirestore.getInstance();
//        TruSoLuong("1","2","3",1);
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("Name", nameNhan.getText().toString().trim()); // Replace with the actual user name
        userDetails.put("PhoneNumber", sdtNhan.getText().toString().trim()); // Replace with the actual phone number
        userDetails.put("Address", diaChiNhan.getText().toString().trim()); // Replace with the actual address
        String saveDate, saveTime;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(calendar.getTime());
        userDetails.put("currentDate", saveDate);
        userDetails.put("currentTime", saveTime);
        Map<String, Object> combinedMap = new HashMap<>();
        List<Map<String, Object>> orderList = new ArrayList<>();
        for (int i=0;i<arr.size();i++) {
//            System.out.println(arr.size());

            HashMap<String, Object> oderMap = new HashMap<>();
            oderMap.put("ProductImg", arr.get(i).getImgD().trim());
            oderMap.put("PrductName", arr.get(i).getName().trim());
            oderMap.put("ProductColor", arr.get(i).getColor().trim());
            oderMap.put("ProductSize", arr.get(i).getSize().trim());
            oderMap.put("ProductPrice", arr.get(i).getPrice().trim());
            oderMap.put("ProductQuantity", arr.get(i).getQuantity().trim());
            orderList.add(oderMap);
            updateQuantityBySizeDes("ball",arr.get(i).getIdSP().toString(),arr.get(i).getColor().toString(),arr.get(i).getSize().toString(),arr.get(i).getQuantity().toString());
            updateQuantityBySizeDes("giay",arr.get(i).getIdSP().toString(),arr.get(i).getColor().toString(),arr.get(i).getSize().toString(),arr.get(i).getQuantity().toString());
            updateQuantityBySizeDes("QuanAo",arr.get(i).getIdSP().toString(),arr.get(i).getColor().toString(),arr.get(i).getSize().toString(),arr.get(i).getQuantity().toString());
            updateQuantityBySizeDes("baoho",arr.get(i).getIdSP().toString(),arr.get(i).getColor().toString(),arr.get(i).getSize().toString(),arr.get(i).getQuantity().toString());

            FirebaseFirestore.getInstance().collection("AddToCart")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("CurrentUser")
                    .document(arr.get(i).getId())
                    .delete();

// Combine the order details and user details into a single Map


        }
        combinedMap.put("UserDetails", userDetails);
        combinedMap.put("OrderDetails", orderList);
        if (MainActivity.loginWithAcc == true) {
            db.collection("AddToOder")
                    .document(MainActivity.auth.getCurrentUser().getUid())
                    .collection("CurrentUser")
                    .add(combinedMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            Toast.makeText(ThanhToan.this, "Thanh toán thành công!!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ThanhToan.this, My_oder.class));
                        }
                    });
        }
        if (MainActivity.loginWithGG == true) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            db.collection("AddToOder")
                    .document(MainActivity.auth.getUid())
                    .collection("CurrentUser")
                    .add(combinedMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(ThanhToan.this, "Thanh toán thành công!!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ThanhToan.this, My_oder.class));
                        }
                    });
        }
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
                                tong+= Integer.parseInt(quantity)*Double.parseDouble(price.replaceAll("[^0-9]", ""));
                            System.out.println(name+"color: "+ color +"size: "+sz);
                                CartItemProduct c=new CartItemProduct(id, name, imgD, color, sz, price, quantity,idsp);
                                arr.add(c);
                                TextView sumMoney=findViewById(R.id.sumMoney);
                                sumMoney.setText(tong+"$");

                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
        if(MainActivity.loginWithGG==true){
            GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
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
//                            System.out.println(name+"color: "+ color +"size: "+sz);
                                CartItemProduct c=new CartItemProduct(id, name, imgD, color, sz, price, quantity,idsp);
                                arr.add(c);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }

    private void updateQuantityBySizeDes(String collectionPath, String documentId, String colorDes, String sizeDesValue, String newQuantityValue) {
        DocumentReference docRef = db.collection(collectionPath).document(documentId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy giá trị hiện tại của mảng color
                        ArrayList<Map<String, Object>> colorArray = (ArrayList<Map<String, Object>>) document.get("color");

                        // Lặp qua mảng color
                        for (Map<String, Object> colorMap : colorArray) {
                            String currentColorDes = (String) colorMap.get("colorDes");
                            if (currentColorDes.equals(colorDes)) {
                                // Lấy giá trị hiện tại của mảng size trong mảng color
                                ArrayList<Map<String, Object>> sizeArray = (ArrayList<Map<String, Object>>) colorMap.get("size");

                                // Tìm kiếm phần tử trong mảng size có trường sizeDes bằng newSizeDesValue
                                for (Map<String, Object> sizeMap : sizeArray) {
                                    String currentSizeDes = (String) sizeMap.get("sizeDes");
                                    if (currentSizeDes != null && currentSizeDes.equals(sizeDesValue)) {
                                        int currentQuantity = Integer.parseInt(sizeMap.get("quantity").toString());
                                        System.out.println(currentQuantity+" còn");
                                        int newQuan = currentQuantity -Integer.parseInt(newQuantityValue);
                                        System.out.println(newQuan);
                                        // Cập nhật trường quantity của phần tử có sizeDes bằng newSizeDesValue
                                        sizeMap.put("quantity", String.valueOf(newQuan));
                                    }
                                }
                            }
                        }

                        // Cập nhật dữ liệu trên Firestore
                        docRef.update("color", colorArray)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "Cập nhật dữ liệu thành công!");
                                        } else {
                                            Log.w("TAG", "Lỗi khi cập nhật dữ liệu", task.getException());
                                        }
                                    }
                                });
                    } else {
                        Log.d("TAG", "Không tìm thấy tài liệu");
                    }
                } else {
                    Log.w("TAG", "Lỗi khi đọc tài liệu", task.getException());
                }
            }
        });
    }


}
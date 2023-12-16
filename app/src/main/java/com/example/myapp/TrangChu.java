package com.example.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.myapp.Models.PopulartModel;
import com.example.myapp.adapter.PopularAdapter;
import com.example.myapp.adapter.RecyclerViewSPClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class TrangChu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewSPClickListener {
    private int currentPage = 0;
    private static final int DELAY_MS = 3000; // 3 seconds delay
    private static final int PERIOD_MS = 4000; // 3 seconds period
    LinearLayout giay,quanAo,bong,tat,balo,baoho;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    EditText searchsp;
    TextView textView;
    RecyclerView poprec,poprec2;
    FirebaseFirestore db;
    PopularAdapter popularAdapter,adpTimKiem;
    ArrayList<PopulartModel> populartModels, modelTK,arrchung;


    private void resetColors() {
//        giay.setBackgroundColor(Color.TRANSPARENT);
        giay.setBackgroundResource(R.drawable.botron_img);

//        quanAo.setBackgroundColor(Color.TRANSPARENT);
        quanAo.setBackgroundResource(R.drawable.botron_img);

//        bong.setBackgroundColor(Color.TRANSPARENT);
        bong.setBackgroundResource(R.drawable.botron_img);

//        tat.setBackgroundColor(Color.TRANSPARENT);
        tat.setBackgroundResource(R.drawable.botron_img);

//        balo.setBackgroundColor(Color.TRANSPARENT);
        balo.setBackgroundResource(R.drawable.botron_img);

//        baoho.setBackgroundColor(Color.TRANSPARENT);
        baoho.setBackgroundResource(R.drawable.botron_img);

//        luoi.setBackgroundColor(Color.TRANSPARENT);
//        luoi.setBackgroundResource(R.drawable.botron_img);
    }
    private void changeColor(LinearLayout layout) {
        // Reset màu của tất cả các LinearLayout
        resetColors();

        // Đặt màu cho LinearLayout được click
//        layout.setBackgroundColor(Color.parseColor("#C0C0C0")); // Thay "#your_color" bằng mã màu bạn muốn
        Drawable botronDrawable = ContextCompat.getDrawable(this, R.drawable.botron_img);

        // Đặt màu xám cho drawable
        if (botronDrawable != null) {
            botronDrawable.setColorFilter(Color.parseColor("#C0C0C0"), PorterDuff.Mode.SRC_ATOP);
        }

        layout.setBackground(botronDrawable);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle;
        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.nav_home);
        menu=navigationView.getMenu();


        poprec=findViewById(R.id.pop_rec2);
        poprec2=findViewById(R.id.pop_recsearch);

        poprec.setHasFixedSize(true);
//        poprec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        poprec.setLayoutManager(layoutManager);
        db=FirebaseFirestore.getInstance();
        populartModels=new ArrayList<PopulartModel>();
        arrchung=new ArrayList<PopulartModel>();
        popularAdapter=new PopularAdapter(TrangChu.this,populartModels,this);
        poprec.setAdapter(popularAdapter);
        EventShow();
//        showGiay();
        showcategory();
        ImageView car=findViewById(R.id.cartMain);
        car.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(TrangChu.this,cart_view.class));
    }
});

        ImageView us=findViewById(R.id.UsertMain);
        us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.loginWithAcc==true){

                    startActivity(new Intent(TrangChu.this, profile_acc.class));
                }
                if (MainActivity.loginWithGG==true){
                    Toast.makeText(TrangChu.this, "Bạn đăng nhập bằng google nên không thể sửa thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        giay=findViewById(R.id.giay);
        quanAo=findViewById(R.id.quanAo);
        bong=findViewById(R.id.bong);
        tat=findViewById(R.id.tat);
        balo=findViewById(R.id.balo);
        baoho=findViewById(R.id.baoho);
//        luoi=findViewById(R.id.luoi);
        giay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(giay);
                showGiay();
                poprec.setVisibility(View.VISIBLE);
                    poprec2.setVisibility(View.GONE);
                TextView tt=findViewById(R.id.titlesearch);
                    tt.setVisibility(View.GONE);

            }
        });

        quanAo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(quanAo);
                showQuanAo();
                poprec.setVisibility(View.VISIBLE);
                poprec2.setVisibility(View.GONE);
                TextView tt=findViewById(R.id.titlesearch);
                tt.setVisibility(View.GONE);

            }
        });

        bong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(bong);
                showBong();
                poprec.setVisibility(View.VISIBLE);
                poprec2.setVisibility(View.GONE);
                TextView tt=findViewById(R.id.titlesearch);
                tt.setVisibility(View.GONE);

            }
        });

        tat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(tat);
            }
        });

        balo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(balo);
            }
        });

        baoho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(baoho);
                showBaoHo();
                poprec.setVisibility(View.VISIBLE);
                poprec2.setVisibility(View.GONE);
                TextView tt=findViewById(R.id.titlesearch);
                tt.setVisibility(View.GONE);
//                searchsp.setText("");

            }
        });

//        luoi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeColor(luoi);
//            }
//        });

//        Toolbar toolbar=findViewById(R.id.toolbar);



        ViewPager viewPager = findViewById(R.id.viewPager);
        ImagePagerAdapter adapter;
        adapter = new ImagePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentPage == adapter.getCount()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, PERIOD_MS);
            }
        }, DELAY_MS);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && viewPager.getCurrentItem() == adapter.getCount() - 1) {
                    // Nếu đang ở trang cuối và không có cuộc di chuyển, chuyển về trang đầu tiên
                    viewPager.setCurrentItem(0, true); // Set to the first page without animation
                    currentPage = 0; // Reset currentPage to 0
                }
            }
        });
        searchsp=findViewById(R.id.search_sp);
        poprec2.setHasFixedSize(true);
//        poprec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        poprec2.setLayoutManager(layoutManager2);
        modelTK=new ArrayList<PopulartModel>();

        adpTimKiem=new PopularAdapter(TrangChu.this,modelTK,this);
        poprec2.setAdapter(adpTimKiem);
//        searchsp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    // Xử lý sự kiện khi người dùng nhấn "Enter"
//                    handleEnterKeyPress();
//                    return true; // Đã xử lý sự kiện
//                }
//                return false; // Chưa xử lý sự kiện
//            }
//        });

        searchsp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    TextView tt=findViewById(R.id.titlesearch);
                    tt.setVisibility(View.GONE);
                    populartModels.clear();
                    popularAdapter.notifyDataSetChanged();
                    populartModels.addAll(arrchung);
                    popularAdapter.notifyDataSetChanged();
                    poprec.setVisibility(View.VISIBLE);
                    poprec2.setVisibility(View.GONE);
                }
                else {
                    search_sp(s.toString().trim());
                }
            }
        });
    }

//    private void handleEnterKeyPress() {
//        search_sp(searchsp.getText().toString().trim());
//    }

    private void search_sp(String s) {
        if(!s.isEmpty()){

            modelTK.clear();
            adpTimKiem.notifyDataSetChanged();
            resetColors();
            for(int i=0;i<arrchung.size();i++){
                System.out.println(arrchung.get(i).getName());
                if(arrchung.get(i).getName().toUpperCase().contains(s.toUpperCase())){
//                    System.out.println(populartModels.get(i).getName());
                    modelTK.add(arrchung.get(i));
                    adpTimKiem.notifyDataSetChanged();
                }
            }
            if(modelTK.size()!=0){
                TextView tt=findViewById(R.id.titlesearch);
                tt.setText("Sản phẩm chứa: "+"'"+s+"'");
                poprec.setVisibility(View.GONE);
                poprec2.setVisibility(View.VISIBLE);
                tt.setVisibility(View.VISIBLE);
            }
            else {
                TextView tt=findViewById(R.id.titlesearch);
                tt.setText("Không có sản phẩm chứa: "+"'"+s+"'");
                tt.setHeight(200);
                poprec.setVisibility(View.GONE);
                tt.setVisibility(View.VISIBLE);
            }
//            populartModels.clear();
//            popularAdapter.notifyDataSetChanged();

        }
        else {
            return;
        }
    }

    private void showBaoHo() {populartModels.clear();
        db.collection("baoho")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                    String id=document.getId();
                                    String name=document.getData().get("name").toString();
                                    String des=document.getData().get("priceM").toString();
                                    String rating=document.getData().get("rating").toString();
                                    String type= document.getData().get("loai").toString();
                                    String img=document.getData().get("imgD").toString();
                                    String countColor = "";
                                    if (document.contains("color")) {
                                        // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                        List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                        // Kiểm tra xem colorList có giá trị và không rỗng không
                                        if (colorList != null && !colorList.isEmpty()) {
                                            // Lấy độ dài của mảng colorList
                                            int arrayLength = colorList.size();

                                            // Sử dụng arrayLength theo nhu cầu của bạn
//                                            System.out.println("Độ dài của mảng color: " + arrayLength);
                                            countColor = String.valueOf(arrayLength);
                                        } else {
                                            // Xử lý khi mảng colorList là null hoặc rỗng
//                                            System.out.println("Mảng color là null hoặc rỗng");
                                        }
                                    } else {
                                        // Xử lý khi trường "color" không tồn tại trong document
//                                        System.out.println("Trường 'color' không tồn tại trong document");
                                    }
//                                Toast.makeText(TrangChu.this,document.getData().get("loai").toString(),Toast.LENGTH_SHORT).show();
                                    PopulartModel model=new PopulartModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setDescription(des);
                                    model.setType(type);
                                    model.setRating(rating);
                                    model.setImg(img);
                                    model.setCount_color(countColor);
                                    populartModels.add(model);
                            }
                            popularAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void showBong() {
        populartModels.clear();
        db.collection("ball")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count3=0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String id=document.getId();
                                String name=document.getData().get("name").toString();
                                String des=document.getData().get("priceM").toString();
                                String rating=document.getData().get("rating").toString();
                                String type= document.getData().get("loai").toString();
                                String img=document.getData().get("imgD").toString();
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
//                                        System.out.println("Mảng color là null hoặc rỗng");
                                    }
                                } else {
                                    // Xử lý khi trường "color" không tồn tại trong document
//                                    System.out.println("Trường 'color' không tồn tại trong document");
                                }
//                                Toast.makeText(TrangChu.this,document.getData().get("loai").toString(),Toast.LENGTH_SHORT).show();
                                PopulartModel model=new PopulartModel();
                                model.setId(id);
                                model.setName(name);
                                model.setDescription(des);
                                model.setType(type);
                                model.setRating(rating);
                                model.setImg(img);
                                model.setCount_color(countColor);
                                populartModels.add(model);//                                Toast.makeText(TrangChu.this,model.getName(),Toast.LENGTH_SHORT).show();

//                                Log.d("TAG", document.getId() + " => " + document.getData());
                                count3++;

                            }
                            popularAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void showQuanAo() {
        populartModels.clear();
        db.collection("QuanAo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                    String id=document.getId();
                                    String name=document.getData().get("name").toString();
                                    String des=document.getData().get("priceM").toString();
                                    String rating=document.getData().get("rating").toString();
                                    String type= document.getData().get("loai").toString();
                                    String img=document.getData().get("imgD").toString();
                                    String countColor = "";
                                    if (document.contains("color")) {
                                        // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                        List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                        // Kiểm tra xem colorList có giá trị và không rỗng không
                                        if (colorList != null && !colorList.isEmpty()) {
                                            // Lấy độ dài của mảng colorList
                                            int arrayLength = colorList.size();

                                            // Sử dụng arrayLength theo nhu cầu của bạn
//                                            System.out.println("Độ dài của mảng color: " + arrayLength);
                                            countColor = String.valueOf(arrayLength);
                                        } else {
                                            // Xử lý khi mảng colorList là null hoặc rỗng
//                                            System.out.println("Mảng color là null hoặc rỗng");
                                        }
                                    } else {
                                        // Xử lý khi trường "color" không tồn tại trong document
//                                        System.out.println("Trường 'color' không tồn tại trong document");
                                    }
//                                Toast.makeText(TrangChu.this,document.getData().get("loai").toString(),Toast.LENGTH_SHORT).show();
                                    PopulartModel model=new PopulartModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setDescription(des);
                                    model.setType(type);
                                    model.setRating(rating);
                                    model.setImg(img);
                                    model.setCount_color(countColor);
                                    populartModels.add(model);
                            }
                            popularAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void showGiay() {
        populartModels.clear();
        popularAdapter.notifyDataSetChanged();
        db.collection("giay")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String id=document.getId();
                                String name=document.getData().get("name").toString();
                                String des=document.getData().get("priceM").toString();
                                String rating=document.getData().get("rating").toString();
                                String type= document.getData().get("loai").toString();
                                String img=document.getData().get("imgD").toString();
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
//                                        System.out.println("Mảng color là null hoặc rỗng");
                                    }
                                } else {
                                    // Xử lý khi trường "color" không tồn tại trong document
//                                    System.out.println("Trường 'color' không tồn tại trong document");
                                }
//                                Toast.makeText(TrangChu.this,document.getData().get("loai").toString(),Toast.LENGTH_SHORT).show();
                                PopulartModel model=new PopulartModel();
                                model.setId(id);
                                model.setName(name);
                                model.setDescription(des);
                                model.setType(type);
                                model.setRating(rating);
                                model.setImg(img);
                                model.setCount_color(countColor);
                                populartModels.add(model);
                                popularAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void showcategory() {

        db.collection("default")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String collectionName = (String) document.getData().get("collectionName");
//                                Log.d(TAG, "Collection Name: " + collectionName);
                                Toast.makeText(TrangChu.this,collectionName,Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(TrangChu.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void EventShow() {
//        populartModels.clear();
//        arrchung.clear();
//        popularAdapter.notifyDataSetChanged();
        db.collection("baoho")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count4=0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(count4<3){
                                    String id=document.getId();
                                    String name=document.getData().get("name").toString();
                                    String des=document.getData().get("priceM").toString();
                                    String rating=document.getData().get("rating").toString();
                                    String type= document.getData().get("loai").toString();
                                    String img=document.getData().get("imgD").toString();
                                    String countColor = "";
                                    if (document.contains("color")) {
                                        // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                        List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                        // Kiểm tra xem colorList có giá trị và không rỗng không
                                        if (colorList != null && !colorList.isEmpty()) {
                                            // Lấy độ dài của mảng colorList
                                            int arrayLength = colorList.size();

                                            // Sử dụng arrayLength theo nhu cầu của bạn
//                                            System.out.println("Độ dài của mảng color: " + arrayLength);
                                            countColor = String.valueOf(arrayLength);
                                        } else {
                                            // Xử lý khi mảng colorList là null hoặc rỗng
//                                            System.out.println("Mảng color là null hoặc rỗng");
                                        }
                                    } else {
                                        // Xử lý khi trường "color" không tồn tại trong document
//                                        System.out.println("Trường 'color' không tồn tại trong document");
                                    }
//                                Toast.makeText(TrangChu.this,document.getData().get("loai").toString(),Toast.LENGTH_SHORT).show();
                                    PopulartModel model=new PopulartModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setDescription(des);
                                    model.setType(type);
                                    model.setRating(rating);
                                    model.setImg(img);
                                    model.setCount_color(countColor);
                                    populartModels.add(model);
                                    arrchung.add(model);
//                                Toast.makeText(TrangChu.this,model.getName(),Toast.LENGTH_SHORT).show();
                                }
//                                Log.d("TAG", document.getId() + " => " + document.getData());
                                count4++;

                            }
                            popularAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });



        db.collection("ball")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count3=0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(count3<3){
                                    String id=document.getId();
                                    String name=document.getData().get("name").toString();
                                    String des=document.getData().get("priceM").toString();
                                    String rating=document.getData().get("rating").toString();
                                    String type= document.getData().get("loai").toString();
                                    String img=document.getData().get("imgD").toString();
                                    String countColor = "";
                                    if (document.contains("color")) {
                                        // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                        List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                        // Kiểm tra xem colorList có giá trị và không rỗng không
                                        if (colorList != null && !colorList.isEmpty()) {
                                            // Lấy độ dài của mảng colorList
                                            int arrayLength = colorList.size();

                                            // Sử dụng arrayLength theo nhu cầu của bạn
//                                            System.out.println("Độ dài của mảng color: " + arrayLength);
                                            countColor = String.valueOf(arrayLength);
                                        } else {
                                            // Xử lý khi mảng colorList là null hoặc rỗng
//                                            System.out.println("Mảng color là null hoặc rỗng");
                                        }
                                    } else {
                                        // Xử lý khi trường "color" không tồn tại trong document
//                                        System.out.println("Trường 'color' không tồn tại trong document");
                                    }
//                                Toast.makeText(TrangChu.this,document.getData().get("loai").toString(),Toast.LENGTH_SHORT).show();
                                    PopulartModel model=new PopulartModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setDescription(des);
                                    model.setType(type);
                                    model.setRating(rating);
                                    model.setImg(img);
                                    model.setCount_color(countColor);
                                    arrchung.add(model);
                                    populartModels.add(model);
//                                Toast.makeText(TrangChu.this,model.getName(),Toast.LENGTH_SHORT).show();
                                }
//                                Log.d("TAG", document.getId() + " => " + document.getData());
                                count3++;

                            }
                            popularAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("QuanAo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count2=0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(count2<3){
                                    String id=document.getId().toString();
                                    String name=document.getData().get("name").toString();
                                    String des=document.getData().get("priceM").toString();
                                    String rating=document.getData().get("rating").toString();
                                    String type= document.getData().get("loai").toString();
                                    String img=document.getData().get("imgD").toString();
                                    String countColor = "";
                                    if (document.contains("color")) {
                                        // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                        List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                        // Kiểm tra xem colorList có giá trị và không rỗng không
                                        if (colorList != null && !colorList.isEmpty()) {
                                            // Lấy độ dài của mảng colorList
                                            int arrayLength = colorList.size();

                                            // Sử dụng arrayLength theo nhu cầu của bạn
//                                            System.out.println("Độ dài của mảng color: " + arrayLength);
                                            countColor = String.valueOf(arrayLength);
                                        } else {
                                            // Xử lý khi mảng colorList là null hoặc rỗng
//                                            System.out.println("Mảng color là null hoặc rỗng");
                                        }
                                    } else {
                                        // Xử lý khi trường "color" không tồn tại trong document
//                                        System.out.println("Trường 'color' không tồn tại trong document");
                                    }
//                                Toast.makeText(TrangChu.this,document.getData().get("loai").toString(),Toast.LENGTH_SHORT).show();
                                    PopulartModel model=new PopulartModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setDescription(des);
                                    model.setType(type);
                                    model.setRating(rating);
                                    model.setImg(img);
                                    model.setCount_color(countColor);
                                    arrchung.add(model);
                                    populartModels.add(model);

//                                Toast.makeText(TrangChu.this,model.getName(),Toast.LENGTH_SHORT).show();
                                }
                                count2 += 1;
//                                Log.d("TAG", document.getId() + " => " + document.getData());


                            }
                            popularAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("giay")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count1=0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(count1<3){
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    String id=document.getId();
                                    String name=document.getData().get("name").toString();
                                    String des=document.getData().get("priceM").toString();
                                    String rating=document.getData().get("rating").toString();
                                    String type= document.getData().get("loai").toString();
                                    String img=document.getData().get("imgD").toString();
                                    String countColor = "";
                                    if (document.contains("color")) {
                                        // Lấy giá trị của trường "color" là một List<Map<String, Object>>
                                        List<Map<String, Object>> colorList = (List<Map<String, Object>>) document.get("color");

                                        // Kiểm tra xem colorList có giá trị và không rỗng không
                                        if (colorList != null && !colorList.isEmpty()) {
                                            // Lấy độ dài của mảng colorList
                                            int arrayLength = colorList.size();

                                            // Sử dụng arrayLength theo nhu cầu của bạn
//                                            System.out.println("Độ dài của mảng color: " + arrayLength);
                                            countColor = String.valueOf(arrayLength);
                                        } else {
                                            // Xử lý khi mảng colorList là null hoặc rỗng
//                                            System.out.println("Mảng color là null hoặc rỗng");
                                        }
                                    } else {
                                        // Xử lý khi trường "color" không tồn tại trong document
//                                        System.out.println("Trường 'color' không tồn tại trong document");
                                    }
//                                Toast.makeText(TrangChu.this,document.getData().get("loai").toString(),Toast.LENGTH_SHORT).show();
                                    PopulartModel model=new PopulartModel();
                                    model.setId(id);
                                    model.setName(name);
                                    model.setDescription(des);
                                    model.setType(type);
                                    model.setRating(rating);
                                    model.setImg(img);
                                    model.setCount_color(countColor);
                                    populartModels.add(model);
                                    arrchung.add(model);
                                }
                                count1++;
//
//                                Toast.makeText(TrangChu.this,model.getName(),Toast.LENGTH_SHORT).show();

                            }
                            popularAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }



    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_home1:
//                // Thực hiện hành động khi nhấn vào nav_home
//                break;
//            case R.id.nav_properties1:
//                startActivity(new Intent(TrangChu.this, fragment_profile.class));
//                break;
//        }
        if(item.getItemId()==R.id.nav_home1){
        }
        if(item.getItemId()==R.id.nav_properties1){
            if(MainActivity.loginWithAcc==true){

                startActivity(new Intent(TrangChu.this, profile_acc.class));
            }
            if (MainActivity.loginWithGG==true){
                Toast.makeText(this, "Bạn đăng nhập bằng google nên không thể sửa thông tin", Toast.LENGTH_SHORT).show();
            }
        }
        if(item.getItemId()==R.id.nav_clothes1){
            changeColor(quanAo);
            showQuanAo();
        }
        if(item.getItemId()==R.id.nav_shoe1){
            changeColor(giay);
            showGiay();
        }
        if(item.getItemId()==R.id.nav_ball1){
            changeColor(bong);
            showBong();
        }
        if(item.getItemId()==R.id.nav_logout){
            if(MainActivity.loginWithGG==true){
                MainActivity.loginWithGG=false;
                MainActivity.mgs.signOut();
                startActivity(new Intent(TrangChu.this, MainActivity.class));
            }
            if(MainActivity.loginWithAcc==true){
                MainActivity.loginWithAcc=false;
                MainActivity.auth.signOut();
                startActivity(new Intent(TrangChu.this,MainActivity.class));
            }
        }
        if(item.getItemId()==R.id.nav_cart1){
            startActivity(new Intent(TrangChu.this, cart_view.class));
        }
        if(item.getItemId()==R.id.nav_oder1){
            startActivity(new Intent(TrangChu.this, My_oder.class));
        }
        if(item.getItemId()==R.id.nav_properties1){
            if(MainActivity.loginWithGG==true){

                Toast.makeText(this, "Bạn đăng nhập bằng gg", Toast.LENGTH_SHORT).show();
            }
            if(MainActivity.loginWithAcc==true){
                startActivity(new Intent(TrangChu.this,profile_acc.class));
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(this, "Clicked on color at position: " + position, Toast.LENGTH_SHORT) .show();
    }
}
package com.example.myapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.spinner.ApiService;
import com.example.myapp.spinner.District;
import com.example.myapp.spinner.Province;
import com.example.myapp.spinner.RetrofitService;
import com.example.myapp.spinner.Ward;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class TestSpinner extends AppCompatActivity {
    private Spinner provinceSpinner, districtSpinner, wardSpinner;
    private List<Province> provinces;
    private List<District> districts;
    private List<Ward> wards;
    private FirebaseFirestore db;
    EditText name,sdt;
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_spinner);

        provinceSpinner = findViewById(R.id.provinceSpinner);
        districtSpinner = findViewById(R.id.districtSpinner);
        wardSpinner = findViewById(R.id.wardSpinner);
        name=findViewById(R.id.nameNhan);
        sdt=findViewById(R.id.sdtNhan);
        ok=findViewById(R.id.btnXN);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText().toString().trim())
                        ||TextUtils.isEmpty(sdt.getText().toString().trim())
                        ||TextUtils.isEmpty(wardSpinner.getSelectedItem().toString().trim())
                        ||TextUtils.isEmpty(districtSpinner.getSelectedItem().toString().trim())
                        ||TextUtils.isEmpty(provinceSpinner.getSelectedItem().toString().trim())){
                    Toast.makeText(TestSpinner.this, "Bạn phải nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isValidPhoneNumber(sdt.getText().toString().trim())==false){
                    Toast.makeText(TestSpinner.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                ThanhToan.sdt=sdt.getText().toString().trim();
                ThanhToan.name=name.getText().toString().trim();
                ThanhToan.diachi=wards.get(wardSpinner.getSelectedItemPosition()).toString().trim()
                                +", "+districts.get(districtSpinner.getSelectedItemPosition()).toString().trim()
                                +", "+provinces.get(provinceSpinner.getSelectedItemPosition()).toString().trim();
                startActivity(new Intent(TestSpinner.this,ThanhToan.class));
            }
        });
        // Gọi AsyncTask để tải dữ liệu từ API
        new LoadDataAsyncTask().execute();

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

    private class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {
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
}
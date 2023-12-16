package com.example.myapp.spinner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
public interface ApiService {
    @GET("/api/p/")
    Call<List<Province>> getProvinces();

    @GET("/api/d/")
    Call<List<District>> getDistricts();

    @GET("/api/w/")
    Call<List<Ward>> getWards();
}

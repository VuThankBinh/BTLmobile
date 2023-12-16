package com.example.myapp.spinner;
import com.google.gson.annotations.SerializedName;

public class Ward {
    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private int code;

    @SerializedName("division_type")
    private String divisionType;

    @SerializedName("codename")
    private String codename;

    @SerializedName("district_code")
    private int districtCode;

    // Các getter và setter ở đây

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDivisionType() {
        return divisionType;
    }

    public void setDivisionType(String divisionType) {
        this.divisionType = divisionType;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    @Override
    public String toString() {
        return name;
    }
}


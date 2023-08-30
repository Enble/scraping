package com.example.shelterjsontocsv;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;


public class ShelterDto {

    int count;
    String shelterCategory;
    String name;
    Double latitude;
    Double longitude;
    String tel;
    String detailAddress;
    String legalDongCode;

    public ShelterDto(int count, String shelterCategory, String name, Double latitude, Double longitude, String tel,
            String detailAddress,
            String legalDongCode) {
        this.count = count;
        this.shelterCategory = shelterCategory;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tel = tel;
        this.detailAddress = detailAddress;
        this.legalDongCode = legalDongCode;
    }
}

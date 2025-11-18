package com.golflink.golflink.dto;

import com.golflink.golflink.domain.District;
import lombok.Getter;

@Getter
public class DistrictDto {
    private final Long districtId;
    private final String name;

    // District 엔티티를 받아서 DistrictDto를 생성하는 생성자
    public DistrictDto(District district) {
        this.districtId = district.getDistrictId();
        this.name = district.getName();
    }
}
package com.golflink.golflink.dto;

import com.golflink.golflink.domain.SubDistrict;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubDistrictDto {
    private final Long subDistrictId;
    private final String name;

    public SubDistrictDto(SubDistrict subDistrict) {
        this.subDistrictId = subDistrict.getSubDistrictId();
        this.name = subDistrict.getName();
    }
}

package com.golflink.golflink.dto.adminDto;

import com.golflink.golflink.domain.Professional;
import lombok.Getter;

@Getter
public class AdminProListDto {
    private final Long proId;
    private final String name;
    private final Integer price;
    private final String packageInfo;

    public AdminProListDto(Professional professional) {
        this.proId = professional.getProId();
        this.name = professional.getName();
        this.price = professional.getPrice();
        this.packageInfo = professional.getPackageInfo();
    }
}
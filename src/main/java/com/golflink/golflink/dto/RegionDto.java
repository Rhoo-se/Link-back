package com.golflink.golflink.dto;

import com.golflink.golflink.domain.Region;
import lombok.Getter;

@Getter
public class RegionDto {
    private final Long regionId;
    private final String name;

    public RegionDto(Region region) {
        this.regionId = region.getRegionId();
        this.name = region.getName();
    }
}
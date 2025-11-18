package com.golflink.golflink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingRequestDto {
    private String name;
    private String phoneNumber;
    private String age;
    private String experience;
    private String score;
    private String gender;
    private Long subDistrictId;
    private String subDistrictName;
    private String preferredPlay;
    private String preferredBrand;
    private String requests;
    private String inflowPath;

    // JSON 문자열로 변환된 일정 정보
    private String schedule;
}

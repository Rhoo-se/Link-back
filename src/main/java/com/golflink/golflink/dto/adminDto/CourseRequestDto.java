package com.golflink.golflink.dto.adminDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 프론트엔드에서 골프장 '생성' 또는 '수정' 요청 시 사용하는 DTO
@Getter
@NoArgsConstructor // JSON -> Java 객체 변환을 위해 기본 생성자가 필요합니다.
public class CourseRequestDto {

    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String parkingInfo;
    private String screenInfo;
    private Long districtId; // 어느 지역 그룹에 속하는지에 대한 ID
}

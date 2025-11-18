package com.golflink.golflink.dto.adminDto;

import com.golflink.golflink.domain.GolfCourse;
import lombok.Getter;

// 골프장 상세 정보 페이지에 사용될 DTO (위치 정보 포함)
@Getter
public class AdminCourseDetailDto {
    private final Long courseId;
    private final String name;
    private final String address;
    private final String parkingInfo;
    private final String screenInfo;
    private final Long districtId;
    private final String districtName;
    private final Double latitude;   // [추가] 지도 표시를 위한 위도
    private final Double longitude;  // [추가] 지도 표시를 위한 경도

    public AdminCourseDetailDto(GolfCourse course) {
        this.courseId = course.getCourseId();
        this.name = course.getName();
        this.address = course.getAddress();
        this.parkingInfo = course.getParkingInfo();
        this.screenInfo = course.getScreenInfo();

        // [추가] 위도, 경도 정보를 Entity로부터 매핑합니다.
        this.latitude = course.getLatitude();
        this.longitude = course.getLongitude();

        if (course.getDistrict() != null) {
            this.districtId = course.getDistrict().getDistrictId();
            this.districtName = course.getDistrict().getName();
        } else {
            this.districtId = null;
            this.districtName = "소속 지역 없음";
        }
    }
}


package com.golflink.golflink.dto; // dto 패키지 생성 추천

import com.golflink.golflink.domain.GolfCourse;
import com.golflink.golflink.domain.Professional;
import lombok.Getter;

@Getter
public class CourseInfoResponseDto {

    private final Long courseId;
    private final String courseName;
    private final int representativePrice;
    private final String representativePackageInfo;
    private final String imageUrl; // 골프장 대표 이미지 URL
    private final Double latitude;
    private final Double longitude;
    private final String parkingInfo;
    private final String screenInfo;
    private final String representativeProName;
    private final String representativePhrase;


    public CourseInfoResponseDto(GolfCourse course, Professional representativePro) {
        this.courseId = course.getCourseId();
        this.courseName = course.getName();
        this.latitude = course.getLatitude();
        this.longitude = course.getLongitude();
        this.parkingInfo = course.getParkingInfo();
        this.screenInfo =  course.getScreenInfo();

        if (representativePro != null) {
            this.representativePrice = representativePro.getPrice(); // Professional 엔티티에 price 필드가 있다고 가정
            this.representativePackageInfo = representativePro.getPackageInfo(); // Professional 엔티티에 packageInfo 필드가 있다고 가정
            this.representativeProName = representativePro.getName();
            this.representativePhrase = representativePro.getName();
        } else {
            // 소속된 프로가 없는 경우 기본값 설정
            this.representativePrice = 0;
            this.representativePackageInfo = "레슨 정보가 없습니다.";
            this.representativeProName = null;
            this.representativePhrase = null;
        }


        this.imageUrl = null;
    }
}
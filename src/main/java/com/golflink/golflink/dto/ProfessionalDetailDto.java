package com.golflink.golflink.dto;

import com.golflink.golflink.domain.GolfCourse;
import com.golflink.golflink.domain.Professional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessionalDetailDto {
    private final Long proId;
    private final String name;
    private final String profilePicUrl;
    private final int price;
    private final String packageInfo;
    private final String phrase;
    private final String specialty;

    private final CourseInfo course; // 중첩 객체로 골프장 상세 정보 포함

    // Professional 엔티티를 받아 DTO를 생성하는 생성자
    public ProfessionalDetailDto(Professional professional) {
        this.proId = professional.getProId();
        this.name = professional.getName();
        this.profilePicUrl = professional.getProfilePicUrl();
        this.price = professional.getPrice();
        this.packageInfo = professional.getPackageInfo();
        this.phrase = professional.getPhrase();
        this.specialty = professional.getSpecialty(); // [추가]

        // 연관된 GolfCourse 정보가 있을 경우에만 CourseInfo 객체 생성
        if (professional.getGolfCourse() != null) {
            this.course = new CourseInfo(professional.getGolfCourse());
        } else {
            this.course = null;
        }
    }

    // 골프장 상세 정보를 담을 내부 클래스
    @Getter
    private static class CourseInfo {
        private final Long courseId;
        private final String name;
        private final String address;
        private final String parkingInfo;
        private final String screenInfo;
        private final Double latitude;
        private final Double longitude;

        public CourseInfo(GolfCourse golfCourse) {
            this.courseId = golfCourse.getCourseId();
            this.name = golfCourse.getName();
            this.address = golfCourse.getAddress();
            this.parkingInfo = golfCourse.getParkingInfo();
            this.screenInfo = golfCourse.getScreenInfo();
            this.latitude = golfCourse.getLatitude();
            this.longitude = golfCourse.getLongitude();
        }
    }
}

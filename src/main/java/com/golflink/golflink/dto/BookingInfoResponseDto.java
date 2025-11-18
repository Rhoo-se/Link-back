package com.golflink.golflink.dto;

import com.golflink.golflink.domain.GolfCourse;
import com.golflink.golflink.domain.Professional;
import lombok.Getter;

@Getter
public class BookingInfoResponseDto {

    // --- 프로 정보 ---
    private final Long proId;
    private final String proName;
    private final String profilePicUrl;

    // --- 구장 정보 ---
    private final Long courseId;
    private final String courseName;
    private final String courseAddress;

    // --- 레슨 정보 ---
    private final int price;
    private final String packageInfo;

    /**
     * Service 계층에서 DB로부터 조회한 Professional 엔티티를 받아
     * BookingPage에 필요한 데이터만으로 가공하는 생성자입니다.
     */
    public BookingInfoResponseDto(Professional professional) {
        // 프로 정보 매핑
        this.proId = professional.getProId();
        this.proName = professional.getName();
        this.profilePicUrl = professional.getProfilePicUrl();

        // 레슨 정보 매핑
        this.price = professional.getPrice();
        this.packageInfo = professional.getPackageInfo();

        // 프로와 연관된 구장 정보 매핑 (Null-safe)
        GolfCourse course = professional.getGolfCourse();
        if (course != null) {
            this.courseId = course.getCourseId();
            this.courseName = course.getName();
            this.courseAddress = course.getAddress();
        } else {
            // 소속 구장이 없는 경우에 대한 예외 처리
            this.courseId = null;
            this.courseName = "소속 없음";
            this.courseAddress = "";
        }
    }
}
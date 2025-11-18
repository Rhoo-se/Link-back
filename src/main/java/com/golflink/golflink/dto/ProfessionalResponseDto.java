package com.golflink.golflink.dto;

import com.golflink.golflink.domain.Professional;
import lombok.Getter;

@Getter
public class ProfessionalResponseDto {
    private final Long proId;
    private final String name;
    private final String profilePicUrl;
    private final int price;
    private final String packageInfo;
    private final String phrase;
    private final String parkingInfo;
    private final String screenInfo;

    // [변경] CourseInfo 객체 대신, 구장 정보나 활동 지역 상태를 모두 담을 수 있는 문자열 필드 사용
    private final String affiliation;

    // [핵심] Service 계층에서 사용할 새로운 생성자
    // 이 생성자는 Professional 엔티티와 함께, '소속 정보'를 나타낼 문자열을 직접 받아옵니다.
    public ProfessionalResponseDto(Professional professional, String affiliation) {
        this.proId = professional.getProId();
        this.name = professional.getName();
        this.profilePicUrl = professional.getProfilePicUrl();
        this.price = professional.getPrice();
        this.packageInfo = professional.getPackageInfo();
        this.phrase = professional.getPhrase();
        this.affiliation = affiliation; // 외부에서 받은 affiliation 문자열을 그대로 사용

        if (professional.getGolfCourse() != null) {
            this.parkingInfo = professional.getGolfCourse().getParkingInfo();
            this.screenInfo = professional.getGolfCourse().getScreenInfo();
        } else {
            // 프로가 소속된 구장이 없을 경우를 대비한 기본값
            this.parkingInfo = null;
            this.screenInfo = null;
        }
    }
}

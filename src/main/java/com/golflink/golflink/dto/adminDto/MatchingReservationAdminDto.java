package com.golflink.golflink.dto.adminDto;

import com.golflink.golflink.domain.MatchingReservation;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MatchingReservationAdminDto {
    private Long matchingId;
    private String name;
    private String phoneNumber;
    private String subDistrictName; // 세부 지역 이름

    // --- ⬇️ 누락된 필드들 추가 ⬇️ ---
    private String age;
    private String gender;
    private String experience;
    private String score;
    private String preferredPlay;
    private String preferredBrand;
    private String requests;
    private String schedule; // JSON 문자열 형태의 스케줄
    private String inflowPath;

    private String status;
    private LocalDateTime createdAt;

    // 엔티티를 DTO로 변환하는 생성자
    public MatchingReservationAdminDto(MatchingReservation entity) {
        this.matchingId = entity.getMatchingId();
        this.name = entity.getName();
        this.phoneNumber = entity.getPhoneNumber();

        if (entity.getSubDistrictName() != null && !entity.getSubDistrictName().isEmpty()) {
            // 관리자가 알아보기 쉽도록 '(직접입력)' 텍스트를 추가해줍니다.
            this.subDistrictName = entity.getSubDistrictName() + " (직접입력)";
        }
        // 2. 직접 입력된 값이 없다면, 연결된 세부 지역 이름을 사용합니다.
        else if (entity.getSubDistrict() != null) {
            this.subDistrictName = entity.getSubDistrict().getName();
        }
        // 3. 두 경우 모두 해당하지 않으면 기본값을 설정합니다.
        else {
            this.subDistrictName = "정보 없음";
        }

        // --- ⬇️ 추가된 필드 매핑 ⬇️ ---
        this.age = entity.getAge();
        this.gender = entity.getGender();
        this.experience = entity.getExperience();
        this.score = entity.getScore();
        this.preferredPlay = entity.getPreferredPlay();
        this.schedule = entity.getSchedule();
        this.preferredBrand = entity.getPreferredBrand();
        this.requests = entity.getRequests();
        this.status = entity.getStatus();
        this.inflowPath = entity.getInflowPath();
        this.createdAt = entity.getCreatedAt();
    }
}
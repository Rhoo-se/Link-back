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


    private final String affiliation;


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
            this.parkingInfo = null;
            this.screenInfo = null;
        }
    }
}

package com.golflink.golflink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequestDto {
    private String userName;
    private String phoneNumber;
    private String experience;
    private String swingCount;
    private String coachingPart;
    private String channel;
    private String reservationDate;
    private String reservationTime;
    private Long proId;
}
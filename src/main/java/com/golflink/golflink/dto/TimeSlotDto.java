package com.golflink.golflink.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 추가
public class TimeSlotDto {
    private String time;
    private String status; // "available", "booked", "blocked"
    private String userName; // 예약한 사용자 이름
    private Long reservationId; // 예약을 취소할 때 필요한 ID
    private String coachingPart;
    private String proName;       // 예약된 강사 이름
    private String channel;       // 고객 유입 경로
    private Long blockedSlotId;
    private Long proId;
}
package com.golflink.golflink.dto;

import com.golflink.golflink.domain.Reservation;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ReservationResponseDto {

    private final Long reservationId;
    private final String proName; // pro_id 대신 프로 이름
    private final String userName;
    private final String phoneNumber;
    private final LocalDate reservationDate;
    private final String reservationTime;
    private final String status;
    private final String channel;
    private final LocalDateTime createdAt;
    private final String experience;
    private final String swing_count;

    // Reservation 엔티티를 DTO로 변환하는 생성자
    public ReservationResponseDto(Reservation reservation) {
        this.reservationId = reservation.getReservationId();
        // Professional 엔티티에 getName() 메서드가 있다고 가정
        this.proName = reservation.getPro().getName();
        this.userName = reservation.getUserName();
        this.phoneNumber = reservation.getPhoneNumber();
        this.reservationDate = reservation.getReservationDate();
        this.reservationTime = reservation.getReservationTime();
        this.status = reservation.getStatus();
        this.channel = reservation.getChannel();
        this.createdAt = reservation.getCreatedAt();
        this.experience = reservation.getExperience();
        this.swing_count = reservation.getSwingCount();
    }
}
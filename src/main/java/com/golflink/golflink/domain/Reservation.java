package com.golflink.golflink.domain;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "pro_id") // DB의 pro_id 컬럼과 매핑
    private Professional pro;
    private String userName;
    private String phoneNumber;
    private String experience;
    private String swingCount;
    private String coachingPart;
    private LocalDate reservationDate;
    private String reservationTime;
    private String status;
    private String channel;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Getter, Setter, 생성자 등은 Lombok 어노테이션(@Getter, @Setter 등)이나 직접 생성
}
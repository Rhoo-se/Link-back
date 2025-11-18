package com.golflink.golflink.domain;

import com.golflink.golflink.domain.SubDistrict;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "matching_reservations")
@Getter
@Setter
@NoArgsConstructor // JPA는 기본 생성자를 필요로 합니다.
public class MatchingReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long matchingId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "age")
    private String age;

    @Column(name = "experience")
    private String experience;

    @Column(name = "score")
    private String score;

    @Column(name = "gender")
    private String gender;

    @Column(name = "preferred_play")
    private String preferredPlay;

    @Column(name = "inflow_path")
    private String inflowPath;

    @Column(name = "schedule", columnDefinition = "TEXT")
    private String schedule;

    @Column(name = "status")
    private String status = "PENDING"; // 기본 상태를 'PENDING'으로 설정

    @Column(name = "sub_district_name")
    private String subDistrictName;

    @Column(name = "preferred_brand")
    private String preferredBrand;

    @Column(name = "requests")
    private String requests;

    @CreationTimestamp // 데이터 생성 시 자동으로 현재 시간이 기록됩니다.
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // SubDistrict와의 N:1 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_district_id")
    private SubDistrict subDistrict;


}
package com.golflink.golflink.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "golf_courses")
public class GolfCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String parkingInfo;
    private String screenInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id") // DB의 district_id 컬럼과 연결됩니다.
    private District district;
    // [추가] Professional 엔티티와 1:N 관계를 맺습니다.
    // 이 관계는 나중에 골프장에 소속된 프로 목록을 가져올 때 필요합니다.
    @JsonManagedReference
    @OneToMany(mappedBy = "golfCourse")
    private List<Professional> professionals = new ArrayList<>();


    public void update(String name, String address, Double latitude, Double longitude, String parkingInfo, String screenInfo) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingInfo = parkingInfo;
        this.screenInfo = screenInfo;
    }
}
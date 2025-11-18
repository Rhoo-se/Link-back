package com.golflink.golflink.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "professionals")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proId;

    private String name;

    private String profilePicUrl;

    private Integer price;

    private String packageInfo;

    private String phrase;

    private String specialty;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id") // DB의 외래 키 컬럼 이름
    private GolfCourse golfCourse;

    @ManyToMany
    @JoinTable(
            name = "district_professionals",
            joinColumns = @JoinColumn(name = "pro_id"),
            inverseJoinColumns = @JoinColumn(name = "district_id")
    )
    private Set<District> activityDistricts = new HashSet<>();


    public Professional(String name, String profilePicUrl, Integer price, String packageInfo, String phrase, String specialty, GolfCourse golfCourse) {
        this.name = name;
        this.profilePicUrl = profilePicUrl;
        this.price = price;
        this.packageInfo = packageInfo;
        this.phrase = phrase;
        this.specialty = specialty;
        this.golfCourse = golfCourse;
    }

    // 수정을 위한 비즈니스 메소드
    public void update(String name, String profilePicUrl, Integer price, String packageInfo, String phrase, String specialty) {
        this.name = name;
        this.profilePicUrl = profilePicUrl;
        this.price = price;
        this.packageInfo = packageInfo;
        this.phrase = phrase;
        this.specialty = specialty;
    }


}
package com.golflink.golflink.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "districts")
@Getter
@Setter
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Long districtId;

    private String name;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @JsonManagedReference
    @OneToMany(mappedBy = "district")
    private List<GolfCourse> golfCourses = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "district")
    private List<SubDistrict> subDistricts = new ArrayList<>();

    @ManyToMany(mappedBy = "activityDistricts")
    private Set<Professional> professionals = new HashSet<>();
}
package com.golflink.golflink.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.golflink.golflink.domain.District;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regions")
@Getter
@Setter
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    private String name;

    //District와의 1:N 관계 설정
    @JsonManagedReference
    @OneToMany(mappedBy = "region")
    private List<District> districts = new ArrayList<>();
}
package com.golflink.golflink.repository;

import com.golflink.golflink.domain.SubDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubDistrictRepository extends JpaRepository<SubDistrict, Long> {
    List<SubDistrict> findByDistrict_DistrictId(Long districtId);
}

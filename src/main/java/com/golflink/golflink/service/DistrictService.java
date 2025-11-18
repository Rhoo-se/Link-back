package com.golflink.golflink.service;

import com.golflink.golflink.domain.District;
import com.golflink.golflink.domain.SubDistrict;
import com.golflink.golflink.dto.DistrictDto;
import com.golflink.golflink.dto.SubDistrictDto;
import com.golflink.golflink.repository.DistrictRepository;
import com.golflink.golflink.repository.SubDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final SubDistrictRepository subDistrictRepository;

    public List<DistrictDto> findAll() {
        List<District> districts = districtRepository.findAll();

        // 엔티티 리스트를 DTO 리스트로 변환하여 반환
        return districts.stream()
                .map(DistrictDto::new) // .map(district -> new DistrictDto(district)) 와 동일
                .collect(Collectors.toList());
    }
    public List<DistrictDto> findByRegionId(Long regionId) {
        List<District> districts = districtRepository.findByRegion_RegionId(regionId);
        // 엔티티 리스트를 DTO 리스트로 변환하여 반환합니다.
        return districts.stream()
                .map(DistrictDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SubDistrictDto> findSubDistrictsByDistrictId(Long districtId){
        List<SubDistrict> subDistricts = subDistrictRepository.findByDistrict_DistrictId(districtId);


        return subDistricts.stream()
                .map(SubDistrictDto::new)
                .collect(Collectors.toList());
    }
}
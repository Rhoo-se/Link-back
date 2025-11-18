package com.golflink.golflink.service;

import com.golflink.golflink.domain.Region;
import com.golflink.golflink.dto.RegionDto;
import com.golflink.golflink.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 데이터를 읽기만 하므로 readOnly = true 설정
public class RegionService {
    private final RegionRepository regionRepository;

    // [수정] 반환 타입을 List<RegionDto>로 변경
    public List<RegionDto> findAll() {
        List<Region> regions = regionRepository.findAll();
        // Region 엔티티 목록을 RegionDto 목록으로 변환
        return regions.stream()
                .map(RegionDto::new)
                .collect(Collectors.toList());
    }
}
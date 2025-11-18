package com.golflink.golflink.controller;


import com.golflink.golflink.dto.MatchingRequestDto;
import com.golflink.golflink.dto.SubDistrictDto;
import com.golflink.golflink.dto.adminDto.MatchingReservationAdminDto;
import com.golflink.golflink.service.DistrictService;
import com.golflink.golflink.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api") // 공통 경로를 /api로 설정
public class MatchingController {

    private final DistrictService districtService;
    private final MatchingService matchingService;

    @GetMapping("/sub-districts")
    public ResponseEntity<List<SubDistrictDto>> getSubDistrictsByDistrict(@RequestParam Long districtId) {
        List<SubDistrictDto> subDistrictDtos = districtService.findSubDistrictsByDistrictId(districtId);
        return ResponseEntity.ok(subDistrictDtos);
    }

    @PostMapping("/matching-reservations")
    public ResponseEntity<Void> createMatchingRequest(@RequestBody MatchingRequestDto requestDto) {
        matchingService.createRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/matching-reservations")
    public ResponseEntity<List<MatchingReservationAdminDto>> getMatchingReservations() {
        List<MatchingReservationAdminDto> requests = matchingService.findAllForAdmin();
        return ResponseEntity.ok(requests);
    }
}

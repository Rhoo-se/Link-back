package com.golflink.golflink.service;

import com.golflink.golflink.domain.MatchingReservation;
import com.golflink.golflink.domain.SubDistrict;
import com.golflink.golflink.dto.MatchingRequestDto;
import com.golflink.golflink.dto.adminDto.MatchingReservationAdminDto;
import com.golflink.golflink.repository.MatchingRepository;
import com.golflink.golflink.repository.SubDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final SubDistrictRepository subDistrictRepository; // SubDistrict를 찾기 위해 필요

    /**
     * 클라이언트로부터 받은 매칭 신청 정보를 DB에 저장합니다.
     * @param requestDto 매칭 신청 정보가 담긴 DTO
     */
    // MatchingService.java

    @Transactional
    public void createRequest(MatchingRequestDto requestDto) {
        MatchingReservation newRequest = new MatchingReservation();

        // --- ⬇️ 이 부분이 수정되었습니다 ⬇️ ---

        // 경우 1: '기타'를 선택하여 subDistrictName이 넘어온 경우
        if (requestDto.getSubDistrictName() != null && !requestDto.getSubDistrictName().isEmpty()) {
            newRequest.setSubDistrictName(requestDto.getSubDistrictName());
            // 이 경우, subDistrict 외래 키는 null이 됩니다.
        }
        // 경우 2: 기존 지역을 선택하여 subDistrictId가 넘어온 경우
        else if (requestDto.getSubDistrictId() != null) {
            SubDistrict subDistrict = subDistrictRepository.findById(requestDto.getSubDistrictId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 세부 지역을 찾을 수 없습니다. ID: " + requestDto.getSubDistrictId()));
            // 관계 설정
            newRequest.setSubDistrict(subDistrict);
        }
        // 경우 3: 두 값 모두 없는 경우 (오류 처리)
        else {
            throw new IllegalArgumentException("세부 지역 정보가 누락되었습니다.");
        }

        // --- ⬆️ 여기까지 수정 ⬆️ ---

        // 나머지 정보는 동일하게 매핑합니다.
        newRequest.setName(requestDto.getName());
        newRequest.setPhoneNumber(requestDto.getPhoneNumber());
        newRequest.setAge(requestDto.getAge());
        newRequest.setExperience(requestDto.getExperience());
        newRequest.setScore(requestDto.getScore());
        newRequest.setGender(requestDto.getGender());
        newRequest.setPreferredPlay(requestDto.getPreferredPlay());
        newRequest.setPreferredBrand(requestDto.getPreferredBrand());
        newRequest.setRequests(requestDto.getRequests());
        newRequest.setInflowPath(requestDto.getInflowPath());
        newRequest.setSchedule(requestDto.getSchedule());

        // DB에 저장
        matchingRepository.save(newRequest);
    }

    @Transactional(readOnly = true)
    public List<MatchingReservationAdminDto> findAllForAdmin() {
        return matchingRepository.findAllByOrderByMatchingIdDesc().stream()
                .map(MatchingReservationAdminDto::new) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }
}
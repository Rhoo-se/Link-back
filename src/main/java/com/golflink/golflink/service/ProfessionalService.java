package com.golflink.golflink.service;

import com.golflink.golflink.domain.Professional;
import com.golflink.golflink.dto.ProfessionalDetailDto;
import com.golflink.golflink.dto.ProfessionalResponseDto;
import com.golflink.golflink.repository.ProfessionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    // [수정] ID 타입을 Long으로 변경합니다.
    public List<Professional> getProfessionalsByCourse(Long courseId) {
        return professionalRepository.findByGolfCourse_CourseId(courseId);
    }

    public ProfessionalDetailDto findProfessionalDetail(Long proId) {
        Professional professional = professionalRepository.findByIdWithGolfCourse(proId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로를 찾을 수 없습니다: " + proId));

        // 조회한 엔티티를 DTO로 변환하여 반환
        return new ProfessionalDetailDto(professional);



    }



    public List<ProfessionalResponseDto> findProfessionalsForListPage(Long districtId) {
        // 결과를 담을 DTO 맵. Key는 프로 ID, Value는 최종 DTO
        // Map을 사용해서 중복을 자연스럽게 제거합니다.
        Map<Long, ProfessionalResponseDto> professionalDtoMap = new HashMap<>();

        // 1. '주 소속' 프로 목록 조회
        List<Professional> primaryPros = professionalRepository.findPrimaryByDistrictId(districtId);

        // 2. '주 소속' 프로들을 DTO로 변환하여 맵에 먼저 추가
        for (Professional pro : primaryPros) {
            // 이 프로의 소속은 '실제 구장 이름'입니다.
            String affiliation = pro.getGolfCourse().getName();
            professionalDtoMap.put(pro.getProId(), new ProfessionalResponseDto(pro, affiliation));
        }

        // 3. '추가 활동 지역' 프로 목록 조회
        List<Professional> additionalPros = professionalRepository.findByActivityDistricts_DistrictId(districtId);

        // 4. '추가 활동 지역' 프로들을 DTO로 변환 (맵에 없는 프로만 추가)
        for (Professional pro : additionalPros) {
            // 이미 맵에 있다면, 그 프로는 '주 소속'이므로 더 정확한 정보가 이미 담겨있음.
            // 따라서 맵에 없는 경우에만 추가합니다.
            if (!professionalDtoMap.containsKey(pro.getProId())) {
                // 이 프로의 소속은 '활동 가능 지역' 텍스트입니다.
                // (필요하다면 District 이름을 파라미터로 받아와서 넣어줄 수도 있습니다)
                String affiliation = pro.getGolfCourse().getName();
                professionalDtoMap.put(pro.getProId(), new ProfessionalResponseDto(pro, affiliation));
            }
        }

        // 5. 맵의 값들(DTO)을 리스트로 변환하여 최종 반환
        return new ArrayList<>(professionalDtoMap.values());
    }
}
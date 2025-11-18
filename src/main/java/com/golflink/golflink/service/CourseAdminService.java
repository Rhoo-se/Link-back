package com.golflink.golflink.service;

import com.golflink.golflink.domain.GolfCourse;
import com.golflink.golflink.domain.Professional;
import com.golflink.golflink.dto.adminDto.AdminCourseDetailDto;
import com.golflink.golflink.dto.adminDto.AdminProListDto;
import com.golflink.golflink.repository.GolfCourseRepository;
import com.golflink.golflink.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseAdminService {

    private final GolfCourseRepository golfCourseRepository;
    private final ProfessionalRepository professionalRepository;

    // 특정 골프장 ID로 상세 정보를 찾아 DTO로 반환
    public AdminCourseDetailDto findCourseDetailsById(Long courseId) {
        GolfCourse course = golfCourseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 골프장을 찾을 수 없습니다: " + courseId));
        return new AdminCourseDetailDto(course);
    }

    // 특정 골프장 ID에 소속된 강사 목록을 찾아 DTO 리스트로 반환
    public List<AdminProListDto> findProfessionalsByCourse(Long courseId) {
        List<Professional> professionals = professionalRepository.findByGolfCourse_CourseId(courseId);

        return professionals.stream()
                .map(AdminProListDto::new)
                .collect(Collectors.toList());
    }
}
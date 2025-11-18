package com.golflink.golflink.controller; // 패키지명은 본인 프로젝트에 맞게 확인해주세요.

import com.golflink.golflink.domain.District;
import com.golflink.golflink.domain.GolfCourse;
import com.golflink.golflink.domain.Professional;
import com.golflink.golflink.dto.DistrictDto;
import com.golflink.golflink.dto.ProfessionalDetailDto;
import com.golflink.golflink.dto.ProfessionalResponseDto;
import com.golflink.golflink.service.DistrictService;
import com.golflink.golflink.service.GolfCourseService;
import com.golflink.golflink.service.ProfessionalService;
import com.golflink.golflink.dto.CourseInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // 공통 경로를 /api로 설정
@RequiredArgsConstructor
public class GolfCourseController {

    // 서비스 주입
    private final DistrictService districtService;
    private final GolfCourseService golfCourseService;
    private final ProfessionalService professionalService;

    /**
     * HomePage용: 모든 지역 그룹 목록 조회
     */
    @GetMapping("/districts")
    public ResponseEntity<List<DistrictDto>> getAllDistricts() {
        List<DistrictDto> districts = districtService.findAll();
        return ResponseEntity.ok(districts);
    }

    /**
     * ListPage용: 특정 지역 그룹에 속한 골프장 목록 조회 (대표 가격/설명 포함)
     */
    @GetMapping("/courses")
    public ResponseEntity<List<CourseInfoResponseDto>> getCoursesByDistrict(@RequestParam Long districtId) {
        List<CourseInfoResponseDto> courses = golfCourseService.findCoursesByDistrictWithRepresentativeInfo(districtId);
        return ResponseEntity.ok(courses);
    }

    /**
     * DetailPage용: 특정 골프장의 상세 정보 조회
     */
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<GolfCourse> getCourseDetails(@PathVariable Long courseId) {
        GolfCourse course = golfCourseService.getCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    /**
     * DetailPage용: 특정 골프장에 소속된 프로 강사 목록 조회
     */
    @GetMapping("/professionals")
    public ResponseEntity<List<Professional>> getProfessionalsByCourse(@RequestParam Long courseId) {
        List<Professional> professionals = professionalService.getProfessionalsByCourse(courseId);
        return ResponseEntity.ok(professionals);
    }

    @GetMapping("/professionals/district")
    public ResponseEntity<List<ProfessionalResponseDto>> getProfessionalsByDistrict(@RequestParam Long districtId) {
        List<ProfessionalResponseDto> professionals = professionalService.findProfessionalsForListPage(districtId);
        return ResponseEntity.ok(professionals);
    }

    @GetMapping("/professionals/detail/{proId}")
    public ResponseEntity<ProfessionalDetailDto> getProfessionalDetail(@PathVariable Long proId) {
        ProfessionalDetailDto professionalDetail = professionalService.findProfessionalDetail(proId);
        return ResponseEntity.ok(professionalDetail);
    }
}
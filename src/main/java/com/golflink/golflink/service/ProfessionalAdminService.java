package com.golflink.golflink.service;

import com.golflink.golflink.domain.GolfCourse;
import com.golflink.golflink.domain.Professional;
import com.golflink.golflink.dto.adminDto.ProRequestDto;
import com.golflink.golflink.repository.GolfCourseRepository;
import com.golflink.golflink.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfessionalAdminService {

    private final ProfessionalRepository professionalRepository;
    private final GolfCourseRepository golfCourseRepository;
    private final S3UploadService s3UploadService; // S3 서비스 주입

    @Transactional
    public Long createProfessional(ProRequestDto requestDto, MultipartFile imageFile) throws IOException {
        GolfCourse golfCourse = golfCourseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 골프장입니다."));

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            // S3에 "professionals/{courseId}" 디렉토리 경로로 업로드합니다.
            imageUrl = s3UploadService.upload(imageFile, "professionals/" + golfCourse.getCourseId());
        }

        Professional newPro = new Professional(
                requestDto.getName(),
                imageUrl, // S3에서 받은 URL 저장
                requestDto.getPrice(),
                requestDto.getPackageInfo(),
                requestDto.getPhrase(),
                requestDto.getSpecialty(),
                golfCourse
        );

        Professional savedPro = professionalRepository.save(newPro);
        return savedPro.getProId();
    }

    @Transactional
    public void updateProfessional(Long proId, ProRequestDto requestDto, MultipartFile imageFile) throws IOException {
        Professional proToUpdate = professionalRepository.findById(proId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강사입니다. ID: " + proId));
        String oldImageUrl = proToUpdate.getProfilePicUrl();
        String newImageUrl = oldImageUrl;

        if (imageFile != null && !imageFile.isEmpty()) {
            // 새 이미지가 있으면, 기존 이미지를 S3에서 삭제합니다.
            if (oldImageUrl != null) {
                s3UploadService.deleteFile(oldImageUrl);
            }
            // 새 이미지를 S3에 업로드합니다.
            newImageUrl = s3UploadService.upload(imageFile, "professionals/" + proToUpdate.getGolfCourse().getCourseId());
        }

        proToUpdate.update(
                requestDto.getName(),
                newImageUrl,
                requestDto.getPrice(),
                requestDto.getPackageInfo(),
                requestDto.getPhrase(),
                requestDto.getSpecialty()
        );
    }

    @Transactional
    public void deleteProfessional(Long proId) {
        Professional proToDelete = professionalRepository.findById(proId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강사입니다."));

        // S3에서 이미지 파일을 먼저 삭제합니다.
        if (proToDelete.getProfilePicUrl() != null) {
            s3UploadService.deleteFile(proToDelete.getProfilePicUrl());
        }

        // DB에서 강사 정보를 삭제합니다.
        professionalRepository.delete(proToDelete);
    }
}

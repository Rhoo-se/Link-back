package com.golflink.golflink.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일을 S3에 업로드하고, 저장된 파일의 전체 URL을 반환합니다.
     * @param multipartFile 업로드할 파일
     * @param dirName S3 버킷 내에 파일을 저장할 디렉토리 이름 (예: "professionals/12")
     * @return 업로드된 파일의 전체 URL
     * @throws IOException 파일 처리 중 오류 발생 시
     */
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // 1. 파일 이름이 겹치지 않도록 UUID를 사용해 고유한 이름을 생성합니다.
        String originalFilename = multipartFile.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        String s3Filename = dirName + "/" + uniqueFilename;

        // 2. 파일 메타데이터를 설정합니다. (파일 크기, 타입 등)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // 3. S3에 파일을 업로드합니다.
        amazonS3Client.putObject(bucket, s3Filename, multipartFile.getInputStream(), metadata);

        // 4. 업로드된 파일의 URL을 반환하여 DB에 저장할 수 있도록 합니다.
        return amazonS3Client.getUrl(bucket, s3Filename).toString();
    }

    /**
     * S3에서 기존 파일을 삭제합니다.
     * @param fileUrl DB에 저장된 파일의 전체 URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        try {
            // 전체 URL에서 파일 키(버킷 내 경로 + 파일 이름)를 추출합니다.
            // 예: "https://.../professionals/12/uuid_image.jpg" -> "professionals/12/uuid_image.jpg"
            String fileKey = URLDecoder.decode(fileUrl.substring(fileUrl.indexOf(bucket) + bucket.length() + 1), StandardCharsets.UTF_8);
            amazonS3Client.deleteObject(bucket, fileKey);
        } catch (Exception e) {
            // 파일 삭제 실패는 서비스의 핵심 로직에 큰 영향을 주지 않으므로, 에러 로그만 남기고 넘어가는 것이 좋습니다.
            System.err.println("S3 파일 삭제 실패: " + fileUrl + ", 원인: " + e.getMessage());
        }
    }
}


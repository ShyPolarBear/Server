package com.shy_polarbear.server.domain.images.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.shy_polarbear.server.domain.images.exception.ImageException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    String uploadToS3(MultipartFile uploadFile, String s3UploadFilePath) {
        String uploadImageUrl = putS3(uploadFile, s3UploadFilePath);

//        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(MultipartFile uploadFile, String s3UploadFilePath) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(uploadFile.getSize());
        metadata.setContentType(uploadFile.getContentType());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, s3UploadFilePath, uploadFile.getInputStream(), metadata);
            amazonS3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new ImageException(ExceptionStatus.FAIL_UPLOAD_IMAGES);
        }
        return amazonS3Client.getUrl(bucket, s3UploadFilePath).toString();
    }

    void removeNewFile(File targetFile) {
//        if(targetFile.delete()) {
//            log.info("파일이 삭제되었습니다.");
//        }else {
//            log.info("파일이 삭제되지 못했습니다.");
//        }
    }
}

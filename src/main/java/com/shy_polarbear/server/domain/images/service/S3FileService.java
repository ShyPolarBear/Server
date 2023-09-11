package com.shy_polarbear.server.domain.images.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.shy_polarbear.server.domain.images.exception.ImageException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class S3FileService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    String upload(MultipartFile uploadFile, String s3UploadFilePath) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(uploadFile.getSize());
        metadata.setContentType(uploadFile.getContentType());
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, s3UploadFilePath, uploadFile.getInputStream(), metadata);
            amazonS3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new ImageException(ExceptionStatus.FAIL_UPLOAD_IMAGES);
        }
        String s3UploadImageUrl = amazonS3Client.getUrl(bucket, s3UploadFilePath).toString();
        return s3UploadImageUrl;
    }

    void delete(String s3DeleteFilePath) {
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, s3DeleteFilePath));
        } catch (Exception e) {
            throw new ImageException(ExceptionStatus.FAIL_DELETE_IMAGES);
        }
    }
}

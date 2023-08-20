package com.shy_polarbear.server.domain.images.service;

import com.shy_polarbear.server.domain.images.dto.request.DeleteImageRequest;
import com.shy_polarbear.server.domain.images.dto.request.UpdateImageRequest;
import com.shy_polarbear.server.domain.images.dto.request.UploadImageRequest;
import com.shy_polarbear.server.domain.images.dto.response.DeleteImageResponse;
import com.shy_polarbear.server.domain.images.dto.response.UpdateImageResponse;
import com.shy_polarbear.server.domain.images.dto.response.UploadImageResponse;
import com.shy_polarbear.server.domain.images.exception.ImageException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

import static java.text.MessageFormat.format;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {


    private final S3FileService s3FileService;

    public UploadImageResponse upload(UploadImageRequest uploadImageRequest) {
        List<MultipartFile> multipartImageFiles = uploadImageRequest.getImageFiles();
        String fileType = uploadImageRequest.getType();
        List<String> s3UploadImageUrls = uploadImages(multipartImageFiles, fileType);
        return new UploadImageResponse(s3UploadImageUrls);
    }

    private List<String> uploadImages(List<MultipartFile> multipartImageFiles, String fileType) {
        if ((fileType.equals("profile") && !(multipartImageFiles.size() == 1))) {
            throw new ImageException(ExceptionStatus.INVALID_INPUT_VALUE);
        }
        List<String> s3UploadImageUrls = multipartImageFiles.stream()
                .map(multipartFile -> uploadImage(multipartFile, fileType))
                .toList();
        return s3UploadImageUrls;
    }

    private String uploadImage(MultipartFile multipartFile, String fileType) {
        //파일 타입에 따라 업로드 파일 경로 만들기
        String s3UploadFilePath = FilePathUtils.createS3UploadFilePath(multipartFile, fileType);
        //s3 업로드
        return s3FileService.upload(multipartFile, s3UploadFilePath);
    }


    public UpdateImageResponse update(UpdateImageRequest updateImageRequest) {
        String fileType = updateImageRequest.getType();
        List<MultipartFile> newImageFiles = updateImageRequest.getNewImageFiles();
        List<String> oldImageUrls = updateImageRequest.getOldImageUrls();
        //삭제
        deleteImages(oldImageUrls);
        //업로드
        List<String> s3UploadImageUrls = uploadImages(newImageFiles, fileType);
        return new UpdateImageResponse(s3UploadImageUrls);
    }

    public DeleteImageResponse delete(DeleteImageRequest deleteImageRequest) {
        List<String> imageUrls = deleteImageRequest.getImageUrls();
        deleteImages(imageUrls);
        return new DeleteImageResponse(imageUrls.size());
    }

    public void deleteImages(List<String> imageUrls) {
        imageUrls.stream().forEach((imageUrl) -> {
            String filePath = FilePathUtils.parseFilePathFromUrl(imageUrl);
            s3FileService.delete(filePath);
        });
    }

}

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
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

import static java.text.MessageFormat.format;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String FILE_SEPARATOR = "/";

    private final S3FileService s3FileService;

    public UploadImageResponse uploadImages(UploadImageRequest uploadImageRequest) {
        List<MultipartFile> multipartImageFiles = uploadImageRequest.getImageFiles();
        String fileType = uploadImageRequest.getType();
        if (multipartImageFiles == null || multipartImageFiles.size() > 5 || multipartImageFiles.isEmpty() || (fileType.equals("profile") && !(multipartImageFiles.size() == 1))) {
            throw new ImageException(ExceptionStatus.INVALID_IMAGE_COUNT);
        }

        List<String> s3UploadImageUrls = multipartImageFiles.stream()
                .map(multipartFile -> uploadImage(multipartFile, fileType))
                .toList();
        return new UploadImageResponse(s3UploadImageUrls);
    }

    private String uploadImage(MultipartFile multipartFile, String fileType) {
        //파일 타입에 따라 업로드 파일 경로 만들기
        String s3UploadFilePath = createS3UploadFilePath(multipartFile, fileType);
        //s3 업로드
        return s3FileService.upload(multipartFile, s3UploadFilePath);
    }

    // 파일경로  = {파일타입}/{UUID}_{유저파일이름}.{확장자}
    private String createS3UploadFilePath(MultipartFile multipartFile, String fileType) {
        String originalFilename = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);
        String fileDir = getFileDir(fileType);
        String fileName = createS3UploadFileName(originalFilename);
        return fileDir + FILE_SEPARATOR + fileName;
    }

    private String getFileDir(String fileType) {
        if (fileType.equals("profile")) {
            return S3FileDir.PROFILE.path;
        } else if (fileType.equals("feed")) {
            return S3FileDir.FEED.path;
        } else {
            throw new ImageException(ExceptionStatus.INVALID_IMAGE_TYPE);
        }
    }

    private String createS3UploadFileName(String originalFilename) {
        int pos = originalFilename.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String userFileName = originalFilename.substring(0, pos);
        String fileExtension = originalFilename.substring(pos + 1);
        return format("{0}_{1}.{2}", userFileName, UUID.randomUUID(), fileExtension);
    }

    public UpdateImageResponse updateImages(UpdateImageRequest updateImageRequest) {
        return null;
    }

    public DeleteImageResponse deleteImages(DeleteImageRequest deleteImageRequest) {
        return null;
    }
}

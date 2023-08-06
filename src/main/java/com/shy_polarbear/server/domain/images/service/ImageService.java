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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final S3Service s3Service;
    public UploadImageResponse uploadImages(UploadImageRequest uploadImageRequest) {
        List<MultipartFile> multipartImageFiles = uploadImageRequest.getImageFiles();
        //타입에 따라 파일 path 만들기
        //s3에 업로드

        return null;
    }



    public UpdateImageResponse updateImages(UpdateImageRequest updateImageRequest) {
        return null;
    }

    public DeleteImageResponse deleteImages(DeleteImageRequest deleteImageRequest) {
        return null;
    }
}

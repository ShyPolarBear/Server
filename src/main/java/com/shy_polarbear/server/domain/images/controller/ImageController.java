package com.shy_polarbear.server.domain.images.controller;

import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.shy_polarbear.server.domain.images.dto.request.DeleteImageRequest;
import com.shy_polarbear.server.domain.images.dto.request.UpdateImageRequest;
import com.shy_polarbear.server.domain.images.dto.request.UploadImageRequest;
import com.shy_polarbear.server.domain.images.dto.response.DeleteImageResponse;
import com.shy_polarbear.server.domain.images.dto.response.UpdateImageResponse;
import com.shy_polarbear.server.domain.images.exception.ImageException;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    //TODO: 실제 api 개발 시 ModelAttribute validation 필요
    @PostMapping
    public ApiResponse<UploadImageRequest> uploadImages(@ModelAttribute UploadImageRequest uploadImageRequest) {
        return null;
    }

    @PutMapping
    public ApiResponse<UpdateImageResponse> updateImages(@ModelAttribute UpdateImageRequest updateImageRequest) {
        return null;
    }

    @DeleteMapping
    public ApiResponse<DeleteImageResponse> deleteImage(@RequestBody DeleteImageRequest deleteImageRequest) {
        return null;
    }

    private static int checkUploadTypeAndCount(String type, List<MultipartFile> files) {
        //type 체크
        if ((type == null) || (!type.equals("feed") && !type.equals("profile"))) {
            throw new ImageException(ExceptionStatus.INVALID_IMAGE_TYPE);
        }

        //이미지 개수 체크
        if (files == null || files.size() > 5 || files.isEmpty() || (type.equals("profile") && !(files.size() == 1))) {
            throw new ImageException(ExceptionStatus.INVALID_IMAGE_COUNT);
        }
        return files.size();
    }
    private static List<String> checkDeleteImageCount(List<String> deleteImages) {
        if (deleteImages.isEmpty() || deleteImages.size() > 5) {
            throw new ImageException(ExceptionStatus.FAIL_DELETE_IMAGES);
        }
        return deleteImages;
    }

}

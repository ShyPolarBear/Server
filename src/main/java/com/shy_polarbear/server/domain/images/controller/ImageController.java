package com.shy_polarbear.server.domain.images.controller;

import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.shy_polarbear.server.domain.images.dto.request.DeleteImageRequest;
import com.shy_polarbear.server.domain.images.dto.request.UpdateImageRequest;
import com.shy_polarbear.server.domain.images.dto.request.UploadImageRequest;
import com.shy_polarbear.server.domain.images.dto.response.DeleteImageResponse;
import com.shy_polarbear.server.domain.images.dto.response.UpdateImageResponse;
import com.shy_polarbear.server.domain.images.dto.response.UploadImageResponse;
import com.shy_polarbear.server.domain.images.exception.ImageException;
import com.shy_polarbear.server.domain.images.service.ImageService;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ApiResponse<UploadImageResponse> uploadImages(@Valid @ModelAttribute UploadImageRequest uploadImageRequest) {
        return ApiResponse.success(imageService.upload(uploadImageRequest));
    }

    @PutMapping
    public ApiResponse<UpdateImageResponse> updateImages(@Valid @ModelAttribute UpdateImageRequest updateImageRequest) {
        return ApiResponse.success(imageService.update(updateImageRequest));
    }

    @DeleteMapping
    public ApiResponse<DeleteImageResponse> deleteImages(@Valid @RequestBody DeleteImageRequest deleteImageRequest) {
        return ApiResponse.success(imageService.delete(deleteImageRequest));
    }

}

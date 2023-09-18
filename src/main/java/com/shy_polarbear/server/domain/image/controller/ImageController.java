package com.shy_polarbear.server.domain.image.controller;

import com.shy_polarbear.server.domain.image.dto.request.DeleteImageRequest;
import com.shy_polarbear.server.domain.image.dto.request.UpdateImageRequest;
import com.shy_polarbear.server.domain.image.dto.request.UploadImageRequest;
import com.shy_polarbear.server.domain.image.dto.response.DeleteImageResponse;
import com.shy_polarbear.server.domain.image.dto.response.UpdateImageResponse;
import com.shy_polarbear.server.domain.image.dto.response.UploadImageResponse;
import com.shy_polarbear.server.domain.image.service.ImageService;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

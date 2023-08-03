package com.shy_polarbear.server.domain.images.controller;

import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.shy_polarbear.server.domain.images.dto.request.DeleteImageRequest;
import com.shy_polarbear.server.domain.images.dto.request.UpdateImageRequest;
import com.shy_polarbear.server.domain.images.dto.response.DeleteImageResponse;
import com.shy_polarbear.server.domain.images.dto.request.UploadImageRequest;
import com.shy_polarbear.server.domain.images.exception.ImageException;
import com.shy_polarbear.server.global.common.dto.ApiResponse;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/fake/api/images")
public class FakeImageController {

    //TODO: 실제 api 개발 시 ModelAttribute validation 필요
    @PostMapping
    public Object uploadImages(@ModelAttribute UploadImageRequest uploadImageRequest) {
        int size = checkUploadTypeAndCount(uploadImageRequest.getType(), uploadImageRequest.getImageFiles());
        return getResponse("/fake/images/upload" + size + ".json");
    }
    @PutMapping
    public Object updateImages(@ModelAttribute UpdateImageRequest updateImageRequest) {
        int size = checkUploadTypeAndCount(updateImageRequest.getType(), updateImageRequest.getNewImageFiles());
        checkDeleteImageCount(updateImageRequest.getOldImageUrls());
        return getResponse("/fake/images/update" + size + ".json");
    }
    @DeleteMapping
    public ApiResponse<DeleteImageResponse> deleteImage(@RequestBody DeleteImageRequest deleteImageRequest) {
        List<String> imageUrls = checkDeleteImageCount(deleteImageRequest.getImageUrls());
        return ApiResponse.success(new DeleteImageResponse(imageUrls.size()));
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

    private static Object getResponse(String path) {
        Object parseDate;
        try {
            String data;
            ClassPathResource resource = new ClassPathResource(path);
            byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
            data = new String(bdata, StandardCharsets.UTF_8);
            JSONParser jsonParser = new JSONParser();
            parseDate = jsonParser.parse(data);
        } catch (Exception e) {
            throw new ImageException(ExceptionStatus.FAIL_UPLOAD_IMAGES);
        }
        return parseDate;
    }
}

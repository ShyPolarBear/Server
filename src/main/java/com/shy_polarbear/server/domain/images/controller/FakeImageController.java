package com.shy_polarbear.server.domain.images.controller;

import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/fake/api/images")
public class FakeImageController {

    //TODO: 실제 api 개발 시 ModelAttribute validation 필요
    @PostMapping
    public Object uploadImages(@ModelAttribute UploadImageRequest uploadImageRequest) {
        //type 체크
        String type = uploadImageRequest.getType();
        if ((!type.equals("feed") && !type.equals("profile")) || type == null) {
            throw new ImageException(ExceptionStatus.INVALID_IMAGE_TYPE);
        }

        //이미지 개수 체크
        List<MultipartFile> files = uploadImageRequest.getImageFiles();
        if (files == null) {
            throw new ImageException(ExceptionStatus.INVALID_IMAGE_COUNT);
        }
        int size = files.size();
        if (size > 5 || files.isEmpty() || (type.equals("profile") && !(size == 1))) {
            throw new ImageException(ExceptionStatus.INVALID_IMAGE_COUNT);
        }

        return getResponse("/fake/images/upload" + size + ".json");
    }

    @PutMapping
    public Object updateImages(@ModelAttribute UpdateImageRequest updateImageRequest) {
        return null;
    }
    @DeleteMapping
    public ApiResponse<DeleteImageResponse> deleteImage(@RequestBody DeleteImageRequest deleteImageRequest) {
        List<String> imageUrls = deleteImageRequest.getImageUrls();
        if (imageUrls.isEmpty() || imageUrls.size() > 5) {
            throw new ImageException(ExceptionStatus.FAIL_DELETE_IMAGES);
        }
        return ApiResponse.success(new DeleteImageResponse(imageUrls.size()));
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
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImageException(ExceptionStatus.FAIL_UPLOAD_IMAGES);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ImageException(ExceptionStatus.FAIL_UPLOAD_IMAGES);
        }
        return parseDate;
    }
}

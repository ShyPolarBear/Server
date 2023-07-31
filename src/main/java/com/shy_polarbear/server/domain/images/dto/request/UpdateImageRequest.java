package com.shy_polarbear.server.domain.images.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class UpdateImageRequest {
    private String type;
    private List<MultipartFile> newImageFiles;
    private List<String> oldImageUrls;
}

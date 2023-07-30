package com.shy_polarbear.server.domain.images.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UploadImageRequest {
    private String type;
    private List<MultipartFile> imageFiles;
}

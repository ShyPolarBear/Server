package com.shy_polarbear.server.domain.images.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UploadImageRequest {

    @NotBlank
    private String type;
    @NotNull
    @Size(min = 1, max = 5)
    private List<MultipartFile> imageFiles;
}

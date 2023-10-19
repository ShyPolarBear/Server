package com.shy_polarbear.server.domain.image.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.*;

@Getter
@Setter
public class UploadImageRequest {

    @NotBlank
    private String type;
    @NotNull
    @Size(min = MIN_IMAGE_COUNT, max = MAX_FEED_IMAGE_COUNT)
    private List<MultipartFile> imageFiles;
}

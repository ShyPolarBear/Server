package com.shy_polarbear.server.domain.image.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.MAX_FEED_IMAGES_COUNT;
import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.MIN_IMAGE_COUNT;

@Setter
@Getter
public class UpdateImageRequest {
    @NotBlank
    private String type;
    @NotNull
    @Size(min = MIN_IMAGE_COUNT, max = MAX_FEED_IMAGES_COUNT)
    private List<MultipartFile> newImageFiles;
    @NotNull
    @Size(min = MIN_IMAGE_COUNT, max = MAX_FEED_IMAGES_COUNT)
    private List<String> oldImageUrls;
}

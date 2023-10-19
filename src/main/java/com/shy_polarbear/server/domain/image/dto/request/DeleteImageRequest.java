package com.shy_polarbear.server.domain.image.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.MAX_FEED_IMAGE_COUNT;
import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.MIN_IMAGE_COUNT;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeleteImageRequest {
    @NotNull
    @Size(min = MIN_IMAGE_COUNT, max = MAX_FEED_IMAGE_COUNT)
    private List<String> imageUrls;
}

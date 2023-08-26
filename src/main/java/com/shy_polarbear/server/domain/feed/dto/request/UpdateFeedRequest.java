package com.shy_polarbear.server.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static com.shy_polarbear.server.global.common.constants.BusinessLogicConstants.MAX_IMAGES_COUNT;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFeedRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @Size(max = MAX_IMAGES_COUNT)
    private List<String> feedImages;
}

package com.shy_polarbear.server.domain.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteImageResponse {
    private Integer deletedImageCount;
}

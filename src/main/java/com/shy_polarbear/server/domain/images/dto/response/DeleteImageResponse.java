package com.shy_polarbear.server.domain.images.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class DeleteImageResponse {
    private Integer deletedImageCount;
}

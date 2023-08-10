package com.shy_polarbear.server.domain.images.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class UploadImageResponse {
    private static final Integer MAX_IMAGE_COUNT = 5;
    private List<String> imageLinks = new ArrayList<>(MAX_IMAGE_COUNT);

    public UploadImageResponse(List<String> imageLinks) {
        imageLinks.stream()
                .forEach(image -> this.imageLinks.add(image));
    }
}

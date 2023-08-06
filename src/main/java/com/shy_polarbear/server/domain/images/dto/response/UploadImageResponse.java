package com.shy_polarbear.server.domain.images.dto.response;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class UploadImageResponse {
    private static final Integer MAX_IMAGE_COUNT = 5;
    List<String> imageLinks = new ArrayList<>(MAX_IMAGE_COUNT);

    public UploadImageResponse(List<String> imageLinks) {
        imageLinks.stream()
                .map(image -> this.imageLinks.add(image));
    }
}
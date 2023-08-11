package com.shy_polarbear.server.domain.images.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateImageResponse {
    List<String> imageLinks;
}

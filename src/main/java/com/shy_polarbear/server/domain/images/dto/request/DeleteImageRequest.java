package com.shy_polarbear.server.domain.images.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeleteImageRequest {
    private List<String> imageUrls = new ArrayList<>();
}

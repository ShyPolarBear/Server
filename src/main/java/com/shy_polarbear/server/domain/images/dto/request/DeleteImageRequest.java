package com.shy_polarbear.server.domain.images.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeleteImageRequest {
    @NotNull
    @Size(min = 1, max = 5)
    private List<String> imageUrls;
}

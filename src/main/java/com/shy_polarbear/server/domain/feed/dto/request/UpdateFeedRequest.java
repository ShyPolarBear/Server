package com.shy_polarbear.server.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFeedRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @Size(max = 5)
    private List<String> feedImages;
}

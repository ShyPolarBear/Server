package com.shy_polarbear.server.domain.feed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedRequest {
    private String title;
    private String content;
    private List<String> feedImages;
}

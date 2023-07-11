package com.shy_polarbear.server.domain.feed.dto;

import java.util.ArrayList;
import java.util.List;

public class CreateFeedRequest {

    private String title;
    private String content;
    private List<String> feedImages = new ArrayList<>();
}

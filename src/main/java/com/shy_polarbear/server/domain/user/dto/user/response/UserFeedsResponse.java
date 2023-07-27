package com.shy_polarbear.server.domain.user.dto.user.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserFeedsResponse {
    private Integer count;
    private Boolean isLast;
    private List<UserFeedResponse> myFeedList = new ArrayList<>();

    public static class UserFeedResponse {
        private Long feedId;
        private String title;
        private String feedImage;
    }
}

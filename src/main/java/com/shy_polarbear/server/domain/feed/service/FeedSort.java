package com.shy_polarbear.server.domain.feed.service;

import com.shy_polarbear.server.domain.quiz.model.OXChoice;
import com.shy_polarbear.server.global.common.util.EnumModel;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FeedSort implements EnumModel<String> {
    BEST("best"), RECENT("recent"), RECENT_BEST("recentBest");
    private final String value;

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static FeedSort toEnum(String stringValue) {
        return switch (stringValue) {
            case "best" -> BEST;
            case "recent" -> RECENT;
            case "recentBest" -> RECENT_BEST;
            default -> throw new RuntimeException(ExceptionStatus.SERVER_ERROR.getMessage());
        };
    }
}

package com.shy_polarbear.server.domain.comment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentCursorResult<T> {

    private List<T> values;

    private Boolean hasNext;

    public CommentCursorResult(List<T> values, Boolean hasNext) {

        this.values = values;
        this.hasNext = hasNext;
    }
}

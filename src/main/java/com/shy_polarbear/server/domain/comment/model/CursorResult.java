package com.shy_polarbear.server.domain.comment.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
public class CursorResult<T> {

    private List<T> values;

    private Boolean hasNext;

    public CursorResult(List<T> values, Boolean hasNext){
        this.values = values;
        this.hasNext = hasNext;
    }
}

package com.shy_polarbear.server.domain.comment.dto.request;

import com.shy_polarbear.server.global.common.constants.BusinessLogicConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    @NotNull
    @Length(max = BusinessLogicConstants.COMMENT_CONTENT_MAX_LENGTH)
    private String content;

    private Long parentId;   // nullable
}

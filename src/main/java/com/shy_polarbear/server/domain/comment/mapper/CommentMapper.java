package com.shy_polarbear.server.domain.comment.mapper;

import com.shy_polarbear.server.domain.comment.dto.CommentDto;
import com.shy_polarbear.server.domain.comment.model.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment commentPostToComment(CommentDto.Post post);

    Comment commentPatchToComment(CommentDto.Patch patch);

    CommentDto.Response commentToCommentResponseDto(Comment comment);
}

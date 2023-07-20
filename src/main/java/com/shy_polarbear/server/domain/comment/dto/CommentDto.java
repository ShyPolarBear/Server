package com.shy_polarbear.server.domain.comment.dto;

import com.shy_polarbear.server.domain.user.model.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @NoArgsConstructor
    public static class Post{

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
    }

    @Data
    @NoArgsConstructor
    public static class Patch{

        private Long commentId;

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class Response {

        private Long commentId;

        // 작성자
        private User author;

        // 내용
        private String content;

        // 생성일
        private LocalDateTime createdAt;

        // 수정일
        private LocalDateTime modifiedAt;

        // 신고 당한일
        private LocalDateTime reportedAt;
    }
}

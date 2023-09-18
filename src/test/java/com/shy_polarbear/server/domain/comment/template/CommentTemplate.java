package com.shy_polarbear.server.domain.comment.template;

import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.feed.template.FeedTemplate;
import com.shy_polarbear.server.domain.user.template.UserTemplate;

public class CommentTemplate {
    public static final Long PARENT_ID = 100L;
    public static final String PARENT_CONTENT = "부모 댓글";
    public static final Long CHILD_ID = 200L;
    public static final String CHILD_CONTENT = "자식 댓글";
    public static final Long DELETED_ID = 400L;
    public static final String DELETED_CONTENT = "삭제된 댓글";

    public static Comment createDummyParentComment() {
        Comment comment = Comment.createComment(UserTemplate.createDummyUser(), PARENT_CONTENT, FeedTemplate.createDummyFeed());
        comment.setIdForTest(PARENT_ID);
        return comment;
    }

    public static Comment createDummyChildComment(Comment parent) {
        Comment comment = Comment.createChildComment(UserTemplate.createDummyUser(), CHILD_CONTENT, FeedTemplate.createDummyFeed(), parent);
        comment.setIdForTest(CHILD_ID);
        return comment;
    }

    public static Comment createDummySoftDeletedComment() {
        Comment comment = Comment.createComment(UserTemplate.createDummyUser(), DELETED_CONTENT, FeedTemplate.createDummyFeed());
        comment.setIdForTest(DELETED_ID);
        comment.softDelete();
        return comment;
    }
}

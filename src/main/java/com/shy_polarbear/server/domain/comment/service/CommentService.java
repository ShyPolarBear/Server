package com.shy_polarbear.server.domain.comment.service;



import com.shy_polarbear.server.domain.comment.dto.request.CreateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.request.UpdateCommentRequest;
import com.shy_polarbear.server.domain.comment.dto.response.CreateCommentResponse;
import com.shy_polarbear.server.domain.comment.dto.response.GetCommentResponse;
import com.shy_polarbear.server.domain.comment.dto.response.UpdateCommentResponse;
import com.shy_polarbear.server.domain.comment.model.Comment;
import com.shy_polarbear.server.domain.comment.model.CommentLike;
import com.shy_polarbear.server.domain.comment.model.CommentReport;
import com.shy_polarbear.server.domain.comment.repository.CommentRepository;
import com.shy_polarbear.server.domain.feed.model.Feed;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.domain.user.exception.CommentException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import com.shy_polarbear.server.domain.user.service.UserService;
import com.shy_polarbear.server.global.exception.CustomException;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final FeedService feedService;

    private final FeedRepository feedRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository,
                          UserService  userService, FeedService feedService, FeedRepository feedRepository){
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.feedService = feedService;
        this.feedRepository = feedRepository;
    }

    // 댓글 등록하기
    public CreateCommentResponse createComment(Long feedId, CreateCommentRequest createCommentRequest) {
        User user = userService.getCurruentUser();
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_USER));
        Comment comment = Comment.createComment(user, createCommentRequest.getContent(), feed);
        commentRepository.save(comment);
        return new CreateCommentResponse(comment.getId(), null);
    }

    // 대댓글 등록하기
    public CreateCommentResponse createChildComment(Long feedId, CreateCommentRequest createCommentRequest) {
        User user = userService.getCurruentUser();
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_FEED));
        Comment parentComment = commentRepository.findById((createCommentRequest.getParentId()))
                .orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));
        Comment comment = Comment.createChildComment(user, createCommentRequest.getContent(), feed, parentComment);
        commentRepository.save(comment);
        return new CreateCommentResponse(comment.getId(), parentComment.getId());
    }

    private static final int DEFAULT_PAGE_SIZE = 10;

    // 댓글 조회
    public List<GetCommentResponse.CommentInfo> getComments(Long feedId, int pageNumber, Integer pageSize) {

        // pageSize가 null이거나 1보다 작으면 기본값으로 설정
        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        // 페이지 번호와 페이지 크기를 기반으로 댓글 목록을 가져오는 메서드를 호출합니다.
        List<Comment> comments = commentRepository.findCommentsByFeedId(feedId, PageRequest.of(pageNumber, pageSize));

        // comments를 GetCommentResponse.CommentInfo 형태로 변환하여 리턴합니다.
        return comments.stream()
                .map(comment -> createCommentInfo(comment))
                .collect(Collectors.toList());
    }


    // 다음 페이지가 있는지 확인하는 로직을 구현합니다.
    public boolean hasNextPage(Long feedId, int pageNumber, int pageSize) {
        List<Comment> nextPageComments = commentRepository.findCommentsByFeedId(feedId, PageRequest.of(pageNumber + 1, pageSize));
        return !nextPageComments.isEmpty();
    }

    // 댓글을 GetCommentResponse.CommentInfo 형태로 변환하는 메서드
    private GetCommentResponse.CommentInfo createCommentInfo(Comment comment) {
        User currentUser = userService.getCurruentUser();

        List<GetCommentResponse.CommentInfo> childComments = comment.getChildComments().stream()
                .map(childComment -> createCommentInfo(childComment))
                .collect(Collectors.toList());

        // 작성자 정보 추출
        String authorName = comment.getAuthor() != null ? comment.getAuthor().getNickName() : null;
        String authorProfileImage = comment.getAuthor() != null ? comment.getAuthor().getProfileImage() : null;

        // 좋아요 수 계산
        long likeCount = comment.getCommentLikes().size();

        // 현재 사용자가 댓글 작성자인지 확인
        boolean isAuthor = comment.getIsAuthor(currentUser);

        // 현재 사용자가 댓글에 좋아요를 눌렀는지 확인
        boolean isLike = comment.getCommentLikes().stream()
                .anyMatch(like -> like.getUser().equals(currentUser));

        // 댓글 작성일자
        String createdDate = comment.getCreatedDate().toString();

        return new GetCommentResponse.CommentInfo(
                comment.getId(),
                authorName,
                authorProfileImage,
                comment.getContent(),
                likeCount,
                isAuthor,
                isLike,
                createdDate,
                childComments
        );
    }

    // 댓글 수정
    public UpdateCommentResponse updateComment(UpdateCommentRequest updateCommentRequest, Long commentId) {
        // 현재 사용자 정보 가져오기
        User findUser = userService.getCurruentUser();
        // 댓글 조회
        Comment findComment = findComment(commentId);
        // 본인이 작성한 댓글인지 확인
        if (!findComment.getAuthor().equals(findUser)){
            throw new CommentException(ExceptionStatus.NOT_MY_COMMENT);
        }
        // 댓글 내용 업데이트
        findComment.updateContent(updateCommentRequest.getContent());

        return UpdateCommentResponse.from(findComment);
    }

    // 댓글 삭제
    public boolean deleteComment(Long commentId) {
        // 현재 사용자 정보 가져오기
        User findUser = userService.getCurruentUser();

        // 댓글 조회
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment != null) {
            commentRepository.delete(existingComment);
            return true;
        } else return false;
    }

    // 댓글 좋아요
    public void likeComment(Long commentId, Long userId) {
        // 좋아요를 누르는 유저
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CommentException(ExceptionStatus.NOT_FOUND_USER));
        // 댓글 조회
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment != null) {
            // 이미 해당 사용자가 댓글에 좋아요를 누른 경우, 중복 추가를 막기 위해 체크
            if (existingComment.getCommentLikes().stream().noneMatch(like -> like.getUser().equals(user))) {
                CommentLike commentLike = new CommentLike(user, existingComment);
                existingComment.addLike(commentLike);
                commentRepository.save(existingComment);
            }
        } else {
            throw new CommentException(ExceptionStatus.NOT_FOUND_COMMENT);
        }
    }
    // 댓글 신고
    public void reportComment(Long commentId, Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CommentException(ExceptionStatus.NOT_FOUND_USER));

        // 댓글 조회
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));

        if (existingComment != null) {
            // 본인의 댓글 신고할 경우
            if (existingComment.getAuthor().equals(user)){
                throw new CommentException(ExceptionStatus.NOT_MY_COMMENT_REPORT);
            }
            // 이미 신고한 댓글인 경우
            if (existingComment.getCommentReports().stream().anyMatch(report -> report.getUser().equals(user))){
                throw new CommentException(ExceptionStatus.COMMENT_REPORT_DUPLICATION);
            }

            // 댓글에 신고 정보 추가
            existingComment.addReport(new CommentReport(user));
            existingComment.reportComment();
            commentRepository.save(existingComment);
        } else {
            // 댓글이 존재하지 않는 경우
            throw new CommentException(ExceptionStatus.NOT_FOUND_COMMENT);
        }
    }

    private Comment findComment(Long commentId) {
        Optional<Comment> findComment = commentRepository.findById(commentId);

        return findComment.orElseThrow(() -> new CommentException(ExceptionStatus.NOT_FOUND_COMMENT));
    }
}

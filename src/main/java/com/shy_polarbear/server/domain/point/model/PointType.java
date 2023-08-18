package com.shy_polarbear.server.domain.point.model;

import lombok.Getter;

@Getter
public enum PointType {

    CREATE_FEED(500), //환경피드 작성
    LIKE_RECEIVED(10), //내가 쓴 환경 피드에 좋아요를 받은 경우
    BEST_FEED(2000), //내가 쓴 환경 피드가 베스트 피드가 된 경우 (베스트 피드 == 좋아요 50개 이상)
    SOLVE_QUIZ(100), //환경 문제를 맞춘 경우
    NOT_SOLVE_QUIZ(0), //환경 문제를 못 맞춘 경우
    DELETE_FEED(-500), //환경 피드 삭제
    LIKE_RECEIVED_CANCEL(-10), //내가 쓴 환경 피드에 좋아요가 취소된 경우
    DELETE_BEST_FEED(2000), //베스트 피드를 삭제한 경우 (베스트 피드 == 좋아요 50개 이상)
    REPORT_RECEIVED(500); //각종 신고를 당한 경우

    private final int value;

    PointType(int value) {
        this.value = value;
    }

}

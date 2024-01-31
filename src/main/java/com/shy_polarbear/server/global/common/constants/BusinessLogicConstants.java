package com.shy_polarbear.server.global.common.constants;

public abstract class BusinessLogicConstants {  // TODO refactor: 각 객체로 책임 이동

    /**
     * 퀴즈
     */

    public static final String REVIEW_QUIZ_LIMIT_PARAM_DEFAULT_VALUE = "5";

    /**
     * 피드
     */
    public static final int MAX_IMAGES_COUNT = 5;
    public static final int BEST_FEED_MIN_LIKE_COUNT = 50;
    public static final String FEED_LIMIT_PARAM_DEFAULT_VALUE = "10";
    public static final int RECENT_BEST_FEED_DAY_LIMIT = 7;
    public static final int FEED_CONTENT_MAX_LENGTH = 1000;


    /**
     * 댓글, 대댓글
     **/
    public static final int COMMENT_CONTENT_MAX_LENGTH = 300;
    public static final String COMMENT_LIMIT_PARAM_DEFAULT_VALUE = "10";

    /**
     * FCM 푸시 알림
     **/
    public static final int RECENT_NOTIFICATION_LIMIT = 30;

    /**
     * 유저
     */
    public static final String USER_FEED_LIMIT_PARAM_DEFAULT_VALUE = "10";

    /**
     * 이미지
     **/
    public static final int MIN_IMAGE_COUNT = 1;
    public static final int MAX_FEED_IMAGE_COUNT = 5;

    public static final int IMAGE_URL_MAX_LENGTH = 310;


    /**
     * 랭킹
     */
    public static final String RANKING_LIMIT_PARAM_DEFAULT_VALUE = "10";
    public static final String RANKING_UPDATE_DATE = "0 0 8 * * *";
    public static final int RANKING_POINT_RESET_DAY = 1;
    public static final int RANKING_POINT_RESET_HOUR = 8;
    public static final int WINNABLE_RANKING_LIMIT = 5;

}

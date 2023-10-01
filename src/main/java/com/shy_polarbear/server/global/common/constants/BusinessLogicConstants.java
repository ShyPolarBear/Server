package com.shy_polarbear.server.global.common.constants;

import javax.swing.plaf.PanelUI;

public abstract class BusinessLogicConstants {

    /** 퀴즈 **/
    public static final int OX_QUIZ_TIME_LIMIT = 17;
    public static final int MULTIPLE_CHOICE_QUIZ_TIME_LIMIT = 17;
    public static final int REVIEW_QUIZ_AMOUNT_LIMIT = 5;
    public static final String REVIEW_QUIZ_LIMIT_PARAM_DEFAULT_VALUE = "5";

    /** 피드 **/
    public static final int MAX_FEED_IMAGES_COUNT = 5;
    public static final int BEST_FEED_MIN_LIKE_COUNT = 50;
    public static final String FEED_LIMIT_PARAM_DEFAULT_VALUE = "10";
    public static final int RECENT_BEST_FEED_DAY_LIMIT = 7;

    /**
     * 이미지
     **/
    public static final int MIN_IMAGE_COUNT = 1;


    /**
     * 랭킹
     */
    public static final String RANKING_LIMIT_PARAM_DEFAULT_VALUE = "10";
    public static final String RANKING_UPDATE_DATE = "0 0 8 * * *";
    public static final int RANKING_POINT_RESET_DAY = 1;
    public static final int RANKING_POINT_RESET_HOUR = 8;


}

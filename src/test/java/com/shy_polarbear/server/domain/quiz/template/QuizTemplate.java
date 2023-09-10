package com.shy_polarbear.server.domain.quiz.template;

import com.shy_polarbear.server.domain.quiz.model.*;
import com.shy_polarbear.server.domain.user.template.UserTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizTemplate {

    public static OXQuiz createDummyOXQuizA() {
        OXQuiz quiz = OXQuiz.builder()
                .question("부탄가스 통은 가스를 모두 제거하고 버려야 한다")
                .answer(OXChoice.O)
                .explanation("부탄가스나 살충제 용기 등 가스가 들어가 있는 기타 캔류는 구멍을 뚫어 남은 가스를 비운 후 배출해야 해요!")
                .build();
        quiz.setIdForMockTest(1L);
        return quiz;
    }

    public static OXQuiz createDummyOXQuizB() {
        OXQuiz quiz = OXQuiz.builder()
                .question("철 캔과 알루미늄 캔을 구분해서 버려야 한다")
                .answer(OXChoice.O)
                .explanation("내용물을 비우고 물로 헹군 후 압착하여 철, 알루미늄 캔 구분해서 배출해요 \n 알루미늄 캔을 땅속에 묻혀 분해되는 데 걸리는 시간이 500년이나 된답니다!")
                .build();
        quiz.setIdForMockTest(2L);
        return quiz;
    }


    public static OXQuiz createDummyOXQuizC() {
        OXQuiz quiz = OXQuiz.builder()
                .question("게 껍데기는 음식물 쓰레기로 버려야 한다")
                .answer(OXChoice.X)
                .explanation("'동물 사료로 쓰일 수 있는가?' 동물이 먹을 수 있을 대 음식물쓰레기, 아닐 경우 일반쓰레기로 분류돼요!")
                .build();
        quiz.setIdForMockTest(3L);
        return quiz;
    }

    public static OXQuiz createDummyOXQuizD() {
        OXQuiz quiz = OXQuiz.builder()
                .question("양파 껍질은 일반 쓰레기로 버려야 한다")
                .answer(OXChoice.O)
                .explanation("까도까도 끝이 없는 양파 껍질!\n EX) 대파, 미나리 등의 뿌리, 양파, 마늘, 옥수수 등의 껍질, 고추씨, 고춧대, 옥수숫대 등 질긴 채소류, 호두, 밤, 땅콩 등 딱딱한 껍데기와 복숭아, 살구, 감 등 핵과류의 단단한 씨를 가진 과일류는 일반쓰레기로 분류돼요!")
                .build();
        quiz.setIdForMockTest(4L);
        return quiz;
    }

    public static OXQuiz createDummyOXQuizE() {
        OXQuiz quiz = OXQuiz.builder()
                .question("미세먼지는 1군 발암물질이다")
                .answer(OXChoice.O)
                .explanation("미세먼지는 호흡기 및 심혈관계 질환의 발생과 연관이 있으며 사망률을 증가시키므로 주의가 필요합니다!")
                .build();
        quiz.setIdForMockTest(5L);
        return quiz;
    }

    public static List<Quiz> createDummyOXQuizList() {
        return Arrays.asList(createDummyOXQuizA(), createDummyOXQuizB(), createDummyOXQuizC(), createDummyOXQuizD(), createDummyOXQuizE());
    }

    // 객관식 퀴즈 1개
    public static MultipleChoiceQuiz createDummyMultipleChoiceQuizA() {
        MultipleChoiceQuiz quiz = MultipleChoiceQuiz.builder()
                .question("다음 중 일반 쓰레기가 아닌 것은?")
                .explanation("정답은 바나나 껍질!!\n이 밖에도 파인애플 껍질은 일반쓰레기, 바나나 겁질은 음식물쓰레기, 족발 뼈나 갈비뼈 등은 일반쓰레기라는 점 기억해주세요!")
                .build();
        quiz.setIdForMockTest(101L);
        return quiz;
    }

    public static final MultipleChoiceQuiz dummyMultipleChoiceQuizA = createDummyMultipleChoiceQuizA();

    // 객관식 선택지 4개
    public static final MultipleChoice makeDummyMultipleChoiceAA() {
        MultipleChoice choice = MultipleChoice.builder().multipleChoiceQuiz(dummyMultipleChoiceQuizA).isAnswer(false).content("견과류 껍데기").build();
        choice.setIdForMockTest(1L);
        return choice;
    }
    public static final MultipleChoice makeDummyMultipleChoiceAB() {
        MultipleChoice choice = MultipleChoice.builder().multipleChoiceQuiz(dummyMultipleChoiceQuizA).isAnswer(false).content("일회용 티백").build();
        choice.setIdForMockTest(2L);
        return choice;
    }
    public static final MultipleChoice makeDummyMultipleChoiceAC() {
        MultipleChoice choice = MultipleChoice.builder().multipleChoiceQuiz(dummyMultipleChoiceQuizA).isAnswer(true).content("바나나 껍질").build();
        choice.setIdForMockTest(3L);
        return choice;
    }
    public static final MultipleChoice makeDummyMultipleChoiceAD() {
        MultipleChoice choice = MultipleChoice.builder().multipleChoiceQuiz(dummyMultipleChoiceQuizA).isAnswer(false).content("계란 껍데기").build();
        choice.setIdForMockTest(4L);
        return choice;
    }

    public static List<MultipleChoice> createDummyMultipleChoiceList() {
        return Arrays.asList(makeDummyMultipleChoiceAA(), makeDummyMultipleChoiceAB(), makeDummyMultipleChoiceAC(), makeDummyMultipleChoiceAD());
    }

}

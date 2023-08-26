package com.shy_polarbear.server.global.common.dummy;

import com.shy_polarbear.server.domain.quiz.model.MultipleChoice;
import com.shy_polarbear.server.domain.quiz.model.MultipleChoiceQuiz;
import com.shy_polarbear.server.domain.quiz.model.OXChoice;
import com.shy_polarbear.server.domain.quiz.model.OXQuiz;
import com.shy_polarbear.server.domain.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Component("QuizInitializer")
@DependsOn({"LoginUserInitializer"})
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuizInitializer {  // TODO: 지금 로직은 지속가능하지 않음. 퀴즈 업데이트를 한다면..?
    private final QuizRepository quizRepository;

    @PostConstruct
    public void init() {
        if (quizRepository.count() > 0) {
            log.info("[QuizInitializer] 퀴즈 이미 존재");
        } else {
            createDummyOXQuiz();
            createDummyMultipleChoiceQuiz();
            log.info("[QuizInitializer] 퀴즈 생성 완료");
        }
    }

    private void createDummyOXQuiz() {
        quizRepository.save(OXQuiz.builder().question("부탄가스 통은 가스를 모두 제거하고 버려야 한다").answer(OXChoice.O).explanation("부탄가스나 살충제 용기 등 가스가 들어가 있는 기타 캔류는 구멍을 뚫어 남은 가스를 비운 후 배출해야 해요!").build());
        quizRepository.save(OXQuiz.builder().question("철 캔과 알루미늄 캔을 구분해서 버려야 한다").answer(OXChoice.O).explanation("내용물을 비우고 물로 헹군 후 압착하여 철, 알루미늄 캔 구분해서 배출해요 \n 알루미늄 캔을 땅속에 묻혀 분해되는 데 걸리는 시간이 500년이나 된답니다!").build());
        quizRepository.save(OXQuiz.builder().question("게 껍데기는 음식물 쓰레기로 버려야 한다").answer(OXChoice.X).explanation("'동물 사료로 쓰일 수 있는가?' 동물이 먹을 수 있을 대 음식물쓰레기, 아닐 경우 일반쓰레기로 분류돼요!").build());
        quizRepository.save(OXQuiz.builder().question("양파 껍질은 일반 쓰레기로 버려야 한다").answer(OXChoice.O).explanation("까도까도 끝이 없는 양파 껍질!\n EX) 대파, 미나리 등의 뿌리, 양파, 마늘, 옥수수 등의 껍질, 고추씨, 고춧대, 옥수숫대 등 질긴 채소류, 호두, 밤, 땅콩 등 딱딱한 껍데기와 복숭아, 살구, 감 등 핵과류의 단단한 씨를 가진 과일류는 일반쓰레기로 분류돼요!").build());
        quizRepository.save(OXQuiz.builder().question("미세먼지는 1군 발암물질이다").answer(OXChoice.O).explanation("미세먼지는 호흡기 및 심혈관계 질환의 발생과 연관이 있으며 사망률을 증가시키므로 주의가 필요합니다!").build());
        quizRepository.save(OXQuiz.builder().question("폐형광등, 건전지는 일반 쓰레기다").answer(OXChoice.X).explanation("폐건전지가 든 일반 쓰레기를 태우면 폭발위험이 있으므로 주민센터 / 행정 복지 센터 / 폐건전지 수거함에 버려주세요!").build());
        quizRepository.save(OXQuiz.builder().question("야간 인공조명은 동식물 성장에 해롭다").answer(OXChoice.O).explanation("야간 인공조명은 동식물의 생태계를 파괴시키는 빛공해에 포함되어 정상적인 성장에 악영향을 끼칩니다").build());
    }

    private void createDummyMultipleChoiceQuiz() {
        MultipleChoiceQuiz quiz1 = MultipleChoiceQuiz.builder().question("다음 중 일반 쓰레기가 아닌 것은?").explanation("정답은 바나나 껍질!!\n이 밖에도 파인애플 껍질은 일반쓰레기, 바나나 겁질은 음식물쓰레기, 족발 뼈나 갈비뼈 등은 일반쓰레기라는 점 기억해주세요!").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz1).isAnswer(false).content("견과류 껍데기").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz1).isAnswer(false).content("일회용 티백").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz1).isAnswer(true).content("바나나 껍질").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz1).isAnswer(false).content("계란 껍데기").build();
        quizRepository.save(quiz1);

        MultipleChoiceQuiz quiz2 = MultipleChoiceQuiz.builder().question("에너지를 절약할 수 있는 겨울철 적정 실내온도는?").explanation("보건복지부와 질병관리본부에서 정한 18~20도는 난방에너지 절약이 가능하며, 이보다 높은 실내온도를 유지할 경우 추운 날씨에 대한 인체 적응력 및 면역력이 떨어진다고 합니다.").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz2).isAnswer(false).content("13-16도").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz2).isAnswer(false).content("16-18도").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz2).isAnswer(true).content("18-20도").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz2).isAnswer(false).content("26-28도").build();
        quizRepository.save(quiz2);

        MultipleChoiceQuiz quiz3 = MultipleChoiceQuiz.builder().question("세계 환경의 날은 언제일까?").explanation("유연환경계획(UNEP)은 1987년부터 매년 세계 환경의 날을 맞아 그해의 주제를 선정 및 발표하며, 대륙별로 돌아가며 한 나라를 정해 행사를 개최하고 있는데요. 한국에서도 1997년에 세계 환경의 날 행사를 개최했습니다 :)").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz3).isAnswer(false).content("3월 22일").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz3).isAnswer(false).content("4월 22일").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz3).isAnswer(true).content("5월 31일").build();
        MultipleChoice.builder().multipleChoiceQuiz(quiz3).isAnswer(false).content("6월 5일").build();
        quizRepository.save(quiz3);
    }

}

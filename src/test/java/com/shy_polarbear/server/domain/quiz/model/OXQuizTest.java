package com.shy_polarbear.server.domain.quiz.model;

import com.shy_polarbear.server.domain.quiz.model.OXChoice;
import com.shy_polarbear.server.domain.quiz.model.OXQuiz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OXQuizTest {
    @Test
    @DisplayName("퀴즈가 생성된다.")
    public void testQuizCreation() {
        String question = "질문", explanation = "설명";

        assertThatNoException().isThrownBy(()-> createOXQuiz(question, explanation, OXChoice.O));
    }

    private OXQuiz createOXQuiz(String question, String explanation, OXChoice oxChoice) {
        return OXQuiz.builder()
                .question(question)
                .explanation(explanation)
                .answer(oxChoice)
                .build();
    }
}

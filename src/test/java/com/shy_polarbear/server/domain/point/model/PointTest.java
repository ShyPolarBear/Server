package com.shy_polarbear.server.domain.point.model;

import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Point 클래스")
class PointTest {

    private User user;
    private final String nickName = "노을";
    private final String email = "chi6465618@naver.com";
    private final String profileImage = "";
    private final String phoneNumber = "01093926465";
    private final UserRole userRole = UserRole.ROLE_USR;

    @BeforeEach
    void setUp() {
        user = User.createUser(nickName, email, profileImage, phoneNumber, userRole, null, null);
    }

    @DisplayName("Point 객체가 생성되면 User의 point 값이 변경된다.")
    @Test
    void createPoint() {
        Point.createPoint(user, PointType.CREATE_FEED);
        Point.createPoint(user, PointType.BEST_FEED);

        int sum = user.getPoints().stream()
                .mapToInt(point -> point.getPointType().getValue()).sum();
        assertThat(sum).isEqualTo(PointType.CREATE_FEED.getValue() + PointType.BEST_FEED.getValue());
    }
}
package com.shy_polarbear.server.domain.feed.model;

import com.shy_polarbear.server.domain.feed.repository.FeedLikeRepository;
import com.shy_polarbear.server.domain.feed.repository.FeedRepository;
import com.shy_polarbear.server.domain.feed.service.FeedService;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.domain.user.model.UserRole;
import com.shy_polarbear.server.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;

@DisplayName("Feed 클래스")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class FeedTest {

    private User user;
    private Feed feed;
    private final String nickName = "노을";
    private final String email = "chi6465618@naver.com";
    private final String profileImage = "";
    private final String phoneNumber = "01093926465";
    private final UserRole userRole = UserRole.ROLE_USR;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FeedLikeRepository feedLikeRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedService feedService;

    @BeforeEach
    void setUp() {
        user = User.createUser(nickName, email, profileImage, phoneNumber, userRole, null, null, passwordEncoder);
        userRepository.save(user);
        feed = Feed.createFeed("", "", new ArrayList<>(), user);
        feedRepository.save(feed);
    }
}
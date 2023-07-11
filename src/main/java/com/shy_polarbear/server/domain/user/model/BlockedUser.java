package com.shy_polarbear.server.domain.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //차단하는 유저
    @ManyToOne
    @JoinColumn(name = "blocking_user_id")
    private User blockingUser;

    //차단당한 유저
    @ManyToOne
    @JoinColumn(name = "blocked_user_id")
    private User blockedUser;

    @Builder
    private BlockedUser(User blockingUser, User blockedUser) {
        this.blockingUser = blockingUser;
        this.blockedUser = blockedUser;
    }

    public static BlockedUser createBlockedUser(User blockingUser, User blockedUser) {
        return BlockedUser.builder()
                .blockedUser(blockedUser)
                .blockingUser(blockingUser)
                .build();
    }
}

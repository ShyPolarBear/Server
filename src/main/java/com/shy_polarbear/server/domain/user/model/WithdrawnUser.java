package com.shy_polarbear.server.domain.user.model;

import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawnUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdrawn_user_id")
    private Long id;
    private Long userId;
}

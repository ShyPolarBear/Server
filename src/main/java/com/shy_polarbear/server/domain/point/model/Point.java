package com.shy_polarbear.server.domain.point.model;


import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.common.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType pointType;

    @Column(nullable = false, name = "point_value")
    private Integer value;

    @Builder
    private Point(User user, PointType pointType, Integer value) {
        this.user = user;
        this.pointType = pointType;
        this.value = value;
    }

    public static Point createPoint(User user, PointType pointType) {
        Point point = Point.builder()
                .user(user)
                .pointType(pointType)
                .value(pointType.getValue())
                .build();
        user.addPoint(point);
        return point;
    }
}

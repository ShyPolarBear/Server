package com.shy_polarbear.server.domain.point.model;

<<<<<<< HEAD:src/main/java/com/shy_polarbear/server/domain/point/model/Point.java
import com.shy_polarbear.server.domain.ranking.model.Ranking;
import com.shy_polarbear.server.domain.user.model.User;
=======
import com.shy_polarbear.server.domain.ranking.entity.Ranking;
import com.shy_polarbear.server.domain.user.entity.User;
import com.shy_polarbear.server.global.common.BaseEntity;
>>>>>>> eb05871b6fd8e3daa86135174cfecc35d7ef4c2c:src/main/java/com/shy_polarbear/server/domain/point/entity/Point.java
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranking_id")
    private Ranking ranking;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Builder
    private Point(User user, PointType pointType) {
        this.user = user;
        this.pointType = pointType;
    }

    public static Point createPoint(User user, PointType pointType) {
        Point point = Point.builder()
                .user(user)
                .pointType(pointType)
                .build();
        user.addPoint(point);
        return point;
    }
}

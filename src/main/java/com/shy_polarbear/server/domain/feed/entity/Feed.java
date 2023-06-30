package com.shy_polarbear.server.domain.feed.entity;

import com.shy_polarbear.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;
    private String title;
    private String content;
    @OneToMany(mappedBy = "feed")
    private final List<Like> feedLikes = new ArrayList<>();
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "feed_images", joinColumns = @JoinColumn(name = "feed_id"))
    @Column(name = "feed_image")
    private final List<String> feedImages = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;
}

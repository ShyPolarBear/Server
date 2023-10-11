package com.shy_polarbear.server.global.common.model;

import com.shy_polarbear.server.global.common.util.LocalDateTimeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    protected String createdDate;

    @LastModifiedDate
    protected String modifiedDate;

    @PrePersist
    public void onPrePersist(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTimeUtils.convertToString(now);
        this.createdDate = LocalDateTimeUtils.convertToString(now);
        this.modifiedDate = this.createdDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        LocalDateTime now = LocalDateTime.now();
        this.modifiedDate = LocalDateTimeUtils.convertToString(now);
    }
}

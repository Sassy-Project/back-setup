package com.projectsassy.sassy.token.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "REFRESH_TOKEN")
public class RefreshToken {

    @Id
    @Column(name = "rt_key")
    private String key;
    @Column(name = "rt_value")
    private String value;

    @CreatedDate
    @Column(name = "rt_create_time")
    private LocalDateTime createdTime;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}

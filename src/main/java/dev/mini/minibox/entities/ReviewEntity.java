package dev.mini.minibox.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewEntity {
    private int index;
    private String movieCd;
    private String nickname;
    private int rating;
    private String keyword;
    private String content;
    private int like;
    private LocalDateTime createdAt;
}

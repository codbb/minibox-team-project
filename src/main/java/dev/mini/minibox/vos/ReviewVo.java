package dev.mini.minibox.vos;

import dev.mini.minibox.entities.ReviewEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewVo extends ReviewEntity {
    private int index;
    private String movieNm;
    private String movieNmEn;
    private LocalDateTime createdAt;
}

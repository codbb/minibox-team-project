package dev.mini.minibox.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = {"index"})
@AllArgsConstructor
@NoArgsConstructor
public class TheaterDetailEntity { // 극장의 지점의 상세페이지를 나타내는곳
    private String brchNo;
    private String content;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private int index;
}

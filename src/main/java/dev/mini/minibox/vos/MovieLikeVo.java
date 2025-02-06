package dev.mini.minibox.vos;

import dev.mini.minibox.entities.MovieLikeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieLikeVo extends MovieLikeEntity {
    private String movieNm;
    private String directors;
    private String actors;
    private String genres;
    private String showTm;
    private String openDt;
    private String content;

}

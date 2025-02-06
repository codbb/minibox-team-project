package dev.mini.minibox.entities;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MovieBoxOffice {
    private int rank;
    private int audiAcc;
    private int id;
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String showTypes;   // JSON 형태
    private String directors;   // JSON 형태
    private String actors;      // JSON 형태
    private String genres;      // JSON 형태
    private String showTm;
    private String watchGradeNm;
    private String openDt;
    private String prdtStatNm;
    private String typeNm;
    private String nationNm;
    private String poster;
    private String content;
}

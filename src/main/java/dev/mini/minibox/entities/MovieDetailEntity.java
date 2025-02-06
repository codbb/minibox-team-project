package dev.mini.minibox.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@EqualsAndHashCode()
public class MovieDetailEntity {
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

    public void update(MovieDetailEntity newData) {
        // 기존 데이터와 새로운 데이터가 같을 경우 업데이트를 생략, 새로운 데이터의 값이 null 이 아닌 경우에만 업데이트

        if (newData.getShowTypes() != null && !newData.getShowTypes().isEmpty() && this.showTypes.isEmpty()) {
            this.showTypes = newData.getShowTypes();
        }
        if (newData.getDirectors() != null && !newData.getDirectors().isEmpty() && this.directors.isEmpty()) {
            this.directors = newData.getDirectors();
        }
        if (newData.getActors() != null && !newData.getActors().isEmpty() && this.actors.isEmpty()) {
            this.actors = newData.getActors();
        }
        if (newData.getShowTm() != null && !newData.getShowTm().isEmpty() && this.showTm.isEmpty()) {
            this.showTm = newData.getShowTm();
        }
        if (newData.getWatchGradeNm() != null && !newData.getWatchGradeNm().isEmpty() && this.watchGradeNm.isEmpty()) {
            this.watchGradeNm = newData.getWatchGradeNm();
        }
        if (newData.getOpenDt() != null && !newData.getOpenDt().isEmpty() && this.openDt.isEmpty()) {
            this.openDt = newData.getOpenDt();
        }
        if (newData.getPrdtStatNm() != null && !newData.getPrdtStatNm().equals(this.prdtStatNm)) {
            this.prdtStatNm = newData.getPrdtStatNm();
        }
        if (newData.getPoster() != null && !newData.getPoster().isEmpty() && this.poster.isEmpty()) {
            this.poster = newData.getPoster();
        }
        if (newData.getContent() != null && !newData.getContent().isEmpty() &&this.content.isEmpty()) {
            this.content = newData.getContent();
        }
    }

}

package dev.mini.minibox.entities;

import lombok.*;


@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MovieEntity {
    private String movieCd;
    private String movieNm;
    private String openDt;
    private int rank;
    private int audiAcc;

    public void update(MovieEntity newData) {
        if (newData.getMovieNm() != null && !newData.getMovieNm().equals(this.movieNm)) {
            this.movieNm = newData.getMovieNm();
        }
        if (newData.getOpenDt() != null && !newData.getOpenDt().equals(this.openDt)) {
            this.openDt = newData.getOpenDt();
        }
        if (newData.getAudiAcc() != this.audiAcc) {
            this.audiAcc = newData.getAudiAcc();
        }
    }
}


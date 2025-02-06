package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.MovieDetailEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieDetailMapper {
    void insertMovieInfo(MovieDetailEntity movieDetailEntity);

    MovieDetailEntity findByMovieCd(String movieCd);

    MovieDetailEntity selectMovieByMovieCd(String movieCd);

    String selectMoviePosterByMovieCd(String movieCd);

    String selectMovieBoxOffice(String movieCd);

    String selectMovieAudiAcc(String movieCd);

    String selectWatchGrade(String movieCd);

    // 예매율
    int selectReserveCountByMovieNm(String movieNm);

    int selectReserveCountAll();

    List<String> selectAllMovieNms();
}

package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.MovieBoxOffice;
import dev.mini.minibox.entities.MovieDetailEntity;
import dev.mini.minibox.entities.MovieEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieMapper {
    void insertMovieCd(MovieEntity movieEntity);

    MovieBoxOffice[] selectMoviesByRank(boolean isMainPage);

    MovieDetailEntity[] selectMovies();

    MovieEntity findByMovieCd(@Param("movieCd") String movieCd);

    // 영화별 관람등급 가져오기
    String selectWatchGradeByMovieCd(@Param("movieCd") String movieCd);

    // 영화별 개봉상태 가져오기
    String selectPrdtStatNmByMovieCd(@Param("movieCd") String movieCd);

    // 영화별 상영타입 가져오기
    String selectShowTypesByMovieCd(@Param("movieCd") String movieCd);

    // 박스오피스 영화 포스터 가져오기
    List<String> selectAllPosters();

    List<String> selectAllMovieCds();

    List<String> selectAllMovieNms();

    List<String> selectAllOpenDts();

    // 상영예정작 영화 가져오기
    MovieDetailEntity[] selectComingSoonMovies();

    // 상영예정작 영화 포스터 가져오기
    String selectMoviePoster(@Param("movieCd") String movieCd);

    List<String> selectAllMovieCdsAtMovies();

    List<Integer> selectAllMovieIdsAtMovies();

    // 필름 영화 가져오기
    MovieDetailEntity[] selectFilmMovies();

    // 국내 영화 가져오기
    MovieDetailEntity[] selectDomesticMovies();

    // 해외 영화 가져오기
    MovieDetailEntity[] selectOverseasMovies();

    // 개봉상태 업데이트
    void updateMovieStatus();
}

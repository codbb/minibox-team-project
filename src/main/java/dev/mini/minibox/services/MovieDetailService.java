package dev.mini.minibox.services;

import dev.mini.minibox.entities.MovieDetailEntity;
import dev.mini.minibox.mappers.MovieDetailMapper;
import dev.mini.minibox.mappers.MovieLikeMapper;
import dev.mini.minibox.mappers.MovieMapper;
import dev.mini.minibox.mappers.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MovieDetailService {
    private final MovieDetailMapper movieDetailMapper;
    private final MovieMapper movieMapper;
    private final MovieLikeMapper movieLikeMapper;
    private final ReviewMapper reviewMapper;

    @Autowired
    public MovieDetailService(MovieDetailMapper movieDetailMapper, MovieMapper movieMapper, MovieLikeMapper movieLikeMapper, ReviewMapper reviewMapper) {
        this.movieDetailMapper = movieDetailMapper;
        this.movieMapper = movieMapper;
        this.movieLikeMapper = movieLikeMapper;
        this.reviewMapper = reviewMapper;
    }

    public MovieDetailEntity getMovieDetail(String movieCd) {
        // DB 조회 영화 객체 반환
        return movieDetailMapper.selectMovieByMovieCd(movieCd);
    }

    // 영화별 포스터 url 리스트
    public String getMoviePosters(String movieCd) {
        return movieDetailMapper.selectMoviePosterByMovieCd(movieCd);
    }

    // 영화 순위 가져오기
    public String getMovieBoxOffice(String movieCd) {
        if (movieDetailMapper.selectMovieBoxOffice(movieCd) != null) {
            return movieDetailMapper.selectMovieBoxOffice(movieCd);
        } else {
            return "-";
        }
    }

    // 누적관객수 가져오기
    public String getMovieAudiAcc(String movieCd) {
        String audiAcc = this.movieDetailMapper.selectMovieAudiAcc(movieCd);
        if (audiAcc != null) {
            // 100000000 -> 100,000,000 변환
            return audiAcc.replaceAll("(?<=\\d)(?=(\\d{3})+$)", ",");
        } else {
            return "0";
        }
    }

    // 좋아요 수 가져오기
    public Map<Integer, Integer> getMovieLike() {
        Map<Integer, Integer> movieLikeMap = new HashMap<>();
        for (int movieId : getAllMovieIdsAtMovies()) {
            int movieLike = movieLikeMapper.movieLikeCountByMovieId(movieId) + 50; // 기본 50에서 시작
            movieLikeMap.put(movieId, movieLike);
        }
        return movieLikeMap;
    }

    // 관람평 수 가져오기
    public Map<String, Integer> getReviewCount() {
        Map<String, Integer> reviewCountMap = new HashMap<>();
        for (String movieCd : getAllMovieCdsAtMovies()) {
            int reviewCount = reviewMapper.reviewCountByMovieCd(movieCd);
            reviewCountMap.put(movieCd, reviewCount);
        }
        return reviewCountMap;
    }

    // 예매율 가져오기
    public Map<String, Double> getReservePer() {
        Map<String, Double> reservePerMap = new HashMap<>();
        for (String movieNm : getAllMovieNms()) {
            int reserveCountByMovieNm = movieDetailMapper.selectReserveCountByMovieNm(movieNm);
            int reserveCountAll = movieDetailMapper.selectReserveCountAll();
            // 실수 계산을 위해 (double)로 형변환
            double reservePer = ((double) reserveCountByMovieNm / reserveCountAll) * 100;
            // 소수점 둘째 자리에서 반올림
            reservePer = Math.round(reservePer * 100) / 100.0;
            reservePerMap.put(movieNm, reservePer);
        }
        return reservePerMap;
    }

    // 영화진흥 위원회 통합전산망제공 기준 날짜 (어제)
    public String getStandardDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        calendar.add(Calendar.DATE, -1);
        return formatter.format(calendar.getTime());
    }

    // 영화 관람가
    public String getWatchGrade(String movieCd) {
        String watchGradeNm = this.movieDetailMapper.selectWatchGrade(movieCd);
        if (watchGradeNm != null) {
            String grade;
            if (watchGradeNm.contains("전체관람가")) {
                grade = "age-all";
            } else if (watchGradeNm.contains("12세이상관람가")) {
                grade = "age-12";
            } else if (watchGradeNm.contains("15세이상관람가")) {
                grade = "age-15";
            } else if (watchGradeNm.contains("청소년관람불가")) {
                grade = "age-19";
            } else {
                grade = "";
            }
            return grade;
        } else {
            return "미정";
        }
    }


    public List<String> posterList(String movieCd) {
        return Arrays.asList(getMoviePosters(movieCd).split(",\\s*"));
    }

    public List<Integer> getAllMovieIdsAtMovies() {
        return movieMapper.selectAllMovieIdsAtMovies();
    }

    public List<String> getAllMovieCdsAtMovies() {
        return movieMapper.selectAllMovieCdsAtMovies();
    }

    public List<String> getAllMovieNms() {
        return movieDetailMapper.selectAllMovieNms();
    }

    public MovieDetailEntity findByMovieCd(String movieCd) {
        return movieDetailMapper.findByMovieCd(movieCd);
    }

    public void saveMovieInfo(MovieDetailEntity movieDetailEntity) {
        movieDetailMapper.insertMovieInfo(movieDetailEntity);
    }

}




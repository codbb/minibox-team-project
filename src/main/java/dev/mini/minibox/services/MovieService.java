package dev.mini.minibox.services;

import dev.mini.minibox.entities.MovieBoxOffice;
import dev.mini.minibox.entities.MovieDetailEntity;
import dev.mini.minibox.entities.MovieEntity;
import dev.mini.minibox.mappers.MovieLikeMapper;
import dev.mini.minibox.mappers.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieMapper movieMapper;
    private final MovieLikeMapper movieLikeMapper;

    public MovieDetailEntity[] getOverseasMovies() {return movieMapper.selectOverseasMovies();}

    public MovieDetailEntity[] getDomesticMovies() {return movieMapper.selectDomesticMovies();}

    public MovieDetailEntity[] getFilmMovies() {
        return movieMapper.selectFilmMovies();
    }

    public MovieDetailEntity[] getComingSoonMovies() {
        return movieMapper.selectComingSoonMovies();
    }

    public MovieBoxOffice[] getMovieBoxOffices(Boolean isMainPage) {
        return movieMapper.selectMoviesByRank(isMainPage);
    }

    public MovieDetailEntity[] getAllMovies() {
        return movieMapper.selectMovies();
    }

    public MovieEntity findByMovieCd(String movieCd) {
        return movieMapper.findByMovieCd(movieCd);
    }

    // 개봉상태 업데이트
    public void updateMovieStatuses() {
        movieMapper.updateMovieStatus();
    }

    // 박스오피스 영화별 포스터 URL 리스트
    public List<List<?>> getPostersByMovie() {
        List<String> postersList = movieMapper.selectAllPosters();

        return postersList.stream()
                .map(posters -> {
                    if (posters == null || posters.isEmpty()) return new ArrayList<>();
                    return Arrays.stream(posters.split(", "))
                            .map(String::trim)
                            .collect(Collectors.toList());
                })
                .collect(Collectors.toList());
    }

    // 박스오피스 영화별 메인 포스터 가져오기
    public Map<String,String> getMainPoster() {
        Map<String,String> posterMap = new HashMap<>();     // 영화코드에 맞는 메인포스터를 가져오기 위해 Map 사용
        List<List<?>> postersList = getPostersByMovie();    // 영화별 포스터 url 리스트 -> [[영화1 포스터 리스트], [영화2 포스터 리스트], ... , [영화 10 포스터 리스트]]
        int i = 0;
        for (String movieCd : getAllMovieCds()) {                    // 영화 1위 ~ 10위의 영화코드
            List<?> posters = postersList.get(i);                    // 순위 (i+1)위의 영화 포스터 리스트
            String[] mainPosters = new String[postersList.size()];   // 메인포스터 리스트 생성
            mainPosters[i] = (String)posters.get(0);                 // 리스트에 1~10위 영화 메인 포스터 추가

            posterMap.put(movieCd, mainPosters[i]);         // Map<영화코드, 영화 메인 포스터>
            i++;
        }
        return posterMap;
    }

    // 영화별 포스터 리스트
    public List<String> getPosterListAtMovies(String movieCd) {
        String urls = movieMapper.selectMoviePoster(movieCd);
        return Arrays.asList(urls.split(","));
    }

    // 영화별 메인 포스터 가져오기
    public Map<String, String> getMainPosterAtMovies() {
        Map<String, String> posterMap = new HashMap<>();
        for (String movieCd : getAllMovieCdsAtMovies()) {
            posterMap.put(movieCd, getPosterListAtMovies(movieCd).get(0));
        }
        return posterMap;
    }

    // 영화별 관람등급 가져오기
    public Map<String, String> getGrade() {
        Map<String, String> gradeMap = new HashMap<>(); //  영화코드에 맞는 관람등급을 가져오기 위해 Map 사용
        String grade;
        for (String movieCd : getAllMovieCdsAtMovies()) {
            String watchGrade = movieMapper.selectWatchGradeByMovieCd(movieCd);
            if (watchGrade.contains("전체관람가")) {
                grade = "age-all";
            } else if (watchGrade.contains("12세이상관람가")) {
                grade = "age-12";
            } else if (watchGrade.contains("15세이상관람가")) {
                grade = "age-15";
            } else if (watchGrade.contains("청소년관람불가")) {
                grade = "age-19";
            } else {
                grade = "age-none";
            }
            gradeMap.put(movieCd, grade);
        }
        return gradeMap;
    }

    // 영화별 개봉상태 가져오기
    public Map<String, String> getPrdt() {
        Map<String, String> prdtMap = new HashMap<>(); //  영화코드에 맞는 개봉상태를 가져오기 위해 Map 사용
        String prdt;
        for (String movieCd : getAllMovieCdsAtMovies()) {
            String prdtStat = movieMapper.selectPrdtStatNmByMovieCd(movieCd); // 개봉상태
            String showTypes = movieMapper.selectShowTypesByMovieCd(movieCd); // 상영타입
            if (prdtStat.trim().equals("개봉")) {
                if (showTypes.contains("DOLBYCINEMA")) { // DOLBY CINEMA 있을 경우 DOLBY 버튼 포함
                    prdt = "movieStat3";
                } else {
                    prdt = "movieStat4";
                }
            } else if (prdtStat.trim().equals("개봉예정")) {
                prdt = "movieStat2";
            } else if (prdtStat.trim().equals("개봉준비")) {
                prdt = "movieStat1";
            } else if (prdtStat.trim().equals("기타")) {
                prdt = "movieStat1";
            } else {
                prdt = "movieStat5";
            }
            prdtMap.put(movieCd, prdt);
        }
        return prdtMap;
    }

    // 영화별 좋아요 수 가져오기
    public Map<Integer, Integer> getMovieLike() {
        Map<Integer, Integer> movieLikeMap = new HashMap<>();
        for (int movieId : getAllMovieIdsAtMovies()) {
            int movieLike = movieLikeMapper.movieLikeCountByMovieId(movieId) + 50; // 기본 50에서 시작
            movieLikeMap.put(movieId, movieLike);
        }
        return movieLikeMap;
    }

    public void saveMovie(MovieEntity movieEntity) {
        movieMapper.insertMovieCd(movieEntity);
    }

    public List<String> getAllMovieCds() {
        return movieMapper.selectAllMovieCds();
    }

    public List<String> getAllMovieCdsAtMovies() {
        return movieMapper.selectAllMovieCdsAtMovies();
    }

    public List<Integer> getAllMovieIdsAtMovies() {
        return movieMapper.selectAllMovieIdsAtMovies();
    }

    public List<String> getAllMovieNms() { return movieMapper.selectAllMovieNms(); }

    public List<String> getAllOpenDts() {
        return movieMapper.selectAllOpenDts();
    }
}

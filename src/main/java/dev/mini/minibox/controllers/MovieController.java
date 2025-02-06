package dev.mini.minibox.controllers;

import dev.mini.minibox.services.MovieDetailService;
import dev.mini.minibox.services.MovieLikeService;
import dev.mini.minibox.services.MovieService;
import dev.mini.minibox.services.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/movie")
public class MovieController {

    private final MovieService movieService;
    private final MovieLikeService movieLikeService;
    private final MovieDetailService movieDetailService;
    private final ReviewService reviewService;

    @Autowired
    public MovieController(MovieService movieService, MovieLikeService movieLikeService, MovieDetailService movieDetailService, ReviewService reviewService) {
        this.movieService = movieService;
        this.movieLikeService = movieLikeService;
        this.movieDetailService = movieDetailService;
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getAllMovie() {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("movieBoxOffices", this.movieService.getMovieBoxOffices(false));
            // 전체영화 검색 시 영화 리스트
            modelAndView.addObject("allMovies", this.movieService.getAllMovies());
            // 영화 메인 포스터 가져오기
            modelAndView.addObject("mainPosterMap", this.movieService.getMainPoster());
            // 영화 메인 포스터 가져오기
            modelAndView.addObject("mainPosterAtMoviesMap", this.movieService.getMainPosterAtMovies());
            // 영화 관람가
            modelAndView.addObject("gradeMap", this.movieService.getGrade());
            // 영화 개봉 상태
            modelAndView.addObject("prdtStatMap", this.movieService.getPrdt());
            // 영화 좋아요 수
            modelAndView.addObject("movieLikeMap", this.movieService.getMovieLike());
            // 총 관람평점
            modelAndView.addObject("avgRatingMap", this.reviewService.getAvgRatingByMovieCd());
            // 예매율
            modelAndView.addObject("reservePerMap", this.movieDetailService.getReservePer());

            modelAndView.setViewName("movie/movie");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/comingsoon", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getComingSoonMovie() {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("comingSoonMovies", this.movieService.getComingSoonMovies());
            // 영화 메인 포스터 가져오기
            modelAndView.addObject("mainPosterAtMoviesMap", this.movieService.getMainPosterAtMovies());
            // 영화 관람가
            modelAndView.addObject("gradeMap", this.movieService.getGrade());
            // 영화 개봉 상태
            modelAndView.addObject("prdtStatMap", this.movieService.getPrdt());
            // 영화 좋아요 수
            modelAndView.addObject("movieLikeMap", this.movieService.getMovieLike());
            // 총 관람평점
            modelAndView.addObject("avgRatingMap", this.reviewService.getAvgRatingByMovieCd());
            // 예매율
            modelAndView.addObject("reservePerMap", this.movieDetailService.getReservePer());

            modelAndView.setViewName("movie/comingsoon");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/film", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getFilmMovie() {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("filmMovies", this.movieService.getFilmMovies());
            // 영화 메인 포스터 가져오기
            modelAndView.addObject("mainPosterAtMoviesMap", this.movieService.getMainPosterAtMovies());
            // 영화 관람가
            modelAndView.addObject("gradeMap", this.movieService.getGrade());
            // 영화 개봉 상태
            modelAndView.addObject("prdtStatMap", this.movieService.getPrdt());
            // 영화 좋아요 수
            modelAndView.addObject("movieLikeMap", this.movieService.getMovieLike());
            // 총 관람평점
            modelAndView.addObject("avgRatingMap", this.reviewService.getAvgRatingByMovieCd());
            // 예매율
            modelAndView.addObject("reservePerMap", this.movieDetailService.getReservePer());

            modelAndView.setViewName("movie/film");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/domestic", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getDomesticMovie() {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("domesticMovies", this.movieService.getDomesticMovies());
            // 영화 메인 포스터 가져오기
            modelAndView.addObject("mainPosterAtMoviesMap", this.movieService.getMainPosterAtMovies());
            // 영화 관람가
            modelAndView.addObject("gradeMap", this.movieService.getGrade());
            // 영화 개봉 상태
            modelAndView.addObject("prdtStatMap", this.movieService.getPrdt());
            // 영화 좋아요 수
            modelAndView.addObject("movieLikeMap", this.movieService.getMovieLike());
            // 총 관람평점
            modelAndView.addObject("avgRatingMap", this.reviewService.getAvgRatingByMovieCd());
            // 예매율
            modelAndView.addObject("reservePerMap", this.movieDetailService.getReservePer());

            modelAndView.setViewName("movie/domestic");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/overseas", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getOverseasMovie() {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("overseasMovies", this.movieService.getOverseasMovies());
            // 영화 메인 포스터 가져오기
            modelAndView.addObject("mainPosterAtMoviesMap", this.movieService.getMainPosterAtMovies());
            // 영화 관람가
            modelAndView.addObject("gradeMap", this.movieService.getGrade());
            // 영화 개봉 상태
            modelAndView.addObject("prdtStatMap", this.movieService.getPrdt());
            // 영화 좋아요 수
            modelAndView.addObject("movieLikeMap", this.movieService.getMovieLike());
            // 총 관람평점
            modelAndView.addObject("avgRatingMap", this.reviewService.getAvgRatingByMovieCd());
            // 예매율
            modelAndView.addObject("reservePerMap", this.movieDetailService.getReservePer());

            modelAndView.setViewName("movie/overseas");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 영화 좋아요 기능
    @RequestMapping(value = {"", "/comingsoon", "/film", "domestic", "/overseas"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleMovieLike(@RequestParam(value = "movieId") int movieId,
                                                               @RequestParam(value = "userId") String userId,
                                                               @RequestParam(value = "like") boolean like) {
        Map<String, Object> response = new HashMap<>();
        response.put("result", movieLikeService.toggleMovieLike(movieId, userId, like));
        response.put("movieId", movieId);
        response.put("userId", userId);
        response.put("like", like);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = {"", "/comingsoon", "/film", "domestic", "/overseas"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Integer>> getLikedMovies(@RequestParam(value = "userId") String userId) {
        return ResponseEntity.ok(this.movieLikeService.getMovieLikeList(userId));
    }
}

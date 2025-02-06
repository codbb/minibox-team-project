package dev.mini.minibox.controllers;

import dev.mini.minibox.entities.ReviewEntity;
import dev.mini.minibox.services.MovieDetailService;
import dev.mini.minibox.services.MovieLikeService;
import dev.mini.minibox.services.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/movie-detail")
public class MovieDetailController {

    private final MovieDetailService movieDetailService;
    private final ReviewService reviewService;
    private final MovieLikeService movieLikeService;

    @Autowired
    public MovieDetailController(MovieDetailService movieDetailService, ReviewService reviewService, MovieLikeService movieLikeService) {
        this.movieDetailService = movieDetailService;
        this.reviewService = reviewService;
        this.movieLikeService = movieLikeService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getMovieDetail(@RequestParam(value = "movieCd") String movieCd) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("movieDetail", this.movieDetailService.getMovieDetail(movieCd));
            // 영화 순위
            modelAndView.addObject("rank", this.movieDetailService.getMovieBoxOffice(movieCd));
            // 누적관객수
            modelAndView.addObject("formattedAudiAcc", this.movieDetailService.getMovieAudiAcc(movieCd));
            // 기준 날짜
            modelAndView.addObject("standardDate", this.movieDetailService.getStandardDate());
            // 영화 관람가
            modelAndView.addObject("watchGrade", this.movieDetailService.getWatchGrade(movieCd));
            // 영화 포스터 리스트
            modelAndView.addObject("posterList", this.movieDetailService.posterList(movieCd));
            // 영화 좋아요 수
            modelAndView.addObject("movieLikeMap", this.movieDetailService.getMovieLike());
            // 관람평 수
            modelAndView.addObject("reviewCountMap", this.movieDetailService.getReviewCount());
            // 총 관람평점
            modelAndView.addObject("avgRatingMap", this.reviewService.getAvgRatingByMovieCd());
            // 예매율
            modelAndView.addObject("reservePerMap", this.movieDetailService.getReservePer());
            modelAndView.setViewName("movie/movie-detail");
            return modelAndView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 관람평 작성 (POST 요청)
    @RequestMapping(value = "/comment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postReviewIndex(ReviewEntity reviewEntity) {
        reviewEntity.setIndex(this.reviewService.findAll().length);
        Map<String, Object> response = new HashMap<>();
        response.put("result", this.reviewService.writeReviewResult(reviewEntity));
        response.put("review", reviewEntity);
        return ResponseEntity.ok(response);
    }

    // 관람평 목록 조회 (GET 요청)
    @RequestMapping(value = "/comment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ReviewEntity[]> getReviews(@RequestParam(value = "movieCd") String movieCd) {
        ReviewEntity[] reviews = this.reviewService.getReviewByMovieCd(movieCd);
        if (reviews == null || reviews.length == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(reviews);
    }

    // 관람평 삭제 (DELETE 요청)
    @RequestMapping(value = "/comment/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteReview(@RequestParam(value = "index", required = false, defaultValue = "0") int index) {
        // 전달받은 review index 를 통해 댓글을 삭제 처리
        boolean isDeleted = reviewService.deleteReviewByIndex(index);
        if (isDeleted) {
            return ResponseEntity.ok("관람평이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("관람평을 찾을 수 없습니다.");
        }
    }

    // 영화 좋아요 기능
    @RequestMapping(value = "/like", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @RequestMapping(value = "/like", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Integer>> getLikedMovies(@RequestParam(value = "userId") String userId) {
        return ResponseEntity.ok(this.movieLikeService.getMovieLikeList(userId));
    }
}

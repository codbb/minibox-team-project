package dev.mini.minibox.services;

import dev.mini.minibox.entities.ReviewEntity;
import dev.mini.minibox.mappers.MovieMapper;
import dev.mini.minibox.mappers.ReviewMapper;
import dev.mini.minibox.vos.ReviewVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final MovieMapper movieMapper;

    @Autowired
    public ReviewService(ReviewMapper reviewMapper, MovieMapper movieMapper) {
        this.reviewMapper = reviewMapper;
        this.movieMapper = movieMapper;
    }

    public ReviewEntity[] findAll() {
        return this.reviewMapper.selectAllReview();
    }

    public ReviewEntity[] getReviewByMovieCd(String movieCd) {
        if (movieCd == null) {
            return null;
        }
        ReviewEntity[] reviews = this.reviewMapper.selectReviewByMovieCd(movieCd);
        for (ReviewEntity review : reviews) {
            review.setMovieCd(movieCd);
        }
        return reviews;
    }

    // 관람평점 평균
    public Map<String, Double> getAvgRatingByMovieCd() {
        Map<String, Double> avgRatingByMovieCd = new HashMap<>();
        for (String movieCd : getAllMovieCdsAtMovies()) {
            List<Integer> ratingList = this.reviewMapper.selectRatingsByMovieCd(movieCd);
            if (ratingList == null || ratingList.isEmpty()) {
                avgRatingByMovieCd.put(movieCd, 0.0);
            } else {
                double sum = 0.0;
                for (int rating : ratingList) {
                    sum += rating;
                }
                double avg = sum / ratingList.size();
                avg = Math.round(avg * 10) / 10.0; // 소수점 첫째 자리까지 반올림
                avgRatingByMovieCd.put(movieCd, avg);
            }
        }
        return avgRatingByMovieCd;
    }

    public boolean writeReviewResult(ReviewEntity reviewEntity) {
        if (reviewEntity == null
                || reviewEntity.getMovieCd() == null || reviewEntity.getMovieCd().isEmpty()
                || reviewEntity.getNickname() == null || reviewEntity.getNickname().isEmpty()
                || reviewEntity.getRating() <= 0 || reviewEntity.getRating() > 10
                || reviewEntity.getKeyword() == null || reviewEntity.getKeyword().isEmpty()
                || reviewEntity.getContent() == null || reviewEntity.getContent().isEmpty()) {
            return false;
        }
        reviewEntity.setLike(0);
        reviewEntity.setCreatedAt(LocalDateTime.now());
        return this.reviewMapper.insertReview(reviewEntity) > 0;
    }

    public boolean deleteReviewByIndex(int index) {
        // 관람평 존재 여부 확인
        if (reviewMapper.existsReviewByIndex(index)) {
            // 관람평 삭제
            return reviewMapper.deleteReviewByIndex(index) > 0;
        } else {
            return false;
        }
    }

    public List<String> getAllMovieCdsAtMovies() {
        return movieMapper.selectAllMovieCdsAtMovies();
    }

    public ReviewVo[] getReviewByUserEmail(String userEmail) {
        return this.reviewMapper.selectReviewsByUserEmail(userEmail);
    }
}

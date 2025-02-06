package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.ReviewEntity;
import dev.mini.minibox.vos.ReviewVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {
    int insertReview(ReviewEntity reviewEntity);

    ReviewEntity[] selectAllReview();

    ReviewEntity selectReviewByIndex(@Param("index") int index);

    // 관람평 존재 여부 확인
    boolean existsReviewByIndex(@Param("index") int index);

    // 관람평 삭제
    int deleteReviewByIndex(@Param("index") int index);

    ReviewEntity[] selectReviewByMovieCd(@Param("movieCd") String movieCd);

    int reviewCountByMovieCd(@Param("movieCd") String movieCd);

    List<Integer> selectRatingsByMovieCd(@Param("movieCd") String movieCd);

    ReviewVo[] selectReviewsByUserEmail(@Param("userEmail") String userEmail);
}

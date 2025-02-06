package dev.mini.minibox.mappers;

import dev.mini.minibox.vos.MovieLikeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MovieLikeMapper {
    // 찜 추가
    int insertMovieLike(Map<String, Object> params);
    // 찜 해제
    int deleteMovieLike(Map<String, Object> params);

    // 영화별 좋아요 수 가져오기
    int movieLikeCountByMovieId(@Param("movieId") int movieId);

    // 유저별 찜한 영화 리스트 가져오기
    List<Integer> selectMovieLikesByUserId(@Param("userId") String userId);

    MovieLikeVo[] selectMovieLikesByUserEmail(String userEmail);
}

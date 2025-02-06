package dev.mini.minibox.services;

import dev.mini.minibox.mappers.MovieLikeMapper;
import dev.mini.minibox.vos.MovieLikeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieLikeService {

    private final MovieLikeMapper movieLikeMapper;

    @Autowired
    public MovieLikeService(MovieLikeMapper movieLikeMapper) {
        this.movieLikeMapper = movieLikeMapper;
    }

    public boolean toggleMovieLike(int movieId, String userId, boolean like) {
        Map<String, Object> params = new HashMap<>();
        params.put("movieId", movieId);
        params.put("userId", userId);

        if (like) {
            return movieLikeMapper.insertMovieLike(params) > 0;
        } else {
            return movieLikeMapper.deleteMovieLike(params) > 0;
        }
    }

    public List<Integer> getMovieLikeList(String userId) {
        return movieLikeMapper.selectMovieLikesByUserId(userId);
    }

    public MovieLikeVo[] getMovieLikesByUserEmail(String userEmail) {
        return movieLikeMapper.selectMovieLikesByUserEmail(userEmail);
    }
}

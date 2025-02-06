package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.TheaterDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TheaterDetailMapper { // 극장의 지점의 상세페이지를 나타내는곳
    int insertTheaterDetail(TheaterDetailEntity theaterDetail);

    TheaterDetailEntity selectTheaterDetail(@Param("brchNo") String brchNo);

    int update(TheaterDetailEntity detail);

    // 12-17
    int insertDetail(TheaterDetailEntity detail);

    int updatedTheaterBrchDetail(TheaterDetailEntity detail);

    TheaterDetailEntity selectByDetailCode(String brchNo);

    // 모든 영화관 정보를 조회하는 SQL 메서드
    TheaterDetailEntity[] getAllTheaterDetails();

}

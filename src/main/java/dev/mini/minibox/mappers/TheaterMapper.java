package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.TheaterEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TheaterMapper { // 극장의 지점을 나타내는곳
    int insertTheater(TheaterEntity theater);

    TheaterEntity selectTheaters(@Param("brchNo") String brchNo);

    TheaterEntity selectByBrchNo(String brchNo);
}



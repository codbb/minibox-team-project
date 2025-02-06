package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.ScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    ScheduleEntity[] selectSchedules();


    List<ScheduleEntity> selectSchedule(@Param("date") String date,
                                        @Param("areaNo") String areaNo,
                                        @Param("brchNo") String brchNo,
                                        @Param("movieNm") String movieNm);
}
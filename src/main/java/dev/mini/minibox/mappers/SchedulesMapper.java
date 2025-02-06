package dev.mini.minibox.mappers;

import dev.mini.minibox.vos.SchedulesVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchedulesMapper {

    // 상영 일정을 DB에 삽입하는 메서드
    void insertSchedule(SchedulesVo schedule);
}

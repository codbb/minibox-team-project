package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.SeatEntity;
import dev.mini.minibox.vos.SeatVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SeatMapper {
    SeatEntity[] selectSeats();

    SeatEntity selectSeatById(int seatId);

    SeatVo[] selectSeatVos(@Param("scheduleId") int scheduleId);
}

package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.BookingEntity;
import dev.mini.minibox.vos.BookingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookingMapper {

    BookingEntity selectSeatBooking(@Param("seatId") int seatId, @Param("scheduleId") int scheduleId);

    int insertBooking(BookingEntity bookingEntity);

    BookingVo[] selectBookingsByEmail(String userEmail);

    BookingVo selectBookingsByEmailPaymentId(String userEmail, int paymentId);
}

package dev.mini.minibox.services;

import dev.mini.minibox.entities.BookingEntity;
import dev.mini.minibox.entities.PaymentEntity;
import dev.mini.minibox.entities.ScheduleEntity;
import dev.mini.minibox.entities.SeatEntity;
import dev.mini.minibox.exceptions.TransactionalException;
import dev.mini.minibox.mappers.BookingMapper;
import dev.mini.minibox.mappers.PaymentMapper;
import dev.mini.minibox.mappers.ScheduleMapper;
import dev.mini.minibox.mappers.SeatMapper;
import dev.mini.minibox.results.CommonResult;
import dev.mini.minibox.results.Result;
import dev.mini.minibox.vos.BookingVo;
import dev.mini.minibox.vos.SeatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
public class BookingService {
    private final ScheduleMapper scheduleMapper;
    private final SeatMapper seatMapper;
    private final BookingMapper bookingMapper;
    private final PaymentMapper paymentMapper;

    @Autowired
    public BookingService(ScheduleMapper scheduleMapper, SeatMapper seatMapper, BookingMapper bookingMapper, PaymentMapper paymentMapper) {
        this.scheduleMapper = scheduleMapper;
        this.seatMapper = seatMapper;
        this.bookingMapper = bookingMapper;
        this.paymentMapper = paymentMapper;
    }

    public ScheduleEntity[] getSchedules() {
        return this.scheduleMapper.selectSchedules();
    }

    public SeatEntity[] getSeats() {
        return this.seatMapper.selectSeats();
    }

    public BookingVo[] getBookingsByEmail(String userEmail) {
        return this.bookingMapper.selectBookingsByEmail(userEmail);
    }

    public List<ScheduleEntity> getSchedule(String date, String areaNo, String brchNo, String movieNm) {
        return scheduleMapper.selectSchedule(date, areaNo, brchNo, movieNm);
    }

    // 여러 좌석 결제 및 예매
    @Transactional
    public CommonResult reservation(String userEmail, int scheduleId, List<Integer> seatIds, int charge) {
        // 결제 파트 먼저
        if (userEmail == null) {
            return CommonResult.FAILURE_UNSIGNED;
        }
        PaymentEntity payment = new PaymentEntity();
        payment.setCharge(charge);
        payment.setUserEmail(userEmail);
        payment.setCreatedAt(now());
        payment.setCancelled(false);
        if (this.paymentMapper.insertPayment(payment) == 0) {
            throw new TransactionalException();
        }

        // 좌석별 예약 처리
        for (int seatId : seatIds) {
            BookingEntity booking = new BookingEntity();
            booking.setScheduleId(scheduleId);
            booking.setUserEmail(userEmail);
            booking.setSeatId(seatId);
            booking.setPaymentId(payment.getId());
            booking.setCreatedAt(now());
            if (this.bookingMapper.insertBooking(booking) == 0) {
                throw new TransactionalException();
            }
        }
        return CommonResult.SUCCESS;
    }

    public SeatVo[] getSeatVos(int scheduleId) {
        return this.seatMapper.selectSeatVos(scheduleId);
    }

    // 결제, 예매 취소
    public Result deletePayment(String userEmail, int paymentId) {
//        BookingVo dbBooking = this.bookingMapper.selectBookingsByEmailPaymentId(userEmail, paymentId);
//        if (dbBooking.getPlayDe().isBefore(LocalDate.now())) {
//            return ValidateEmailTokenResult.FAILURE_EXPIRED;
//        }
        return this.paymentMapper.deletePayment(userEmail, paymentId) == 0? CommonResult.FAILURE : CommonResult.SUCCESS;
    }
}


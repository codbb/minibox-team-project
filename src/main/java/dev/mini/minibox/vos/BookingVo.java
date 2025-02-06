package dev.mini.minibox.vos;

import dev.mini.minibox.entities.BookingEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class BookingVo extends BookingEntity {
    private String areaCd;
    private String areaCdNm;
    private String brchNo;
    private String brchNm;
    private String theabNo;
    private String movieNm;
    private LocalTime playStartTime;
    private LocalTime playEndTime;
    private LocalDate playDe;
    private String seatNo;
}

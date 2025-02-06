package dev.mini.minibox.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class ScheduleEntity {
    private int id;
    private String areaCd;
    private String areaCdNm;
    private String brchNo;
    private String brchNm;
    private String theabNo;
    private String movieNm;
    private String playStartTime;
    private String playEndTime;
    private LocalDate playDe;
}

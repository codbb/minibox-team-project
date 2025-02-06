package dev.mini.minibox.vos;

import dev.mini.minibox.entities.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleVo extends ScheduleEntity {
//    private String areaName;
//    private String branchName;
//    private String theaterName;
//    private String movieName;


    private String areaCd;
    private String areaCdNm;
    private String brchNo;
    private String brchNm;
    private String theabNo;
    private String movieNm;
    private LocalDate playDe;
    private String playStartTime;
    private String playEndTime;
}

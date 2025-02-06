package dev.mini.minibox.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SchedulesEntity {
    private int id;
    private String areaCd;
    private String brchNo;
    private String theabNo;
    private String movieCd;
    private LocalDate playDe;
    private LocalTime playStartTime;
    private LocalTime playEndTime;
}

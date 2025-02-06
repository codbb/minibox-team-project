package dev.mini.minibox.vos;

import dev.mini.minibox.entities.SchedulesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulesVo extends SchedulesEntity {
    private String areaCdNm;
    private String brchNm;
    private String theabNm;
    private String movieNm;
}
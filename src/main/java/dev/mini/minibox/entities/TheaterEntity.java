package dev.mini.minibox.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode(of = {"brchNo"})
public class TheaterEntity { // 극장의 지점을 나타내는곳
    private String areaCode;
    private String name;
    private String brchNo;
}

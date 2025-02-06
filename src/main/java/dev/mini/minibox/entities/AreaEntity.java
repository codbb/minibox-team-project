package dev.mini.minibox.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode(of = {"code"})
public class AreaEntity { // 극장의 지역을 나타내는곳
    private String code;
    private String name;
}
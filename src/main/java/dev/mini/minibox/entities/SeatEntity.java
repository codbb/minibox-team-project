package dev.mini.minibox.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class SeatEntity {
    private int id;
    private String seatNo;
}

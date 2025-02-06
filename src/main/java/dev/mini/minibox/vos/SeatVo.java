package dev.mini.minibox.vos;

import dev.mini.minibox.entities.SeatEntity;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatVo extends SeatEntity {
    private boolean isBooked;
}

package dev.mini.minibox.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class BookingEntity {
    private int id;
    private int scheduleId;
    private String userEmail;
    private int seatId;
    private int paymentId;
    private LocalDateTime createdAt;
}

package dev.mini.minibox.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode (of = {"id"})
public class PaymentEntity {
    private int id;
    private String userEmail;
    private int charge;
    private boolean isCancelled;
    private LocalDateTime createdAt;
}

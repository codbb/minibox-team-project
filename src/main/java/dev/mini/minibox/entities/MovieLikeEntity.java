package dev.mini.minibox.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class MovieLikeEntity {
    private int id;
    private int movieId;
    private String userId;
}

package dev.mini.minibox.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"index"})
public class ImageEntity {
    private int index;
    private byte[] data;
    private String contentType;
    private String name;
}

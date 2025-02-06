package dev.mini.minibox.dtos;

import dev.mini.minibox.results.Result;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReturnDto<TPayload> {
    private Result result;
    private TPayload payload;
}

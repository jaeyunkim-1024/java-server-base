package com.sample.base.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomResponseDto<T> {
    private T data;
}

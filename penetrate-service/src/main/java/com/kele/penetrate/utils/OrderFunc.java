package com.kele.penetrate.utils;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderFunc<T>
{
    private Func<T, Boolean> func;
    private int order;
}

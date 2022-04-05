package com.kele.penetrate.utils;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@SuppressWarnings("unused")
public class OrderFunc<T>
{
    private Func<T, Boolean> func;
    private int order;
}

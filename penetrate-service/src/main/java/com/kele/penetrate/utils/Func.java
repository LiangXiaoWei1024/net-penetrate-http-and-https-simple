package com.kele.penetrate.utils;

public interface Func<T,R extends Boolean>
{
    R func(T t);
}

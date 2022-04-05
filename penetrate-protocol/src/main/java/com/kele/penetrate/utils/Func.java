package com.kele.penetrate.utils;

@SuppressWarnings("unused")
public interface Func<T,R extends Boolean>
{
    R func(T t);
}

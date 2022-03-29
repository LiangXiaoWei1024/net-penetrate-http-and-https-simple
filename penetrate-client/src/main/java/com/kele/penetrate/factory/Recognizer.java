package com.kele.penetrate.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Recognizer
{
}

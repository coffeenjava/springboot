package com.brian.support.annotation;

import com.brian.support.util.masking.Masker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 데이터 masking 여부 판단용
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Masking {
    Class<? extends Masker> type();
}

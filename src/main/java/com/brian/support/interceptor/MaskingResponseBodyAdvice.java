package com.brian.support.interceptor;

import com.brian.support.annotation.Masking;
import com.brian.support.util.masking.Masker;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;

/**
 * 응답 내려보내기 전처리
 * 필드에 @Masking 이 존재할 경우 마스킹 처리
 */
@ControllerAdvice
public class MaskingResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true; // 전체 응답 대상
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Arrays.stream(body.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Masking.class) != null) // Masking 어노테이션 존재여부로 필터링
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(body);
                        Masking masking = field.getAnnotation(Masking.class);
                        String maskedString = Masker.mask(masking, value);
                        field.set(body, maskedString);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return body;
    }
}

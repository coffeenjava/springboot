package com.brian.support.interceptor;

import com.brian.controller.dto.BaseDto;
import com.brian.support.annotation.ValidateAll;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

@ControllerAdvice
public class CustomRequestBodyAdvice extends RequestBodyAdviceAdapter {

	@Override
	public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
		return BaseDto.class.isAssignableFrom(methodParameter.getParameterType());
	}

	@Override public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		BaseDto dto = (BaseDto) body;
		BaseDto.validate(dto, BaseDto::validate);

		AnnotatedElement annotatedElement = parameter.getAnnotatedElement();
		ValidateAll annValdate = annotatedElement.getAnnotation(ValidateAll.class);
		if (annValdate != null) {
			BaseDto.validate(dto, BaseDto::validateAll);
		}

		return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
	}
}

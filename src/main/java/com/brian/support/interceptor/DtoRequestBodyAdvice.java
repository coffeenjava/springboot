package com.brian.support.interceptor;

import com.brian.controller.dto.BaseDto;
import com.brian.support.util.ObjectUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * auto dto validation
 */
@ControllerAdvice
public class DtoRequestBodyAdvice extends RequestBodyAdviceAdapter {

	@Override
	public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
		/**
		 * BaseDto or Collection<BaseDto>
		 */
		Class<?> cls = methodParameter.getParameterType();
		if (Collection.class.isAssignableFrom(cls)) {
			cls = ObjectUtil.getClassTypeFromCollection(type);
		}
		return BaseDto.class.isAssignableFrom(cls);
	}

	@Override public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		/**
		 * run validate method in dto
		 */
		BaseDto.validate(body);

		return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
	}
}

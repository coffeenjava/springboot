package com.brian.support.aop;

import com.brian.controller.dto.BaseDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DtoValidationAspect {

	@Around("@annotation(com.brian.support.annotation.ValidateAll)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] objects = joinPoint.getArgs();
		if (objects.length > 0) {
			for (Object o : objects) {
				if (BaseDto.class.isAssignableFrom(o.getClass()) == false) continue;
				BaseDto.validate((BaseDto) o, BaseDto::validateAll);
			}
		}

		return joinPoint.proceed();
	}
}

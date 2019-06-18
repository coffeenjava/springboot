package com.brian.support.util;

import com.brian.controller.dto.BaseDto;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectUtil {

	public static <T> T copyFields(Object source, T dest) {
		Assert.notNull(source, "source must not be null");
		Assert.notNull(dest, "target must not be null");

		List<Field> srcFieldList = Arrays.asList(source.getClass().getDeclaredFields())
				.stream()
				.collect(Collectors.toList());

		Map<String,Field> destFieldMap = Arrays.asList(dest.getClass().getDeclaredFields())
				.stream()
				.collect(Collectors.toMap(Field::getName, Function.identity()));

		BeanWrapper sourceWrapper = PropertyAccessorFactory.forBeanPropertyAccess(source);
		BeanWrapper targetWrapper = PropertyAccessorFactory.forBeanPropertyAccess(dest);

		for (Field sf : srcFieldList) {
			String fieldName = sf.getName();
			Object value = sourceWrapper.getPropertyValue(fieldName);

			if (value == null || value instanceof Collection || value instanceof Map
					|| value instanceof BaseDto) continue;

			if (destFieldMap.containsKey(fieldName)) {
				// don't copy if type is not same
//				if (sf.getType() != targetWrapper.getPropertyType(fieldName)) continue;

				targetWrapper.setPropertyValue(fieldName, value);
			}
		}

		return dest;
	}
}

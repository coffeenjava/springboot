package com.brian.controller.dto;

import com.brian.support.model.BaseModel;
import com.brian.support.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.reflect.Field;
import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown=true, value={ "creator", "updater", "createDate", "updateDate" }, allowGetters=true)
public interface BaseDto extends BaseModel {

	default void validate() {}

	/**
	 * dto validator
	 * @see com.brian.support.interceptor.DtoRequestBodyAdvice
	 */
	static void validate(Object o) {
		if (o == null) return;

		if (Collection.class.isAssignableFrom(o.getClass())) {
			Collection dtos = (Collection) o;
			dtos.forEach(dto -> BaseDto.validate(dto));
		} else if (BaseDto.class.isAssignableFrom(o.getClass())){
			BaseDto dto = (BaseDto) o;
			dto.validate();

			Field[] fields = ObjectUtil.getAllFields(dto.getClass()).stream().toArray(Field[]::new);
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(dto);

			for (Field field : fields) {
				if (beanWrapper.isReadableProperty(field.getName()) == false) continue;

				Object fo = beanWrapper.getPropertyValue(field.getName());
				BaseDto.validate(fo);
			}
		}
	}
}

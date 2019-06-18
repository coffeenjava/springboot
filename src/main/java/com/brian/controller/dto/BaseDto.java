package com.brian.controller.dto;

import com.brian.support.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Consumer;

@JsonIgnoreProperties(value={ "creator", "updater", "createDate", "updateDate" }, allowGetters=true)
public interface BaseDto extends BaseModel {

	default void setCreator(String creator) {}
	default void setUpdater(String updater) {}

	default void validate() {}
	default void validateAll() {}

	static void validate(BaseDto dto, Consumer<BaseDto> consumer) {
		consumer.accept(dto);

		Field[] fields = dto.getClass().getDeclaredFields();
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(dto);

		for (Field field : fields) {
			Class cls = beanWrapper.getPropertyType(field.getName());
			if (Collection.class.isAssignableFrom(cls) == false) continue;

			Type type = field.getGenericType();
			if (ParameterizedType.class.isAssignableFrom(type.getClass()) == false) continue;
			if (BaseDto.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]) == false) continue;

			Collection<BaseDto> collection = (Collection<BaseDto>) beanWrapper.getPropertyValue(field.getName());
			if (collection == null) continue;

			for (BaseDto baseDto : collection) {
				BaseDto.validate(baseDto, consumer);
			}
		}
	}
}

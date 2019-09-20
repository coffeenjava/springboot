package com.brian.support.util;

import com.brian.support.annotation.NoCopy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectUtil {

	/**
	 * shallow copy properties
	 *
	 * target
	 *  same name (even if type is not same)
	 *  has getter/setter
	 *
	 * except
	 *  null value
	 *  has @NoCopy
	 */
	public static <T> T copyProperties(Object source, T target) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		List<String> noCopyFields = getAllFields(source.getClass()).stream()
				.filter(f -> f.getAnnotation(NoCopy.class) != null)
				.map(Field::getName)
				.collect(Collectors.toList());

		PropertyDescriptor[] targetPds = Arrays.asList(BeanUtils.getPropertyDescriptors(target.getClass())).stream()
				.filter(pd -> noCopyFields.contains(pd.getName()) == false)
				.toArray(PropertyDescriptor[]::new);

		BeanWrapper targetWrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);

		for(PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if (readMethod != null) {
						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}

							Object value = readMethod.invoke(source);
							if (value == null) continue; // null 제외

							targetWrapper.setPropertyValue(sourcePd.getName(), value);
						} catch (Throwable t) {
							throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", t);
						}
					}
				}
			}
		}

		return target;
	}

	/**
	 * get all fields include inherited
	 */
	public static Collection<Field> getAllFields(Class<?> type) {
		Map<String,Field> fieldMap = new HashMap<>();

		while (type != Object.class) {
			for (Field f : type.getDeclaredFields()) {
				if (fieldMap.containsKey(f.getName())) continue;
				fieldMap.put(f.getName(), f);
			}
			type = type.getSuperclass();
		}

		return fieldMap.values();
	}
}

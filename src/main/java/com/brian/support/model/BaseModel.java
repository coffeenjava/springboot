package com.brian.support.model;

import com.brian.support.util.ObjectUtil;

import java.io.Serializable;

public interface BaseModel extends Serializable {
	default <T> T copyTo(T e) {
		return ObjectUtil.copyProperties(this, e);
	}
}

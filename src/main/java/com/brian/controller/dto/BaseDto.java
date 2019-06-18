package com.brian.controller.dto;

import com.brian.support.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value={ "creator", "updater", "createDate", "updateDate" }, allowGetters=true)
public interface BaseDto extends BaseModel {

	default void setCreator(String creator) {}
	default void setUpdater(String updater) {}
}

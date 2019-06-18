package com.brian.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TestDto implements BaseDto {

	private String name;
	@NotNull
	private Integer age;
	private String creator;
	private String updater;

	@Valid
	private List<TestDto> dtoList = new ArrayList<>();

	@Override
	public void validate() {
		System.out.println("validate OK! - "+name);
	}

	@Override
	public void validateAll() {
		System.out.println("validate All OK! - "+name);
	}
}

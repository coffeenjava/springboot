package com.brian.controller;

import com.brian.controller.dto.TestDto;
import com.brian.support.RequestScopeHolder;
import com.brian.support.annotation.ValidateAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RequestScopeHolder requestScopeHolder;

	@GetMapping
	public void get(@RequestParam String updater) {
		System.out.println("RequestParam ==> "+updater);
		System.out.println("requestScopeHolder ==> "+requestScopeHolder.getUpdater());
	}

	@PostMapping
	@ValidateAll
	public void post(@Valid @RequestBody TestDto dto) {
		System.out.println("RequestParam ==> "+dto.getUpdater());
		System.out.println("requestScopeHolder ==> "+requestScopeHolder.getUpdater());
	}
}

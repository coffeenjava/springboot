package com.brian.support.interceptor;

import com.brian.support.RequestScopeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ControllerInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private RequestScopeHolder requestScopeHolder;

	@Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String updater = request.getParameter("updater");
		// updater must be required exclude GET request
		if (HttpMethod.GET.matches(request.getMethod()) == false && updater == null) throw new RuntimeException("parameter [updater] is required");
		requestScopeHolder.setUpdater(updater);
		return super.preHandle(request, response, handler);
	}
}

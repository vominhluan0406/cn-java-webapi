package com.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.shop.controller.payload.Respone;

@SuppressWarnings("serial")
@ControllerAdvice
public class ApiExceptionHandler extends RuntimeException {

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public Respone proDouctException(Exception ex, WebRequest request) {
		return new Respone(404, "Page Not Found");
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	@ResponseBody
	public Respone methodException(Exception ex, WebRequest request) {
		return new Respone(405, "Method Not Allowed");
	}
}

package com.xiangxing.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangxing.vo.api.ApiResponse;

@ControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler({ Exception.class })
	@ResponseBody
	public ApiResponse handleArrayIndexOutOfBoundsException(Exception e) {
		e.printStackTrace();
		return ApiResponse.getErrorResponse(e.getMessage());
	}
}

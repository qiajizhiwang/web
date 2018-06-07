package com.xiangxing.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.xiangxing.enums.CodeEnum;
import com.xiangxing.service.SchoolService;

/**
 * 
 * @author Administrator
 *
 */
public abstract class BaseController {

	private static final Logger log = LogManager.getLogger(BaseController.class);

	@Resource
	protected SchoolService schoolService;

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	public void writeToOkResponse(JSONObject jsonObject) {
		jsonObject.put("code", CodeEnum.CODE_10000.getMsg());
		writeToResponse(jsonObject, response);
	}

	public void writeToErrorResponse(JSONObject jsonObject) {
		jsonObject.put("code", CodeEnum.CODE_20000.getMsg());
		writeToResponse(jsonObject, response);
	}

	public void writeToParamsErrorResponse(JSONObject jsonObject) {
		jsonObject.put("code", CodeEnum.CODE_30000.getMsg());
		writeToResponse(jsonObject, response);
	}

	public void writeToResponse(Object object, HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			out.print(object);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}

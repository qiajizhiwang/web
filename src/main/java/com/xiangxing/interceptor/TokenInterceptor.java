package com.xiangxing.interceptor;

import java.io.PrintWriter;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xiangxing.vo.api.ApiResponse;

public class TokenInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object arg2,
			Exception arg3) throws Exception {
		TokenManager.clearToken();

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {



		String token = request.getHeader("token");
		if (StringUtils.isEmpty(token) || null == TokenManager.getUser(token)) {
			response.setContentType("application/json; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.print(JSONObject.toJSONString(ApiResponse.getTokenErrorResponse(""),
					SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat));
			writer.close();
			response.flushBuffer();
			return false;
		}
		TokenManager.setToken(token);
		return true;
	}

}

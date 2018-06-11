package com.xiangxing.controller.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.model.Teacher;
import com.xiangxing.model.TeacherExample;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginRequest;
import com.xiangxing.vo.api.LoginResponse;

@Controller("/api")
@RequestMapping
@RestController
public class LoginController {

	@Autowired
	private TeacherMapper teacherMapper;

	private Map<String, String> loginInfos = new ConcurrentHashMap<>();

	@RequestMapping("/login")
	public ApiResponse login(LoginRequest loginRequest) {
		TeacherExample example = new TeacherExample();
		example.createCriteria().andNameEqualTo(loginRequest.getName());

		List teachers = teacherMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(teachers)) {
			Teacher teacher = (Teacher) teachers.get(0);
			if (teacher.getPassword().equals(loginRequest.getPassword())) {
				LoginResponse loginResponse = new LoginResponse();
				String token = UUID.randomUUID().toString().replace("-", "");
				loginResponse.setToken(token);
				loginInfos.put(token, teacher.getTeacherId());
				return loginResponse;
			}
		}
		return ApiResponse.getErrorResponse("登录失败");

	}

}

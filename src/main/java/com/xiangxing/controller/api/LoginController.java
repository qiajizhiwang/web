package com.xiangxing.controller.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentExample;
import com.xiangxing.model.Teacher;
import com.xiangxing.model.TeacherExample;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginRequest;
import com.xiangxing.vo.api.LoginResponse;

@RequestMapping("/api")
@RestController
public class LoginController {

	@Autowired
	private TeacherMapper teacherMapper;

	@Autowired
	private StudentMapper mapper;


	@RequestMapping("/login")
	public ApiResponse login(LoginRequest loginRequest) {

		if (loginRequest.getType() == 1) {
			TeacherExample example = new TeacherExample();
			example.createCriteria().andNameEqualTo(loginRequest.getName());
			List teachers = teacherMapper.selectByExample(example);
			if (!CollectionUtils.isEmpty(teachers)) {
				Teacher teacher = (Teacher) teachers.get(0);
				if (teacher.getPassword().equals(loginRequest.getPassword())) {
					LoginResponse loginResponse = new LoginResponse();
					String token = UUID.randomUUID().toString().replace("-", "");
					loginResponse.setToken(token);
					TokenManager.setUser(token, new LoginInfo(teacher.getId(), 1));
					return loginResponse;
				}
			}
		} else {
			StudentExample example = new StudentExample();
			example.createCriteria().andNameEqualTo(loginRequest.getName());
			List students = mapper.selectByExample(example);
			if (!CollectionUtils.isEmpty(students)) {
				Student student = (Student) students.get(0);
				if (student.getPassword().equals(loginRequest.getPassword())) {
					LoginResponse loginResponse = new LoginResponse();
					String token = UUID.randomUUID().toString().replace("-", "");
					loginResponse.setToken(token);
					TokenManager.setUser(token, new LoginInfo(student.getId(), 2));
					return loginResponse;
				}
			}
		}
		return ApiResponse.getErrorResponse("登录失败");

	}

}

package com.xiangxing.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginRequest;

@RequestMapping("/api/teacher")
@RestController
public class ApiTeacherController {
	
	@Autowired
	private TeacherMapper teacherMapper;

	@Autowired
	private StudentMapper studentMapper;
	
	@RequestMapping("/myStudents")
	public ApiResponse myStudents() {
		LoginInfo info = TokenManager.getNowUser();
		
		return null;
		
	}

}

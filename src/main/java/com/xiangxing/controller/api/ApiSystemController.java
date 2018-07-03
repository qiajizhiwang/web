package com.xiangxing.controller.api;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.AdvertMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.model.Advert;
import com.xiangxing.model.AdvertExample;
import com.xiangxing.model.Message;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentExample;
import com.xiangxing.model.Teacher;
import com.xiangxing.model.TeacherExample;
import com.xiangxing.model.ex.CourseSignPo;
import com.xiangxing.utils.DateUtil;
import com.xiangxing.vo.api.ApiPageResponse;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.CourseSignResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.LoginRequest;
import com.xiangxing.vo.api.LoginResponse;

@RequestMapping("/api")
@RestController
public class ApiSystemController {

	@Autowired
	private TeacherMapper teacherMapper;

	@Autowired
	private StudentMapper studentMapper;

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
			List students = studentMapper.selectByExample(example);
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

	@RequestMapping("/editPassword")
	public ApiResponse editPassword(String oldPassword, String newPassword) {
		LoginInfo info = TokenManager.getNowUser();
		if (info.getType() == 1) {
			Teacher teacher = teacherMapper.selectByPrimaryKey(info.getId());
			if (oldPassword.equals(teacher.getPassword())) {
				teacher.setPassword(newPassword);
				teacherMapper.updateByPrimaryKey(teacher);
				return new ApiResponse();
			}

		} else {
			Student student = studentMapper.selectByPrimaryKey(info.getId());
			if (oldPassword.equals(student.getPassword())) {
				student.setPassword(newPassword);
				studentMapper.updateByPrimaryKey(student);
				return new ApiResponse();
			}
		}
		return ApiResponse.getErrorResponse("旧密码不正确，无法修改");
	}

	@RequestMapping("/logout")
	public ApiResponse logout() {
		String token = TokenManager.getToken();
		TokenManager.removeUser(token);
		return new ApiResponse();
	}

	@Autowired
	AdvertMapper advertMapper;

	@RequestMapping("/advert")
	public ApiPageResponse<Advert> advert(PageRequest pageRequest, HttpServletRequest httpServletRequest) {
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		AdvertExample advertExample = new AdvertExample();
		advertExample.createCriteria().andIdIsNotNull();
		List<Advert> advertExs = advertMapper.selectByExample(advertExample);
		for (Advert advertEx : advertExs) {
			advertEx.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + advertEx.getPath());
		}
		long total = page.getTotal();
		return new ApiPageResponse<Advert>(total, advertExs);
	}
	


}

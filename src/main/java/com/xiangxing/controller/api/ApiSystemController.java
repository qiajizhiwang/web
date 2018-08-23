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
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.AdvertMapper;
import com.xiangxing.mapper.ContactMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.model.Advert;
import com.xiangxing.model.AdvertExample;
import com.xiangxing.model.Contact;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentExample;
import com.xiangxing.model.Teacher;
import com.xiangxing.model.TeacherExample;
import com.xiangxing.utils.MD5Util;
import com.xiangxing.vo.api.ApiPageResponse;
import com.xiangxing.vo.api.ApiResponse;
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
			example.createCriteria().andPhoneEqualTo(loginRequest.getPhone());
			List teachers = teacherMapper.selectByExample(example);
			if (!CollectionUtils.isEmpty(teachers)) {
				Teacher teacher = (Teacher) teachers.get(0);
				if (teacher.getPassword().equals(MD5Util.MD5Encode(loginRequest.getPassword()))) {
					LoginResponse loginResponse = new LoginResponse();
					String token = UUID.randomUUID().toString().replace("-", "");
					loginResponse.setToken(token);
					TokenManager.setUser(token, new LoginInfo(teacher.getId(), 1));
					return loginResponse;
				}
			}
		} else {
			StudentExample example = new StudentExample();
			example.createCriteria().andPhoneEqualTo(loginRequest.getPhone());
			List students = studentMapper.selectByExample(example);
			if (!CollectionUtils.isEmpty(students)) {
				Student student = (Student) students.get(0);
				if (student.getPassword().equals(MD5Util.MD5Encode(loginRequest.getPassword()))) {
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
			if (MD5Util.MD5Encode(oldPassword).equals(teacher.getPassword())) {
				teacher.setPassword(MD5Util.MD5Encode(newPassword));
				teacherMapper.updateByPrimaryKey(teacher);
				return new ApiResponse();
			}

		} else {
			Student student = studentMapper.selectByPrimaryKey(info.getId());
			if (MD5Util.MD5Encode(oldPassword).equals(student.getPassword())) {
				student.setPassword(MD5Util.MD5Encode(newPassword));
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
	public ApiPageResponse<Advert> advert(HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Long shoolId  ;
		if (info.getType() == 1) {
			Teacher teacher = teacherMapper.selectByPrimaryKey(info.getId());
			shoolId = teacher.getSchoolId();
		} else {
			Student student = studentMapper.selectByPrimaryKey(info.getId());
			shoolId = student.getSchoolId();
		}
		Page<?> page = PageHelper.startPage(1, 2, true);
		AdvertExample advertExample = new AdvertExample();
		advertExample.createCriteria().andSchoolIdIsNull();
		List<Advert> advertExs = advertMapper.selectByExample(advertExample);
		for (Advert advertEx : advertExs) {
			advertEx.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + advertEx.getPath());
		}
		 page = PageHelper.startPage(1, 1, true);
		 advertExample = new AdvertExample();
		advertExample.createCriteria().andSchoolIdEqualTo(shoolId);
		List<Advert> advertExs2 = advertMapper.selectByExample(advertExample);
		for (Advert advertEx : advertExs2) {
			advertEx.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + advertEx.getPath());
		}
		advertExs.addAll(advertExs2);
//		long total = page.getTotal();
		return new ApiPageResponse<Advert>(3,advertExs );
	}

	@Autowired
	ContactMapper contactMapper;

	@RequestMapping("/contactMe")
	public ApiResponse contactMe(String text, String phone, String name) {
		Contact contact = new Contact();
		contact.setCreateTime(new Date());
		contact.setName(name);
		contact.setPhone(phone);
		contact.setText(text);
		contactMapper.insert(contact);
		return new ApiResponse();
	}
	
	

}

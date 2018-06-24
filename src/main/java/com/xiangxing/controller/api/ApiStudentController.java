package com.xiangxing.controller.api;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.mapper.ex.CourseMapperEx;
import com.xiangxing.mapper.ex.ProductPoMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.StudentExample;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.SchoolResponse;

@RequestMapping("/api/student")
@RestController
public class ApiStudentController {

	@Autowired
	private TeacherMapper teacherMapper;

	@Autowired
	private StudentMapper mapper;

	@Autowired
	CourseMapperEx courseMapperEx;

	@Autowired
	ProductPoMapper productPoMapper;

	@RequestMapping("/myCourses")
	public ApiResponse myCourses(PageRequest pageRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List courses = courseMapperEx.courseListByStudentId(info.getId());
		long total = page.getTotal();
		return new ApiPageResponse<CourseEx>(total, courses);

	}

	@RequestMapping("/myProducts")
	public ApiResponse myProducts(PageRequest pageRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List product = productPoMapper.getListByStudentId(info.getId());
		long total = page.getTotal();
		return new ApiPageResponse<CourseEx>(total, product);

	}

	@Autowired
	SchoolMapper schoolMapper;

	@RequestMapping("/mySchool")
	public ApiResponse mySchool() {
		LoginInfo info = TokenManager.getNowUser();

		School school = schoolMapper.selectByPrimaryKey(mapper.selectByPrimaryKey(info.getId()).getSchoolId());
		SchoolResponse response = new SchoolResponse();
		try {
			BeanUtils.copyProperties(response, school);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

}

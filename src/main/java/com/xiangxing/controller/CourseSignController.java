package com.xiangxing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.CourseSignMapper;
import com.xiangxing.mapper.SubjectMapper;
import com.xiangxing.model.CourseSign;

@Controller
@RequestMapping("/courseSign")
public class CourseSignController extends BaseController {
	@Autowired
	private SubjectMapper subjectMapper;

	@Autowired
	private CourseSignMapper courseSignMapper;

	@RequestMapping("/courseSign")
	public String courseSign() {
		return "courseSign";
	}


	@RequestMapping("/courseSignList")
	@ResponseBody
	public PageResponse<CourseSign> courseSignList(PageRequest pageRequest,Long schoolId, Long subjectId) {

		return null;

	}

	@RequestMapping("/editCourseSign")
	public void editcourseSign(CourseSign courseSign, Long courseSignId) {
		courseSign.setId(courseSignId);
		courseSignMapper.updateByPrimaryKeySelective(courseSign);
		writeToOkResponse();
	}


}

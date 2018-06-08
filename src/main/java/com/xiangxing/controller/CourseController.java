package com.xiangxing.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.mapper.CourseMapper;
import com.xiangxing.model.Course;
import com.xiangxing.model.CourseExample;

@Controller
@RequestMapping("/course")
public class CourseController extends BaseController {

	@Autowired
	private CourseMapper courseMapper;

	@RequestMapping("/course")
	public String course() {
		return "course";
	}

	@RequestMapping("/saveCourse")
	public void savecourse(Course course) {
		courseMapper.insertSelective(course);
		writeToOkResponse();
	}

	@RequestMapping("/courseList")
	@ResponseBody
	public PageResponse<Course> courseList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		CourseExample courseExample = new CourseExample();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			courseExample.createCriteria().andNameLike(name);

		}

		List<Course> courses = courseMapper.selectByExample(courseExample);
		long total = page.getTotal();
		return new PageResponse<Course>(total, courses);

	}

	@RequestMapping("/editCourse")
	public void editcourse(Course course, String courseId) {
		courseMapper.updateByPrimaryKeySelective(course);
		writeToOkResponse();
	}

	@RequestMapping("/destroyCourse")
	public void destroycourse(int id) {
		courseMapper.deleteByPrimaryKey(id);
		writeToOkResponse();
	}

}

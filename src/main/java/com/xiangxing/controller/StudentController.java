package com.xiangxing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.EntryFormMapper;
import com.xiangxing.mapper.StudentCourseMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.ex.CourseMapperEx;
import com.xiangxing.mapper.ex.StudentPoMapper;
import com.xiangxing.model.EntryForm;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentCourse;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.model.ex.StudentPo;
import com.xiangxing.utils.MD5Util;
import com.xiangxing.utils.StringUtil;
import com.xiangxing.vo.api.ApiResponse;

@Controller
@RequestMapping("/student")
public class StudentController extends BaseController {

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	private StudentPoMapper studentPoMapper;

	@Autowired
	private CourseMapperEx courseMapperEx;

	@Autowired
	StudentCourseMapper studentCourseMapper;
	@Autowired
	EntryFormMapper entryFormMapper;

	@RequestMapping("/student")
	public String student(Model model) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List courses = new ArrayList<>();
		if (me.getType() == 1) {
			courses = courseMapperEx.courseList(null, me.getSchoolId(), null);
		}
		model.addAttribute("courses", courses);
		return "student";
	}

	@RequestMapping("/saveStudent")
	@ResponseBody
	public ApiResponse savestudent(Student student) {
		student.setPassword(MD5Util.MD5Encode(student.getPassword()));
		studentMapper.insertSelective(student);
		return new ApiResponse();
	}

	@RequestMapping("/studentList")
	@ResponseBody
	public PageResponse<StudentPo> studentList(PageRequest pageRequest, String name) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List<StudentPo> students = studentPoMapper.list(name, me.getSchoolId());
		long total = page.getTotal();
		return new PageResponse<StudentPo>(total, students);

	}

	@RequestMapping("/applyList")
	@ResponseBody
	public PageResponse<CourseEx> applyList(PageRequest pageRequest, Long studentId) {
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List<CourseEx> courseList = courseMapperEx.courseListByStudentId(studentId);
		long total = page.getTotal();
		return new PageResponse<CourseEx>(total, courseList);

	}

	@RequestMapping("/editStudent")
	@ResponseBody
	public ApiResponse editstudent(Student student, Long studentId) {
		student.setId(studentId);
		if (StringUtil.isNotEmpty(student.getPassword())) {
			student.setPassword(MD5Util.MD5Encode(student.getPassword()));
		}
		studentMapper.updateByPrimaryKey(student);
		return new ApiResponse();
	}

	@RequestMapping("/destroyStudent")
	@ResponseBody
	public ApiResponse destroystudent(Long studentId) {
		studentMapper.deleteByPrimaryKey(studentId);
		return new ApiResponse();
	}

	@RequestMapping("/saveApply")
	@ResponseBody
	public ApiResponse saveApply(StudentCourse studentCourse) {
		try {
			studentCourseMapper.insert(studentCourse);
			return new ApiResponse();
		} catch (Exception e) {
			return ApiResponse.getErrorResponse("异常");
		}

	}

	@RequestMapping("/saveEntryForm")
	@ResponseBody
	public ApiResponse saveEntryForm(EntryForm entryForm) {
		try {
			entryForm.setStudentId(entryForm.getId());
			entryForm.setId(null);
			entryForm.setCreateTime(new Date());
			entryFormMapper.insertSelective(entryForm);
			return new ApiResponse();
		} catch (Exception e) {
			return ApiResponse.getErrorResponse("异常");
		}

	}

}

package com.xiangxing.controller;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentExample;

@Controller
@RequestMapping("/student")
public class StudentController extends BaseController {

	@Autowired
	private StudentMapper studentMapper;

	@RequestMapping("/student")
	public String student() {
		return "student";
	}

	@RequestMapping("/saveStudent")
	public void savestudent(Student student) {
		studentMapper.insertSelective(student);
		writeToOkResponse();
	}

	@RequestMapping("/studentList")
	@ResponseBody
	public PageResponse<Student> studentList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		StudentExample studentExample = new StudentExample();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			studentExample.createCriteria().andNameLike(name);

		}

		List<Student> students = studentMapper.selectByExample(studentExample);
		long total = page.getTotal();
		return new PageResponse<Student>(total, students);

	}

	@RequestMapping("/editStudent")
	public void editstudent(Student student, String studentId) {
		studentMapper.updateByPrimaryKeySelective(student);
		writeToOkResponse();
	}

	@RequestMapping("/destroyStudent")
	public void destroystudent(Long studentId) {
		studentMapper.deleteByPrimaryKey(studentId);
		writeToOkResponse();
	}

}

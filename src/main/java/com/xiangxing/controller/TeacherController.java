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
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.model.Teacher;
import com.xiangxing.model.TeacherExample;

@Controller
@RequestMapping("/teacher")
public class TeacherController extends BaseController {

	@Autowired
	private TeacherMapper teacherMapper;

	@RequestMapping("/teacher")
	public String teacher() {
		return "teacher";
	}

	@RequestMapping("/saveTeacher")
	public void saveteacher(Teacher teacher) {
		teacherMapper.insertSelective(teacher);
		writeToOkResponse();
	}

	@RequestMapping("/teacherList")
	@ResponseBody
	public PageResponse<Teacher> teacherList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		TeacherExample teacherExample = new TeacherExample();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			teacherExample.createCriteria().andNameLike(name);

		}

		List<Teacher> teachers = teacherMapper.selectByExample(teacherExample);
		long total = page.getTotal();
		return new PageResponse<Teacher>(total, teachers);

	}

	@RequestMapping("/editTeacher")
	public void editteacher(Teacher teacher, String teacherId) {
		teacherMapper.updateByPrimaryKeySelective(teacher);
		writeToOkResponse();
	}

	@RequestMapping("/destroyTeacher")
	public void destroyteacher(Long teacherId) {
		teacherMapper.deleteByPrimaryKey(teacherId);
		writeToOkResponse();
	}

}

package com.xiangxing.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
<<<<<<< .mine
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
=======
import com.xiangxing.mapper.SchoolMapper;


>>>>>>> .theirs
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.Teacher;
import com.xiangxing.model.TeacherExample;

@Controller
@RequestMapping("/teacher")
public class TeacherController extends BaseController {

	@Autowired
	private TeacherMapper teacherMapper;
	@Autowired
	private SchoolMapper schoolMapper;

	@RequestMapping("/comboboxData")
	@ResponseBody
	public String comboboxData(String schoolId) {
		TeacherExample teacherExample = new TeacherExample();
		if (StringUtils.isNotBlank(schoolId)) {
			teacherExample.createCriteria().andSchoolIdEqualTo(Long.valueOf(schoolId));
		}
		List<Teacher> teachers = teacherMapper.selectByExample(teacherExample);
		return JSON.toJSONString(teachers);

	}

	@RequestMapping("/teacher")
	public String teacher(Model model) {
		SchoolExample schoolExample = new SchoolExample();
		List<School> schools = schoolMapper.selectByExample(schoolExample);
		model.addAttribute("schools", schools);
		model.addAttribute("defaultValue", schools.get(0).getId());
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
	public void editteacher(Teacher teacher, Long teacherId) {
		teacher.setId(teacherId);
		teacherMapper.updateByPrimaryKeySelective(teacher);
		writeToOkResponse();
	}

	@RequestMapping("/destroyTeacher")
	public void destroyteacher(Long teacherId) {
		teacherMapper.deleteByPrimaryKey(teacherId);
		writeToOkResponse();
	}

}

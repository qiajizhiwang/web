package com.xiangxing.controller;

import java.util.HashMap;
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
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.Teacher;
import com.xiangxing.model.TeacherExample;
import com.xiangxing.model.TeacherExample.Criteria;
import com.xiangxing.model.ex.TeacherEx;
import com.xiangxing.utils.MD5Util;
import com.xiangxing.utils.StringUtil;

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
		// SchoolExample schoolExample = new SchoolExample();
		// List<School> schools = schoolMapper.selectByExample(schoolExample);
		// model.addAttribute("schools", schools);
		// model.addAttribute("defaultValue", schools.get(0).getId());
		return "teacher";
	}

	@RequestMapping("/saveTeacher")
	public void saveteacher(Teacher teacher) {
		teacher.setPassword(MD5Util.MD5Encode(teacher.getPassword()));
		teacherMapper.insertSelective(teacher);
		writeToOkResponse();
	}

	@RequestMapping("/teacherList")
	@ResponseBody
	public PageResponse<TeacherEx> teacherList(PageRequest pageRequest, String name, String status) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		TeacherExample teacherExample = new TeacherExample();
		Criteria criteria = teacherExample.createCriteria();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			criteria.andNameLike(name);

		}
		if (StringUtils.isNotBlank(status)) {
			criteria.andStatusEqualTo(Integer.valueOf(status));
		}

		List<Teacher> teachers = teacherMapper.selectByExample(teacherExample);

		SchoolExample schoolExample = new SchoolExample();
		List<School> schools = schoolMapper.selectByExample(schoolExample);
		HashMap<Long, School> maps = new HashMap<Long, School>();
		for (School school : schools) {
			maps.put(school.getId(), school);
		}
		List<TeacherEx> teacherExs = JSON.parseArray(JSON.toJSONString(teachers), TeacherEx.class);
		for (TeacherEx teacherEx : teacherExs) {
			School school = maps.get(teacherEx.getSchoolId());
			if (school != null) {
				teacherEx.setSchoolName(school.getName());
				teacherEx.setSchoolCode(school.getCode());
			}
		}

		long total = page.getTotal();
		return new PageResponse<TeacherEx>(total, teacherExs);

	}

	@RequestMapping("/editTeacher")
	public void editteacher(Teacher teacher, Long teacherId) {
		teacher.setId(teacherId);
		if (StringUtil.isNotEmpty(teacher.getPassword())) {
			teacher.setPassword(MD5Util.MD5Encode(teacher.getPassword()));
		}
		teacherMapper.updateByPrimaryKeySelective(teacher);
		writeToOkResponse();
	}

	@RequestMapping("/destroyTeacher")
	public void destroyteacher(Long teacherId) {
		teacherMapper.deleteByPrimaryKey(teacherId);
		writeToOkResponse();
	}

}

package com.xiangxing.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.User;

@Controller
@RequestMapping("/school")
public class SchoolController extends BaseController {

	@Autowired
	private SchoolMapper schoolMapper;

	@RequestMapping("/school")
	public String school() {
		return "school";
	}

	@RequestMapping("/saveSchool")
	public void saveSchool(School school) {
		schoolService.addSchool(school);
		writeToOkResponse();
	}

	@RequestMapping("/schoolList")
	@ResponseBody
	public PageResponse<School> schoolList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		SchoolExample schoolExample = new SchoolExample();

		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<School> schools = null;
		if (me.getType() == 1) {
			School school = schoolMapper.selectByPrimaryKey(me.getSchoolId());
			schools = new ArrayList<>();
			schools.add(school);
		} else {
			if (StringUtils.isNotBlank(name)) {
				name = "%" + name + "%";
				schoolExample.createCriteria().andNameLike(name);

			}
			schools = schoolMapper.selectByExample(schoolExample);
		}
		long total = page.getTotal();
		return new PageResponse<School>(total, schools);

	}

	@RequestMapping("/editSchool")
	public void editSchool(School school, Long id) {
		schoolService.editSchool(school, id);
		writeToOkResponse();
	}

	@RequestMapping("/destroySchool")
	public void destroySchool(Long id) {
		schoolService.destroySchool(id);
		writeToOkResponse();
	}

	@RequestMapping("/comboboxData")
	@ResponseBody
	public String comboboxData(PageRequest pageRequest, String name) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<School> schools = null;
		if (me.getType() == 1) {
			School school = schoolMapper.selectByPrimaryKey(me.getSchoolId());
			schools = new ArrayList<>();
			schools.add(school);
		} else {
			SchoolExample schoolExample = new SchoolExample();
			schools = schoolMapper.selectByExample(schoolExample);
		}
		return JSON.toJSONString(schools);

	}

	@RequestMapping("/validateCode")
	@ResponseBody
	public boolean validateCode(String code) {
		boolean validateFlag = true;
		Object schoolCode = request.getSession().getAttribute("schoolCode");
		if (schoolCode != null && code.equals(String.valueOf(schoolCode))) {
			return validateFlag;
		}
		SchoolExample schoolExample = new SchoolExample();
		schoolExample.createCriteria().andCodeEqualTo(code);
		List<School> schools = schoolMapper.selectByExample(schoolExample);
		if (schools.size() > 0) {
			validateFlag = false;
		}
		return validateFlag;

	}

	@RequestMapping("/editSchoolCode")
	public void editSchoolCode(String code) {
		request.getSession().setAttribute("schoolCode", code);

	}

}

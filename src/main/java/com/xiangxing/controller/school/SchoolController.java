package com.xiangxing.controller.school;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.BaseController;
import com.xiangxing.controller.PageRequest;
import com.xiangxing.controller.PageResponse;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.SysUser;
import com.xiangxing.model.SysUserExample;

@Controller
@RequestMapping("/school")
public class SchoolController extends BaseController {

	@Autowired
	private SchoolMapper schoolMapper;

	@RequestMapping("/school")
	public String user() {
		return "school/school";
	}

	@RequestMapping("/saveSchool")
	public void saveSchool(School school) {
		school.setSchoolId(school.getCode());
		schoolService.addSchool(school);
		writeToOkResponse();
	}

	@RequestMapping("/schoolList")
	@ResponseBody
	public PageResponse<School> schoolList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		SchoolExample schoolExample = new SchoolExample();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			schoolExample.createCriteria().andNameLike(name);

		}

		List<School> schools = schoolMapper.selectByExample(schoolExample);
		long total = page.getTotal();
		return new PageResponse<School>(total, schools);

		// School school = new School();
		// List<School> schools = schoolService.searchSchools(school);
		// writeToResponse(JSON.toJSONString(schools), response);
	}

	@RequestMapping("/editSchool")
	public void editSchool(School school, String schoolId) {
		schoolService.editSchool(school, schoolId);
		writeToOkResponse();
	}

	@RequestMapping("/destroySchool")
	public void destroySchool(String schoolId) {
		schoolService.destroySchool(schoolId);
		writeToOkResponse();
	}

}

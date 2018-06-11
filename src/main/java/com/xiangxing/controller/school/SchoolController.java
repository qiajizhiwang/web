package com.xiangxing.controller.school;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;

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
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			schoolExample.createCriteria().andNameLike(name);

		}

		List<School> schools = schoolMapper.selectByExample(schoolExample);
		long total = page.getTotal();
		return new PageResponse<School>(total, schools);

	}

	@RequestMapping("/editSchool")
	public void editSchool(School school, int id) {
		schoolService.editSchool(school, id);
		writeToOkResponse();
	}

	@RequestMapping("/destroySchool")
	public void destroySchool(int id) {
		schoolService.destroySchool(id);
		writeToOkResponse();
	}

}

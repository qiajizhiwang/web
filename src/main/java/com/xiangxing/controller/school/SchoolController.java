package com.xiangxing.controller.school;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xiangxing.controller.BaseController;
import com.xiangxing.model.School;

@Controller
@RequestMapping("/school")
public class SchoolController extends BaseController {

	@RequestMapping("/addSchool")
	public String addSchool() {
		School school = new School();
		school.setSchoolId("1000");
		school.setAdress("地址");
		school.setCode("0001");
		school.setComment("备注");
		school.setLinkman("联系人");
		school.setName("学校名称");
		schoolService.addSchool(school);
		return "/index";
	}

	@RequestMapping("/searchSchools")
	public void searchSchools() {
		School school = new School();
		school.setSchoolId("1000");
		List<School> schools = schoolService.searchSchools(school);
		writeToResponse(JSON.toJSONString(schools), response);
	}

}

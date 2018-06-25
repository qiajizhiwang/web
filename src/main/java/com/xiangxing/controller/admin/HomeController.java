package com.xiangxing.controller.admin;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.User;

@Controller
@RequestMapping
public class HomeController {
	
	@Autowired
	SchoolMapper schoolMapper;

	@RequestMapping
	public String index(HttpSession httpSession) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		SchoolExample schoolExample = new SchoolExample();
		Long id = 0l;
		com.xiangxing.model.SchoolExample.Criteria criteria = schoolExample.createCriteria();
		if (me.getType() == 1) {

			criteria.andIdEqualTo(me.getSchoolId());
			id = me.getSchoolId();
			

		}
		List schools = schoolMapper.selectByExample(schoolExample);
		httpSession.setAttribute("schools", schools);
		httpSession.setAttribute("mySchoolId", id);
		httpSession.setAttribute("username", me.getName());
		return "index";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login() {
		return "login";
	}

}

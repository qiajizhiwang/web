package com.xiangxing.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiangxing.mapper.HeadquartersMapper;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.model.HeadquartersExample;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.User;

@Controller
@RequestMapping
public class HomeController {

	@Autowired
	SchoolMapper schoolMapper;

	@Autowired
	HeadquartersMapper headquartersMapper;

	@RequestMapping
	public String index(HttpSession httpSession) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();

		if (me.getType() == 1) {
			SchoolExample schoolExample = new SchoolExample();
			com.xiangxing.model.SchoolExample.Criteria criteria = schoolExample.createCriteria();
			criteria.andIdEqualTo(me.getSchoolId());
			List schools = schoolMapper.selectByExample(schoolExample);
			httpSession.setAttribute("schools", schools);
			httpSession.setAttribute("mySchoolId", me.getSchoolId());
			httpSession.setAttribute("headquarterss", null);
			httpSession.setAttribute("myHeadquartersId", null);
		}

		else if (me.getType() == 2) {
			HeadquartersExample headquartersExample = new HeadquartersExample();
			com.xiangxing.model.HeadquartersExample.Criteria criteria1 = headquartersExample.createCriteria();
			criteria1.andIdEqualTo(me.getHeadquartersId());
			List headquarterss = headquartersMapper.selectByExample(headquartersExample);
			SchoolExample schoolExample = new SchoolExample();
			com.xiangxing.model.SchoolExample.Criteria criteria = schoolExample.createCriteria();
			criteria.andHeadquartersIdEqualTo(me.getHeadquartersId());
			List schools = schoolMapper.selectByExample(schoolExample);
			httpSession.setAttribute("headquarterss", headquarterss);
			httpSession.setAttribute("myHeadquartersId", me.getHeadquartersId());
			httpSession.setAttribute("schools", schools);
			httpSession.setAttribute("mySchoolId", null);

		} else if (me.getType() == 0) {
			HeadquartersExample headquartersExample = new HeadquartersExample();
			com.xiangxing.model.HeadquartersExample.Criteria criteria1 = headquartersExample.createCriteria();
			List headquarterss = headquartersMapper.selectByExample(headquartersExample);
			SchoolExample schoolExample = new SchoolExample();
			com.xiangxing.model.SchoolExample.Criteria criteria = schoolExample.createCriteria();
			List schools = schoolMapper.selectByExample(schoolExample);
			httpSession.setAttribute("headquarterss", headquarterss);
			httpSession.setAttribute("schools", schools);
			httpSession.setAttribute("mySchoolId", null);
			httpSession.setAttribute("myHeadquartersId", null);

		}

		httpSession.setAttribute("username", me.getName());
		return "index";
	}

	@RequestMapping(value = "/login")
	public String login(HttpServletRequest httpServletRequest) {

		if (httpServletRequest.getMethod().equals("POST") && SecurityUtils.getSubject().isAuthenticated())
			return "index";
		return "login";
	}

}

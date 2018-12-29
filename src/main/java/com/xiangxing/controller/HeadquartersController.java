package com.xiangxing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.HeadquartersMapper;
import com.xiangxing.model.Headquarters;
import com.xiangxing.model.HeadquartersExample;
import com.xiangxing.model.User;

@Controller
@RequestMapping("/headquarters")
public class HeadquartersController extends BaseController {

	private static final Logger logger = LogManager.getLogger(HeadquartersController.class);
	@Autowired
	private HeadquartersMapper headquartersMapper;



	@RequestMapping("/headquarters")
	public String headquarters() {
		return "headquarters";
	}

	@RequestMapping("/saveHeadquarters")
	public void saveHeadquarters(Headquarters headquarters) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() != 0) {
			logger.info("权限不足！");
			writeToErrorResponse(new JSONObject());
			return;
		}
		if(null==headquarters.getCount()|| headquarters.getCount()<1){
			headquarters.setCount(1);
		}
		headquarters.setCreateTime(new Date());
		headquartersMapper.insert(headquarters);
		writeToOkResponse();
	}

	@RequestMapping("/headquartersList")
	@ResponseBody
	public PageResponse<Headquarters> headquartersList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		HeadquartersExample headquartersExample = new HeadquartersExample();

		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<Headquarters> headquarterss =  new ArrayList<Headquarters>();
		if (me.getType() == 0) {
			if (StringUtils.isNotBlank(name)) {
				name = "%" + name + "%";
				headquartersExample.createCriteria().andNameLike(name);

			}
			headquarterss = headquartersMapper.selectByExample(headquartersExample);

		} 
		long total = page.getTotal();
		return new PageResponse<Headquarters>(total, headquarterss);

	}

	@RequestMapping("/editHeadquarters")
	public void editHeadquarters(Headquarters headquarters, Long id) {
		headquarters.setId(id);
		headquartersMapper.updateByPrimaryKeySelective(headquarters);
		writeToOkResponse();
	}

	@RequestMapping("/destroyHeadquarters")
	public void destroyHeadquarters(Long id) {
		headquartersMapper.deleteByPrimaryKey(id);
		writeToOkResponse();
	}
/*
	@RequestMapping("/comboboxData")
	@ResponseBody
	public String comboboxData(PageRequest pageRequest, String name) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<Headquarters> headquarterss = null;
		if (me.getType() == 1) {
			Headquarters headquarters = headquartersMapper.selectByPrimaryKey(me.getHeadquartersId());
			headquarterss = new ArrayList<>();
			headquarterss.add(headquarters);
		} else {
			HeadquartersExample headquartersExample = new HeadquartersExample();
			headquarterss = headquartersMapper.selectByExample(headquartersExample);
		}
		return JSON.toJSONString(headquarterss);

	}*/

	

}

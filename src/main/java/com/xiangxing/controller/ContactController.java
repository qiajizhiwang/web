package com.xiangxing.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.ContactMapper;
import com.xiangxing.model.Contact;
import com.xiangxing.model.ContactExample;

@Controller
@RequestMapping("/contact")
public class ContactController {

	private static final Logger logger = LogManager.getLogger(ContactController.class);

	@Autowired
	ContactMapper contactMapper;

	@RequestMapping
	public String contact() {
		return "contact";
	}

	@RequestMapping("/contactList")
	@ResponseBody
	public PageResponse<Contact> contactList(PageRequest pageRequest, HttpServletRequest httpServletRequest) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		ContactExample contactExample = new ContactExample();
		contactExample.createCriteria().andIdIsNotNull();
		contactExample.setOrderByClause("id desc");
		List<Contact> contacts = contactMapper.selectByExample(contactExample);
		long total = page.getTotal();
		return new PageResponse<Contact>(total, contacts);

	}
}

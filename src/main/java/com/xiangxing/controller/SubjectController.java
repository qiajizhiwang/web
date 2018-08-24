package com.xiangxing.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.SubjectMapper;
import com.xiangxing.model.Subject;
import com.xiangxing.model.SubjectExample;
import com.xiangxing.model.User;
import com.xiangxing.model.SubjectExample.Criteria;

@Controller
@RequestMapping("/subject")
public class SubjectController extends BaseController {

	@Autowired
	private SubjectMapper subjectMapper;

	@RequestMapping("/subject")
	public String subject() {
		return "subject";
	}

	@RequestMapping("/saveSubject")
	public void savesubject(Subject subject) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		JSONObject jsonObject = new JSONObject();
		if (me.getType() == 1) {
			jsonObject.put("msg", "无权限！");
			writeToErrorResponse(jsonObject);
			return;
		}
		subjectMapper.insertSelective(subject);
		writeToOkResponse();
	}

	@RequestMapping("/subjectList")
	@ResponseBody
	public PageResponse<Subject> subjectList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		SubjectExample subjectExample = new SubjectExample();
		Criteria criteria = subjectExample.createCriteria();
		criteria.andDelFlagEqualTo(0l);
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			criteria.andNameLike(name);

		}

		List<Subject> subjects = subjectMapper.selectByExample(subjectExample);
		long total = page.getTotal();
		return new PageResponse<Subject>(total, subjects);

	}

	@RequestMapping("/editSubject")
	public void editsubject(Subject subject, Long subjectId) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		JSONObject jsonObject = new JSONObject();
		if (me.getType() == 1) {
			jsonObject.put("msg", "无权限！");
			writeToErrorResponse(jsonObject);
			return;
		}
		subject.setId(subjectId);
		subjectMapper.updateByPrimaryKeySelective(subject);
		writeToOkResponse();
	}

	@RequestMapping("/destroySubject")
	public void destroysubject(Long subjectId) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		JSONObject jsonObject = new JSONObject();
		if (me.getType() == 1) {
			jsonObject.put("msg", "无权限！");
			writeToErrorResponse(jsonObject);
			return;
		}
		Subject record = new Subject();
		record.setId(subjectId);
		record.setDelFlag(1l);
		subjectMapper.updateByPrimaryKeySelective(record);
		writeToOkResponse();
	}

	@RequestMapping("/comboboxData")
	@ResponseBody
	public String comboboxData(PageRequest pageRequest, String name) {
		SubjectExample subjectExample = new SubjectExample();
		subjectExample.createCriteria().andDelFlagEqualTo(0l);
		List<Subject> subjects = subjectMapper.selectByExample(subjectExample);
		return JSON.toJSONString(subjects);

	}
}

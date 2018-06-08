package com.xiangxing.controller;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.mapper.SubjectMapper;
import com.xiangxing.model.Subject;
import com.xiangxing.model.SubjectExample;

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
		subjectMapper.insertSelective(subject);
		writeToOkResponse();
	}

	@RequestMapping("/subjectList")
	@ResponseBody
	public PageResponse<Subject> subjectList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		SubjectExample subjectExample = new SubjectExample();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			subjectExample.createCriteria().andNameLike(name);

		}

		List<Subject> subjects = subjectMapper.selectByExample(subjectExample);
		long total = page.getTotal();
		return new PageResponse<Subject>(total, subjects);

	}

	@RequestMapping("/editSubject")
	public void editsubject(Subject subject, String subjectId) {
		subjectMapper.updateByPrimaryKeySelective(subject);
		writeToOkResponse();
	}

	@RequestMapping("/destroySubject")
	public void destroysubject(Integer subjectId) {
		subjectMapper.deleteByPrimaryKey(subjectId);
		writeToOkResponse();
	}

}

package com.xiangxing.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.EntryFormMapper;
import com.xiangxing.mapper.ex.EntryFormPoMapper;
import com.xiangxing.model.EntryForm;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.EntryFormPo;

@Controller
@RequestMapping("/entryForm")
public class EntryFormController extends BaseController {

	@Autowired
	private EntryFormMapper entryFormMapper;
	@Autowired
	private EntryFormPoMapper entryFormPoMapper;

	@RequestMapping("/entryForm")
	public String entryForm() {
		return "entryForm";
	}

	@RequestMapping("/saveEntryForm")
	public void saveentryForm(EntryForm entryForm) {
		entryFormMapper.insertSelective(entryForm);
		writeToOkResponse();
	}

	@RequestMapping("/entryFormList")
	@ResponseBody
	public PageResponse<EntryFormPo> entryFormList(PageRequest pageRequest, Long schoolId, String studentName, Long examId) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			schoolId = me.getSchoolId();
		}
		List<EntryFormPo> entryFormPos = entryFormPoMapper.list(schoolId, studentName, examId,null);
		long total = page.getTotal();
		return new PageResponse<EntryFormPo>(total, entryFormPos);

	}
}

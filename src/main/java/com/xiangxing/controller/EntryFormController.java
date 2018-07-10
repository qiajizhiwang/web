package com.xiangxing.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
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
import com.xiangxing.utils.DateUtil;
import com.xiangxing.utils.ExcleUtil;
import com.xiangxing.utils.HanyuPinyinHelper;
import com.xiangxing.utils.StringUtil;

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
		List<EntryFormPo> entryFormPos = entryFormPoMapper.list(schoolId, studentName, examId, null);
		for (EntryFormPo entryFormPo : entryFormPos) {
			if (StringUtil.isNotEmpty(entryFormPo.getBirthday())) {
				entryFormPo.setBirthday(DateUtil.dateToString(DateUtil.stringToDate(entryFormPo.getBirthday()), DateUtil.patternG));
			}
		}
		long total = page.getTotal();
		return new PageResponse<EntryFormPo>(total, entryFormPos);

	}

	/***
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "doExcle")
	public void doExcle(Long schoolId, String studentName, Long examId) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			schoolId = me.getSchoolId();
		}
		List<EntryFormPo> entryFormPos = entryFormPoMapper.list(schoolId, studentName, examId, null);
		for (EntryFormPo entryFormPo : entryFormPos) {
			if (StringUtil.isNotEmpty(entryFormPo.getBirthday())) {
				entryFormPo.setBirthday(DateUtil.dateToString(DateUtil.stringToDate(entryFormPo.getBirthday()), DateUtil.patternG));
			}
			if (StringUtil.isNotEmpty(entryFormPo.getStudentName())) {
				entryFormPo.setPinyin(HanyuPinyinHelper.toHanyuPinyin(entryFormPo.getStudentName()));
			}
		}

		List<Map> dataMaps = JSON.parseArray(JSON.toJSONString(entryFormPos), Map.class);

		String[] valueKeys = { "studentName", "pinyin", "gender", "birthday", "nation", "state", "major", "rank" };
		String[] columnNames = { "考生姓名", "拼音", "性别", "出生日期", "民族", "国家", "专业", "级别" };
		try {
			ExcleUtil.doExcle(response, dataMaps, "学生检测报表", columnNames, valueKeys);
			// 导出后清空session
			request.getSession().removeAttribute("StudentDataExport");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

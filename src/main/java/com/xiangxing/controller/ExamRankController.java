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
import com.xiangxing.mapper.ExamRankMapper;
import com.xiangxing.model.ExamRank;
import com.xiangxing.model.ExamRankExample;
import com.xiangxing.model.User;
import com.xiangxing.model.ExamRankExample.Criteria;

@Controller
@RequestMapping("/examRank")
public class ExamRankController extends BaseController {

	@Autowired
	private ExamRankMapper examRankMapper;

	@RequestMapping("/examRank")
	public String examRank() {
		return "examRank";
	}

	@RequestMapping("/saveExamRank")
	public void saveexamRank(ExamRank examRank) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		JSONObject jsonObject = new JSONObject();
		if (me.getType() == 1) {
			jsonObject.put("msg", "无权限！");
			writeToErrorResponse(jsonObject);
			return;
		}
		examRankMapper.insertSelective(examRank);
		writeToOkResponse();
	}

	@RequestMapping("/examRankList")
	@ResponseBody
	public PageResponse<ExamRank> examRankList(PageRequest pageRequest, String rank) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		ExamRankExample examRankExample = new ExamRankExample();
		Criteria criteria = examRankExample.createCriteria();
		if (StringUtils.isNotBlank(rank)) {
			rank = "%" + rank + "%";
			criteria.andRankLike(rank);

		}

		List<ExamRank> examRanks = examRankMapper.selectByExample(examRankExample);
		long total = page.getTotal();
		return new PageResponse<ExamRank>(total, examRanks);

	}

	@RequestMapping("/editExamRank")
	public void editexamRank(ExamRank examRank, Long examRankId) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		JSONObject jsonObject = new JSONObject();
		if (me.getType() == 1) {
			jsonObject.put("msg", "无权限！");
			writeToErrorResponse(jsonObject);
			return;
		}
		examRank.setId(examRankId);
		examRankMapper.updateByPrimaryKeySelective(examRank);
		writeToOkResponse();
	}

	@RequestMapping("/destroyExamRank")
	public void destroyexamRank(Long examRankId) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		JSONObject jsonObject = new JSONObject();
		if (me.getType() == 1) {
			jsonObject.put("msg", "无权限！");
			writeToErrorResponse(jsonObject);
			return;
		}
		examRankMapper.deleteByPrimaryKey(examRankId);
		writeToOkResponse();
	}

	@RequestMapping("/comboboxData")
	@ResponseBody
	public String comboboxData(PageRequest pageRequest, String rank) {
		ExamRankExample examRankExample = new ExamRankExample();
		Criteria criteria = examRankExample.createCriteria();
		if (StringUtils.isNotBlank(rank)) {
			rank = "%" + rank + "%";
			criteria.andRankLike(rank);

		}
		List<ExamRank> examRanks = examRankMapper.selectByExample(examRankExample);
		return JSON.toJSONString(examRanks);

	}
}

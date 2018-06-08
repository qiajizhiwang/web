package com.xiangxing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.mapper.ExamMapper;
import com.xiangxing.model.Exam;
import com.xiangxing.model.ExamExample;

@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController {

	@Autowired
	private ExamMapper examMapper;

	@RequestMapping("/exam")
	public String exam() {
		return "exam";
	}

	@RequestMapping("/saveExam")
	public void saveexam(Exam exam) {
		examMapper.insertSelective(exam);
		writeToOkResponse();
	}

	@RequestMapping("/examList")
	@ResponseBody
	public PageResponse<Exam> examList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		ExamExample examExample = new ExamExample();
		List<Exam> exams = examMapper.selectByExample(examExample);
		long total = page.getTotal();
		return new PageResponse<Exam>(total, exams);

	}

	@RequestMapping("/editExam")
	public void editexam(Exam exam, String examId) {
		examMapper.updateByPrimaryKeySelective(exam);
		writeToOkResponse();
	}

	@RequestMapping("/destroyExam")
	public void destroyexam(Integer examId) {
		examMapper.deleteByPrimaryKey(examId);
		writeToOkResponse();
	}

}

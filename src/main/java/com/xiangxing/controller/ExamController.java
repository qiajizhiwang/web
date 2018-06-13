package com.xiangxing.controller;

import java.util.HashMap;
import java.util.List;

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
import com.xiangxing.mapper.ExamMapper;
import com.xiangxing.mapper.SubjectMapper;
import com.xiangxing.model.Exam;
import com.xiangxing.model.ExamExample;
import com.xiangxing.model.Subject;
import com.xiangxing.model.SubjectExample;
import com.xiangxing.model.ex.ExamEx;

@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController {
	@Autowired
	private SubjectMapper subjectMapper;

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
	public PageResponse<ExamEx> examList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		ExamExample examExample = new ExamExample();
		List<Exam> exams = examMapper.selectByExample(examExample);

		SubjectExample subjectExample = new SubjectExample();
		List<Subject> subjects = subjectMapper.selectByExample(subjectExample);
		HashMap<Long, String> maps = new HashMap<Long, String>();
		for (Subject subject : subjects) {
			maps.put(subject.getId(), subject.getName());
		}
		List<ExamEx> examExs = JSON.parseArray(JSON.toJSONString(exams), ExamEx.class);
		for (ExamEx examEx : examExs) {
			examEx.setSubjectName(maps.get(examEx.getSubjectId()));
		}
		long total = page.getTotal();
		return new PageResponse<ExamEx>(total, examExs);

	}

	@RequestMapping("/editExam")
	public void editexam(Exam exam, Long examId) {
		exam.setId(examId);
		examMapper.updateByPrimaryKeySelective(exam);
		writeToOkResponse();
	}

	@RequestMapping("/destroyExam")
	public void destroyexam(Long examId) {
		examMapper.deleteByPrimaryKey(examId);
		writeToOkResponse();
	}

}

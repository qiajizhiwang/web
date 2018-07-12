package com.xiangxing.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.xiangxing.mapper.ExamMapper;
import com.xiangxing.mapper.SubjectMapper;
import com.xiangxing.mapper.ex.ExamPoMapper;
import com.xiangxing.model.Exam;
import com.xiangxing.model.ExamExample;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.ExamExample.Criteria;
import com.xiangxing.model.Subject;
import com.xiangxing.model.SubjectExample;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.ExamEx;
import com.xiangxing.model.ex.ExamPo;
import com.xiangxing.utils.HanyuPinyinHelper;
import com.xiangxing.utils.PdfUtils;

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
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			exam.setSchoolId(me.getSchoolId());
		} else {
			JSONObject jsonObject = new JSONObject();
			writeToErrorResponse(jsonObject);
		}
		examMapper.insertSelective(exam);
		writeToOkResponse();
	}

	@RequestMapping("/examList")
	@ResponseBody
	public PageResponse<ExamEx> examList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		ExamExample examExample = new ExamExample();
		Criteria criteria = examExample.createCriteria();
		criteria.andStatusNotEqualTo(2l);
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			criteria.andSchoolIdEqualTo(me.getSchoolId());
		}
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
	public void destroyExam(Long examId) {
		Exam record = new Exam();
		record.setId(examId);
		record.setStatus(2l);
		examMapper.updateByPrimaryKeySelective(record);
		writeToOkResponse();
	}

	@Autowired
	ExamPoMapper poMapper;

	@Value(value = "${pdf_path}")
	private String pdfPath;

	@RequestMapping("/exportApplyTable")
	public void exportApplyTable(Long examId, HttpServletResponse response) throws IOException {
		ExamPo examPo = poMapper.get(examId);
		examPo.setPinyin(HanyuPinyinHelper.toHanyuPinyin(examPo.getStudentName()));
		String path = pdfPath + File.separator +"考生报名表"+ examId + ".pdf";
		File file = new File(path);
		if (!file.exists()) {
			PdfUtils.createPdf(path, PdfUtils.class.getClassLoader().getResource("templates/pdf").getPath(),
					"apply.ftl", new org.apache.commons.beanutils.BeanMap(examPo));
			file = new File(path);
		}
		response.addHeader("pragma", "NO-cache");
		response.addHeader("Cache-Control", "no-cache");
		response.addDateHeader("Expries", 0);
		response.setContentType("application/pdf;charset=utf-8");
		response.addHeader("Content-Disposition", "attachment;filename=table" + examId + ".pdf");
		FileInputStream fileInputStream = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		int length = 0;
		byte buffer[] = new byte[1024];
		while ((length = fileInputStream.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		fileInputStream.close();
		out.flush();
		out.close();
	}
	
	@RequestMapping("/exportExamTicket")
	public void exportExamTicket(Long examId, HttpServletResponse response) throws IOException {
		ExamPo examPo = poMapper.get(examId);
		String path = pdfPath + File.separator +"准考证"+ examId + ".pdf";
		File file = new File(path);
		if (!file.exists()) {
			PdfUtils.createMiniPdf(path, PdfUtils.class.getClassLoader().getResource("templates/pdf").getPath(),
					"exam.ftl", new org.apache.commons.beanutils.BeanMap(examPo));
			file = new File(path);
		}
		response.addHeader("pragma", "NO-cache");
		response.addHeader("Cache-Control", "no-cache");
		response.addDateHeader("Expries", 0);
		response.setContentType("application/pdf;charset=utf-8");
		response.addHeader("Content-Disposition", "attachment;filename=ticket" + examId + ".pdf");
		FileInputStream fileInputStream = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		int length = 0;
		byte buffer[] = new byte[1024];
		while ((length = fileInputStream.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		fileInputStream.close();
		out.flush();
		out.close();
	}

	@RequestMapping("/auditExam")
	public void auditExam(Long examId) {
		Exam record = new Exam();
		record.setId(examId);
		record.setStatus(1l);
		examMapper.updateByPrimaryKeySelective(record);
		writeToOkResponse();
	}

	@RequestMapping("/comboboxData")
	@ResponseBody
	public String comboboxData(PageRequest pageRequest, String name) {

		ExamExample examExample = new ExamExample();
		Criteria criteria = examExample.createCriteria();
		criteria.andStatusNotEqualTo(2l);
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			criteria.andSchoolIdEqualTo(me.getSchoolId());
		}
		List<Exam> exams = examMapper.selectByExample(examExample);

		SubjectExample subjectExample = new SubjectExample();
		List<Subject> subjects = subjectMapper.selectByExample(subjectExample);
		HashMap<Long, String> maps = new HashMap<Long, String>();
		for (Subject subject : subjects) {
			maps.put(subject.getId(), subject.getName());
		}
		List<ExamEx> examExs = JSON.parseArray(JSON.toJSONString(exams), ExamEx.class);
		for (ExamEx examEx : examExs) {
			examEx.setSubjectName(maps.get(examEx.getSubjectId()) + "-" + examEx.getRank());
		}
		return JSON.toJSONString(examExs);

	}
}

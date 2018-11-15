package com.xiangxing.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.xiangxing.mapper.EntryFormMapper;
import com.xiangxing.mapper.ex.EntryFormPoMapper;
import com.xiangxing.mapper.ex.ExamPoMapper;
import com.xiangxing.model.Course;
import com.xiangxing.model.EntryForm;
import com.xiangxing.model.Exam;
import com.xiangxing.model.StudentCourse;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.EntryFormPo;
import com.xiangxing.model.ex.ExamPo;
import com.xiangxing.utils.DateUtil;
import com.xiangxing.utils.ExcleUtil;
import com.xiangxing.utils.HanyuPinyinHelper;
import com.xiangxing.utils.PdfUtils;
import com.xiangxing.utils.StringUtil;
import com.xiangxing.vo.api.ApiResponse;

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
	
	@RequestMapping("/deleteExam")
	@ResponseBody
	public ApiResponse deleteExam(Long examId) {
		EntryForm entryForm= entryFormMapper.selectByPrimaryKey(examId);
		
		if(entryForm.getPayStatus()==1)
			entryFormMapper.deleteByPrimaryKey(examId);
		else{
			return ApiResponse.getErrorResponse("状态异常");
		}
		return new ApiResponse();
	}

	@RequestMapping("/entryFormList")
	@ResponseBody
	public PageResponse<EntryFormPo> entryFormList(PageRequest pageRequest, Long schoolId, String studentName, Long examId) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			schoolId = me.getSchoolId();
		}
		List<EntryFormPo> entryFormPos = entryFormPoMapper.list(schoolId, studentName, examId, null, null, null);
		for (EntryFormPo entryFormPo : entryFormPos) {
			if (StringUtil.isNotEmpty(entryFormPo.getBirthday())) {
				entryFormPo.setBirthday(DateUtil.dateToString(DateUtil.stringToDate(entryFormPo.getBirthday()), DateUtil.patternG));
			}
		}
		long total = page.getTotal();
		return new PageResponse<EntryFormPo>(total, entryFormPos);

	}
	
	@RequestMapping("/entryFormListByStudentId")
	@ResponseBody
	public PageResponse<EntryFormPo> entryFormListByStudentId(PageRequest pageRequest, Long studentId) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List<EntryFormPo> entryFormPos = entryFormPoMapper.listByStudentId(studentId,1);
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
		List<EntryFormPo> entryFormPos = entryFormPoMapper.list(schoolId, studentName, examId, null, null, null);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	ExamPoMapper poMapper;

	@Value(value = "${pdf_path}")
	private String pdfPath;

	@RequestMapping("/exportApplyTable")
	public void exportApplyTable(Long entryFormId, HttpServletResponse response) throws IOException {
		EntryForm entryForm = entryFormMapper.selectByPrimaryKey(entryFormId);
		if (entryForm.getPayStatus()==1) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("info", "未缴费！");
			writeToErrorResponse(jsonObject);
			return;
		}
		ExamPo examPo = poMapper.get(entryFormId);
		examPo.setPinyin(HanyuPinyinHelper.toHanyuPinyin(examPo.getStudentName()));
		String path = pdfPath + File.separator + "考生报名表" + entryFormId + ".pdf";
		File file = new File(path);
		if (!file.exists()) {
			PdfUtils.createPdf(path, PdfUtils.class.getClassLoader().getResource("templates/pdf").getPath(), "apply.ftl",
					new org.apache.commons.beanutils.BeanMap(examPo));
			// PdfUtils.createPdf(path, pdfPath+"templates/",
			// "apply.ftl", new org.apache.commons.beanutils.BeanMap(examPo));
			file = new File(path);
		}
		response.addHeader("pragma", "NO-cache");
		response.addHeader("Cache-Control", "no-cache");
		response.addDateHeader("Expries", 0);
		response.setContentType("application/pdf;charset=utf-8");
		String fileName = examPo.getStudentName() + "考生报名表.pdf";
		response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO_8859_1"));
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
	public void exportExamTicket(Long entryFormId, HttpServletResponse response) throws IOException {
		EntryForm entryForm = entryFormMapper.selectByPrimaryKey(entryFormId);
		if (entryForm.getPayStatus()==1) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("info", "未缴费！");
			writeToErrorResponse(jsonObject);
			return;
		}
		ExamPo examPo = poMapper.get(entryFormId);
		String path = pdfPath + File.separator + "准考证" + entryFormId + ".pdf";
		File file = new File(path);
		if (!file.exists()) {
			PdfUtils.createMiniPdf(path, PdfUtils.class.getClassLoader().getResource("templates/pdf").getPath(), "exam.ftl",
					new org.apache.commons.beanutils.BeanMap(examPo));
			// PdfUtils.createMiniPdf(path, pdfPath+"templates/",
			// "exam.ftl", new org.apache.commons.beanutils.BeanMap(examPo));
			file = new File(path);
		}
		response.addHeader("pragma", "NO-cache");
		response.addHeader("Cache-Control", "no-cache");
		response.addDateHeader("Expries", 0);
		response.setContentType("application/pdf;charset=utf-8");
		String fileName = examPo.getStudentName() + "准考证.pdf";
		response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO_8859_1"));
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

}

package com.xiangxing.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.CourseMapper;
import com.xiangxing.mapper.ex.CourseMapperEx;
import com.xiangxing.model.Course;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.utils.DateUtil;
import com.xiangxing.utils.FileUtil;
import com.xiangxing.vo.OptionVo;

/**
 * 课程管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/course")
public class CourseController extends BaseController {

	private static final Logger logger = LogManager.getLogger(CourseController.class);
	// 文件保存目录URL
	@Value("${upload_file_path}")
	private String upload_file_path;

	@Value("${sys_url}")
	private String sys_url;

	@Autowired
	private CourseMapper courseMapper;
	@Autowired
	private CourseMapperEx courseMapperEx;

	// 最大文件大小 30MB
	long maxSize = 31457280;

	@RequestMapping("/course")
	public String course() {
		return "course";
	}

	@RequestMapping("/saveCourse")
	public void savecourse(CourseEx course, MultipartFile file) {

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");

		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("请选择文件。");
			return;
		}

		// 创建文件夹
		String saveUrl = upload_file_path + "image/";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		saveUrl += ymd + "/";

		File dirFile = new File(saveUrl);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		if (file.getSize() > maxSize) {
			logger.error("上传文件大小超过限制。");
			return;
		}
		// 检查扩展名
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		try {
			FileUtil.uploadFile(file.getBytes(), saveUrl, newFileName);
		} catch (Exception e) {
			System.out.println("上传文件失败。");
			return;
		}
		course.setImageUrl(saveUrl + newFileName);
		course.setCurriculumTime(DateUtil.stringToDate(course.getShowCurriculumTime()));
		course.setFinishTime(DateUtil.stringToDate(course.getShowFinishTime()));
		course.setCreateTime(new Date());
		courseMapper.insertSelective(course);
		writeToOkResponse();
	}

	@RequestMapping("/courseList")
	@ResponseBody
	public PageResponse<CourseEx> courseList(PageRequest pageRequest, String name, Long searchrSchoolId, String status) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		// 根据用户过滤课程
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			searchrSchoolId = me.getSchoolId();
		}
		List<CourseEx> courseExs = courseMapperEx.courseList(name, searchrSchoolId, status);
		for (CourseEx courseEx : courseExs) {
			courseEx.setShowCurriculumTime(DateUtil.dateToString(courseEx.getCurriculumTime(), DateUtil.patternA));
			courseEx.setShowFinishTime(DateUtil.dateToString(courseEx.getFinishTime(), DateUtil.patternA));
			courseEx.setImageUrl(sys_url + "initImage?imageUrl=" + courseEx.getImageUrl());
		}
		long total = page.getTotal();
		return new PageResponse<CourseEx>(total, courseExs);

	}

	@RequestMapping("/validCourses")
	@ResponseBody
	public List<OptionVo> courseList(Long schoolId) {

		List<CourseEx> courseExs = courseMapperEx.courseList(null, schoolId, null);
		List options = new ArrayList<>();
		for (CourseEx courseEx : courseExs) {
			OptionVo optionVo = new OptionVo();
			optionVo.setValue(Long.valueOf(courseEx.getId()));
			optionVo.setText(courseEx.getName() + ":" + courseEx.getSchoolTime());
			options.add(optionVo);
		}
		return options;

	}

	@RequestMapping("/editCourse")
	public void editcourse(CourseEx course, Long id, MultipartFile file) {
		if (!file.isEmpty()) {
			// 定义允许上传的文件扩展名
			HashMap<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");

			if (!ServletFileUpload.isMultipartContent(request)) {
				System.out.println("请选择文件。");
				return;
			}

			// 创建文件夹
			String saveUrl = upload_file_path + "image/";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			saveUrl += ymd + "/";

			File dirFile = new File(saveUrl);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			if (file.getSize() > maxSize) {
				logger.error("上传文件大小超过限制。");
				return;
			}
			// 检查扩展名
			String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			try {
				FileUtil.uploadFile(file.getBytes(), saveUrl, newFileName);
			} catch (Exception e) {
				System.out.println("上传文件失败。");
				return;
			}
			course.setImageUrl(saveUrl + newFileName);
		}
		course.setId(id);
		course.setCurriculumTime(DateUtil.stringToDate(course.getShowCurriculumTime()));
		course.setFinishTime(DateUtil.stringToDate(course.getShowFinishTime()));
		courseMapper.updateByPrimaryKeySelective(course);
		writeToOkResponse();
	}

	@RequestMapping("/destroyCourse")
	public void destroycourse(Long id) {
		Course course = courseMapper.selectByPrimaryKey(id);
		if(course.getStatus()==1) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg", "已开课，无法删除！");
			writeToErrorResponse(jsonObject);
			return;
		}
		course.setStatus(2);
		courseMapper.updateByPrimaryKeySelective(course);
		writeToOkResponse();
	}

}

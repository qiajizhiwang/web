package com.xiangxing.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.CourseMapper;
import com.xiangxing.model.Course;
import com.xiangxing.model.CourseExample;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.utils.DateUtil;
import com.xiangxing.utils.FileUtil;

/**
 * 课程管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/course")
public class CourseController extends BaseController {

	@Autowired
	private CourseMapper courseMapper;

	// 最大文件大小
	long maxSize = 1000000;

	@RequestMapping("/course")
	public String course() {
		return "course";
	}

	@RequestMapping("/saveCourse")
	public void savecourse(CourseEx course, MultipartFile file) {

		String savePath = request.getServletContext().getRealPath("/") + "attached/";

		// 文件保存目录URL
		String saveUrl = request.getContextPath() + "/attached/";

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");

		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("请选择文件。");
			return;
		}
		// 检查目录
		File uploadDir = new File(savePath);
		if (!uploadDir.isDirectory()) {
			uploadDir.mkdir();
		}
		if (!uploadDir.isDirectory()) {
			uploadDir.mkdir();
			System.out.println("上传目录不存在。");
			return;
		}
		// 检查目录写权限
		if (!uploadDir.canWrite()) {
			System.out.println("上传目录没有写权限。");
			return;
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!extMap.containsKey(dirName)) {
			System.out.println("目录名不正确。");
			return;
		}
		// 创建文件夹
		savePath += dirName + "/";
		saveUrl += dirName + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		if (file.getSize() > maxSize) {
			System.out.println("上传文件大小超过限制。");
			return;
		}
		// 检查扩展名
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();

		if (!Arrays.asList(extMap.get(dirName).split(",")).contains(fileExt)) {
			System.out.println("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
			return;

		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		try {
			FileUtil.uploadFile(file.getBytes(), savePath, newFileName);
		} catch (Exception e) {
			System.out.println("上传文件失败。");
			return;
		}
		course.setImageUrl(saveUrl + newFileName);
		course.setCurriculumTime(DateUtil.stringToDate(course.getShowCurriculumTime()));
		courseMapper.insertSelective(course);
		writeToOkResponse();
	}

	@RequestMapping("/courseList")
	@ResponseBody
	public PageResponse<CourseEx> courseList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		CourseExample courseExample = new CourseExample();
		if (StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
			courseExample.createCriteria().andNameLike(name);

		}

		List<Course> courses = courseMapper.selectByExample(courseExample);
		List<CourseEx> courseExs = JSON.parseArray(JSON.toJSONString(courses), CourseEx.class);
		for (CourseEx courseEx : courseExs) {
			courseEx.setShowCurriculumTime(DateUtil.dateToString(courseEx.getCurriculumTime(), DateUtil.patternG));
		}
		long total = page.getTotal();
		return new PageResponse<CourseEx>(total, courseExs);

	}

	@RequestMapping("/editCourse")
	public void editcourse(CourseEx course, Integer id) {
		course.setId(id);
		course.setCurriculumTime(DateUtil.stringToDate(course.getShowCurriculumTime()));
		courseMapper.updateByPrimaryKeySelective(course);
		writeToOkResponse();
	}

	@RequestMapping("/destroyCourse")
	public void destroycourse(int id) {
		courseMapper.deleteByPrimaryKey(id);
		writeToOkResponse();
	}

}

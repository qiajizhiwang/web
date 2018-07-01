package com.xiangxing.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.NoticeDetailMapper;
import com.xiangxing.mapper.NoticeMapper;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.model.Notice;
import com.xiangxing.model.NoticeExample;
import com.xiangxing.model.Notice;
import com.xiangxing.model.NoticeDetail;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentExample;
import com.xiangxing.model.User;
import com.xiangxing.utils.FileUtil;
import com.xiangxing.vo.api.ApiResponse;

/**
 * 通知管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

	private static final Logger logger = LogManager.getLogger(NoticeController.class);

	@Autowired
	private NoticeMapper noticeMapper;

	@Autowired
	private SchoolMapper schoolMapper;

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	NoticeDetailMapper noticeDetailMapper;

	@RequestMapping
	public String notice() {
		return "notice";
	}

	@RequestMapping("/saveNotice")
	@ResponseBody
	public ApiResponse saveNotice(Notice notice) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		notice.setType(1);
		notice.setSender(me.getSchoolId());
		notice.setCreateTime(new Date());
		notice.setSenderName(schoolMapper.selectByPrimaryKey(me.getSchoolId()).getName());
		noticeMapper.insertSelective(notice);
		StudentExample studentExample = new StudentExample();
		studentExample.createCriteria().andSchoolIdEqualTo(me.getSchoolId());
		List<Student> students = studentMapper.selectByExample(studentExample);
		for (Student student : students) {
			NoticeDetail noticeDetail = new NoticeDetail();
			noticeDetail.setNoticeId(notice.getId());
			noticeDetail.setStatus(1);
			noticeDetail.setReceiver(student.getId());
			noticeDetailMapper.insert(noticeDetail);
		}
		return new ApiResponse();
	}

	@RequestMapping("/noticeList")
	@ResponseBody
	public PageResponse<Notice> NoticeList(PageRequest pageRequest, HttpServletRequest httpServletRequest) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		NoticeExample NoticeExample = new NoticeExample();
		NoticeExample.createCriteria().andIdIsNotNull();
		List<Notice> notices = noticeMapper.selectByExample(NoticeExample);

		long total = page.getTotal();
		return new PageResponse<Notice>(total, notices);

	}

	@RequestMapping("/destroyNotice")
	@ResponseBody
	public ApiResponse destroyNotice(Long id) {
		noticeMapper.deleteByPrimaryKey(id);
		return new ApiResponse();
	}

}

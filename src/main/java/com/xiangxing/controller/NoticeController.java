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
import org.springframework.beans.factory.annotation.Value;
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
import com.xiangxing.model.NoticeDetailExample;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentExample;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.utils.DateUtil;
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
	
	@Value("${upload_file_path}")
	private String upload_file_path;

	@RequestMapping
	public String notice() {
		return "notice";
		
		
	}

	// 最大文件大小 30MB
		long maxSize = 31457280;
	@RequestMapping("/saveNotice")
	@ResponseBody
	public ApiResponse saveNotice(String text, MultipartFile file,HttpServletRequest request) {
		// 定义允许上传的文件扩展名
				HashMap<String, String> extMap = new HashMap<String, String>();
				extMap.put("image", "gif,jpg,jpeg,png,bmp");

				if (!ServletFileUpload.isMultipartContent(request)) {
					System.out.println("请选择文件。");
					return ApiResponse.getErrorResponse("文件类型不支持");
				}
		
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		String saveUrl = upload_file_path + "/notice/";
		
		File dirFile = new File(saveUrl);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		if (file.getSize() > maxSize) {
			logger.error("上传文件大小超过限制。");
			return ApiResponse.getErrorResponse("上传文件大小超过限制");
		}
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		try {
			FileUtil.uploadFile(file.getBytes(), saveUrl, newFileName);
		} catch (Exception e) {
			System.out.println("上传文件失败。");
			return  ApiResponse.getErrorResponse("上传文件失败。");
		}
		Notice notice = new Notice();
		notice.setText(text);
		notice.setImageUrl(saveUrl + newFileName);
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
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		NoticeExample noticeExample = new NoticeExample();
		noticeExample.createCriteria().andSenderEqualTo(me.getSchoolId()).andTypeEqualTo(1);
		noticeExample.setOrderByClause("create_time desc");
		List<Notice> notices = noticeMapper.selectByExample(noticeExample);
		for (Notice notice : notices) {

			notice.setImageUrl(httpServletRequest.getContextPath()  + "/initImage?imageUrl=" + notice.getImageUrl());
		}
		long total = page.getTotal();
		return new PageResponse<Notice>(total, notices);

	}

	@RequestMapping("/destroyNotice")
	@ResponseBody
	public ApiResponse destroyNotice(Long id) {
		noticeMapper.deleteByPrimaryKey(id);
		NoticeDetailExample detailExample = new NoticeDetailExample();
		detailExample.createCriteria().andNoticeIdEqualTo(id);
		noticeDetailMapper.deleteByExample(detailExample);
		return new ApiResponse();
	}

}

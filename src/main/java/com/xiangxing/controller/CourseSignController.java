package com.xiangxing.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.enums.SignFlagEnum;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.CourseMapper;
import com.xiangxing.mapper.CourseSignMapper;
import com.xiangxing.mapper.NoticeDetailMapper;
import com.xiangxing.mapper.NoticeMapper;
import com.xiangxing.mapper.StudentCourseMapper;
import com.xiangxing.mapper.SubjectMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.mapper.ex.CourseSignPoMapper;
import com.xiangxing.model.Course;
import com.xiangxing.model.CourseSign;
import com.xiangxing.model.Notice;
import com.xiangxing.model.NoticeDetail;
import com.xiangxing.model.StudentCourse;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.model.ex.CourseSignPo;
import com.xiangxing.utils.DateUtil;
import com.xiangxing.vo.api.LoginInfo;

@Controller
@RequestMapping("/courseSign")
public class CourseSignController extends BaseController {
	@Autowired
	private CourseSignPoMapper courseSignPoMapper;

	@Autowired
	private CourseSignMapper courseSignMapper;
	
	@Autowired
	private TeacherMapper teacherMapper;
	
	@Autowired
	private StudentCourseMapper studentCourseMapper;
	
	@Autowired
	private CourseMapper courseMapper;
	
	@Autowired
	NoticeDetailMapper noticeDetailMapper;
	
	@Autowired
	NoticeMapper noticeMapper;

	@RequestMapping("/courseSign")
	public String courseSign() {
		return "courseSign";
	}


	@RequestMapping("/courseSignList")
	@ResponseBody
	public PageResponse<CourseSignPo> courseSignList(PageRequest pageRequest,Long courseId, Long studentId) {
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		List<CourseSignPo> courseSignPos = courseSignPoMapper.getStudentSignInfo(courseId, studentId);
		for (CourseSignPo courseSignPo : courseSignPos) {
			courseSignPo.setShowSignTime(DateUtil.dateToString(courseSignPo.getSignTime(), DateUtil.patternA));
		}
		long total = page.getTotal();
		return new PageResponse<CourseSignPo>(total, courseSignPos);

	}

	@RequestMapping("/editcourseSignFlag")
	public void editcourseSignFlag(Long signFlag, Long id) {
		
		//获取签到详情
		CourseSign singinfo = courseSignMapper.selectByPrimaryKey(id);

		//修改签到记录
		CourseSign courseSign=new CourseSign();
		courseSign.setId(id);
		courseSign.setSignFlag(signFlag);
		courseSignMapper.updateByPrimaryKeySelective(courseSign);
		
		//获取学生课程
		StudentCourse studentCourse = studentCourseMapper.selectByPrimaryKey(singinfo.getStudentCourseId());
		if ((signFlag.equals(SignFlagEnum.qq.getMsg())||signFlag.equals(SignFlagEnum.qj.getMsg()))
				&&(singinfo.getSignFlag().equals(SignFlagEnum.qd.getMsg())||singinfo.getSignFlag().equals(SignFlagEnum.kk.getMsg()))) {
			// 修改课时
			studentCourse.setPeriod(studentCourse.getPeriod() - 2);
			studentCourseMapper.updateByPrimaryKey(studentCourse);
		}
		if (signFlag.equals(SignFlagEnum.qd.getMsg())||signFlag.equals(SignFlagEnum.kk.getMsg())) {
			// 修改课时
			studentCourse.setPeriod(studentCourse.getPeriod() + 2);
			studentCourseMapper.updateByPrimaryKey(studentCourse);

			//		发生消息
			Course course = courseMapper.selectByPrimaryKey(studentCourse.getCourseId());
			Notice notice = new Notice();
			notice.setType(2);
			notice.setText("您的孩子已经开始上课了，本学期还剩" + (course.getPeriod() - studentCourse.getPeriod() - 2) + "课时");
			if(signFlag.equals(SignFlagEnum.kk.getMsg())) {
				notice.setText("请您按时来上课，如有特殊情况请您提前请假，否则视为旷课。本学期还剩" + (course.getPeriod() - studentCourse.getPeriod() - 2) + "课时");
			}
			//获取老师
			notice.setSender(course.getTeacherId());
			notice.setCreateTime(new Date());
			notice.setSenderName(teacherMapper.selectByPrimaryKey(course.getTeacherId()).getName());
			noticeMapper.insertSelective(notice);
			NoticeDetail noticeDetail = new NoticeDetail();
			noticeDetail.setNoticeId(notice.getId());
			noticeDetail.setStatus(1);
			noticeDetail.setReceiver(studentCourse.getStudentId());
			// 签到消息
			noticeDetailMapper.insert(noticeDetail);
		}
		
		writeToOkResponse();
	}


}

package com.xiangxing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiangxing.mapper.CourseMapper;
import com.xiangxing.model.Course;
import com.xiangxing.model.CourseExample;
import com.xiangxing.utils.DateUtil;

@Component
public class Timer {
	
	@Autowired
	CourseMapper courseMapper;
	
	@Scheduled(cron="0 0 0 1/1 * ?")
	public void editCourseStatu() {
		CourseExample example = new CourseExample();
		example.createCriteria().andStatusEqualTo(0).andCurriculumTimeLessThanOrEqualTo(DateUtil.getDate());
		List<Course> l = courseMapper.selectByExample(example);
		for(Course course:l) {
			//状态为开课
			course.setStatus(1);
			courseMapper.updateByPrimaryKeySelective(course);
		}
		 example = new CourseExample();
		example.createCriteria().andStatusEqualTo(1).andFinishTimeLessThanOrEqualTo(DateUtil.getDate());
		 l = courseMapper.selectByExample(example);
		for(Course course:l) {
			//状态为完结
			course.setStatus(3);
			courseMapper.updateByPrimaryKeySelective(course);
		}
	}
	
	

}

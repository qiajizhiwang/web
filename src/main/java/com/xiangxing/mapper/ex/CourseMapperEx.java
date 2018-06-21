package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.CourseEx;

public interface CourseMapperEx {
	List<CourseEx> courseList(@Param("name") String name, @Param("schoolId") Long schoolId);
	
	List<CourseEx> courseListByStudentId(@Param("studentId") Long studentId);
}
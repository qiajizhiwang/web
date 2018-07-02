package com.xiangxing.mapper.ex;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.CourseSign;

public interface CourseSignPoMapper {

	List<CourseSign> getCourseSignInfo(@Param("courseId") Long courseId, @Param("studentId") Long studentId,
			@Param("singDate") Date singDate);
}
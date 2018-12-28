package com.xiangxing.mapper.ex;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.CourseEx;

public interface FinanceMapper {
	Long sumAllPeriodBySchool(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("schoolId") Long schoolId, @Param("type") Integer type);
	
	Long sumUsedPeriodBySchool(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("schoolId") Long schoolId, @Param("type") Integer type);

}
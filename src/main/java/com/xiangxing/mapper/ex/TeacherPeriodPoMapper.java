package com.xiangxing.mapper.ex;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.TeacherPeriodPo;

public interface TeacherPeriodPoMapper {

	List<TeacherPeriodPo> teacherPeriodData(@Param("schoolId") Long schoolId);
	List<TeacherPeriodPo> schoolPeriodData(@Param("headquartersId") Long headquartersId);
	List<TeacherPeriodPo> schoolTotalPeriodData(@Param("headquartersId") Long headquartersId);

	List<TeacherPeriodPo> teacherPeriodDataByDate(@Param("schoolId") Long schoolId,@Param("startDate") Date startDate, @Param("endDate") Date endDated);
	List<TeacherPeriodPo> schoolPeriodDataByDate(@Param("headquartersId") Long schoolId,@Param("startDate") Date startDate, @Param("endDate") Date endDated);

}

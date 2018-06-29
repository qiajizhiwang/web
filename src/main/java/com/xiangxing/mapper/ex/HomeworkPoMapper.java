package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.HomeworkPo;

public interface HomeworkPoMapper {

	List<HomeworkPo> list(@Param("studentId") Long studentId, @Param("courseId") Long courseId,
			@Param("status") Integer status);

}

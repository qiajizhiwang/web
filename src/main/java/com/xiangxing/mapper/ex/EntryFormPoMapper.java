package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.EntryFormPo;

public interface EntryFormPoMapper {

	List<EntryFormPo> list(@Param("schoolId") Long schoolId, @Param("studentName") String studentName,
			@Param("examId") Long examId, @Param("studentId") Long studentId);
	
	List<EntryFormPo> listByStudentId(@Param("studentId") Long studentId,@Param("status") Integer status);
	
	

}

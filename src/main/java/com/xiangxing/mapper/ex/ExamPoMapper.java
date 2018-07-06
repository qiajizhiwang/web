package com.xiangxing.mapper.ex;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.ExamPo;

public interface ExamPoMapper {
	ExamPo get(@Param("examId") Long examId);

}
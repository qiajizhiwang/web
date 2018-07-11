package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.MessageQueuePo;

public interface MessageQueuePoMapper {

	List<MessageQueuePo> list(@Param("teacherId") Long teacherId, @Param("studentId") Long studentId);
}
package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.MessagePo;

public interface MessagePoMapper {

	List<MessagePo> getStudentMessageInfo(@Param("studentId") Long studentId);
}
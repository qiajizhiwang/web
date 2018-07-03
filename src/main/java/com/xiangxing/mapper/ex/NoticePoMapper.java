package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.model.ex.NoticePo;

public interface NoticePoMapper {
	List<NoticePo> list(@Param("studentId") Long studentId);

}
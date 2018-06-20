package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.StudentPo;

public interface StudentPoMapper {

	List<StudentPo> list(@Param("name")String name, @Param("schoolId")Long schoolId);

}

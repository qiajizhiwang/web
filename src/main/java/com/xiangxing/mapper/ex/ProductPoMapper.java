package com.xiangxing.mapper.ex;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.model.ex.ProductPo;

public interface ProductPoMapper {

	List<ProductPo> list(@Param("name")String name, @Param("courseId")String courseId, @Param("schoolId")Long schoolId);

}

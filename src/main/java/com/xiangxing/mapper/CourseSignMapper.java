package com.xiangxing.mapper;

import com.xiangxing.model.CourseSign;
import com.xiangxing.model.CourseSignExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CourseSignMapper {
    int countByExample(CourseSignExample example);

    int deleteByExample(CourseSignExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CourseSign record);

    int insertSelective(CourseSign record);

    List<CourseSign> selectByExample(CourseSignExample example);

    CourseSign selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CourseSign record, @Param("example") CourseSignExample example);

    int updateByExample(@Param("record") CourseSign record, @Param("example") CourseSignExample example);

    int updateByPrimaryKeySelective(CourseSign record);

    int updateByPrimaryKey(CourseSign record);
}
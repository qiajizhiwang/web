package com.xiangxing.mapper;

import com.xiangxing.model.TeacherPeriod;
import com.xiangxing.model.TeacherPeriodExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TeacherPeriodMapper {
    int countByExample(TeacherPeriodExample example);

    int deleteByExample(TeacherPeriodExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TeacherPeriod record);

    int insertSelective(TeacherPeriod record);

    List<TeacherPeriod> selectByExample(TeacherPeriodExample example);

    TeacherPeriod selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TeacherPeriod record, @Param("example") TeacherPeriodExample example);

    int updateByExample(@Param("record") TeacherPeriod record, @Param("example") TeacherPeriodExample example);

    int updateByPrimaryKeySelective(TeacherPeriod record);

    int updateByPrimaryKey(TeacherPeriod record);
}
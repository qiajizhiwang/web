package com.xiangxing.mapper;

import com.xiangxing.model.Student_course;
import com.xiangxing.model.Student_courseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Student_courseMapper {
    int countByExample(Student_courseExample example);

    int deleteByExample(Student_courseExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Student_course record);

    int insertSelective(Student_course record);

    List<Student_course> selectByExample(Student_courseExample example);

    Student_course selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Student_course record, @Param("example") Student_courseExample example);

    int updateByExample(@Param("record") Student_course record, @Param("example") Student_courseExample example);

    int updateByPrimaryKeySelective(Student_course record);

    int updateByPrimaryKey(Student_course record);
}
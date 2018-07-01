package com.xiangxing.mapper;

import com.xiangxing.model.StudentHomework;
import com.xiangxing.model.StudentHomeworkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudentHomeworkMapper {
    int countByExample(StudentHomeworkExample example);

    int deleteByExample(StudentHomeworkExample example);

    int deleteByPrimaryKey(Long id);

    int insert(StudentHomework record);

    int insertSelective(StudentHomework record);

    List<StudentHomework> selectByExample(StudentHomeworkExample example);

    StudentHomework selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") StudentHomework record, @Param("example") StudentHomeworkExample example);

    int updateByExample(@Param("record") StudentHomework record, @Param("example") StudentHomeworkExample example);

    int updateByPrimaryKeySelective(StudentHomework record);

    int updateByPrimaryKey(StudentHomework record);
}
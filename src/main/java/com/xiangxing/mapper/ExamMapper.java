package com.xiangxing.mapper;

import com.xiangxing.model.Exam;
import com.xiangxing.model.ExamExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExamMapper {
    int countByExample(ExamExample example);

    int deleteByExample(ExamExample example);

    int deleteByPrimaryKey(String id);

    int insert(Exam record);

    int insertSelective(Exam record);

    List<Exam> selectByExampleWithBLOBs(ExamExample example);

    List<Exam> selectByExample(ExamExample example);

    Exam selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Exam record, @Param("example") ExamExample example);

    int updateByExampleWithBLOBs(@Param("record") Exam record, @Param("example") ExamExample example);

    int updateByExample(@Param("record") Exam record, @Param("example") ExamExample example);

    int updateByPrimaryKeySelective(Exam record);

    int updateByPrimaryKeyWithBLOBs(Exam record);

    int updateByPrimaryKey(Exam record);
}
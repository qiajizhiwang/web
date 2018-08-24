package com.xiangxing.mapper;

import com.xiangxing.model.ExamRank;
import com.xiangxing.model.ExamRankExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExamRankMapper {
    int countByExample(ExamRankExample example);

    int deleteByExample(ExamRankExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ExamRank record);

    int insertSelective(ExamRank record);

    List<ExamRank> selectByExample(ExamRankExample example);

    ExamRank selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ExamRank record, @Param("example") ExamRankExample example);

    int updateByExample(@Param("record") ExamRank record, @Param("example") ExamRankExample example);

    int updateByPrimaryKeySelective(ExamRank record);

    int updateByPrimaryKey(ExamRank record);
}
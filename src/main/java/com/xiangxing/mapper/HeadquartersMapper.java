package com.xiangxing.mapper;

import com.xiangxing.model.Headquarters;
import com.xiangxing.model.HeadquartersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HeadquartersMapper {
    int countByExample(HeadquartersExample example);

    int deleteByExample(HeadquartersExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Headquarters record);

    int insertSelective(Headquarters record);

    List<Headquarters> selectByExample(HeadquartersExample example);

    Headquarters selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Headquarters record, @Param("example") HeadquartersExample example);

    int updateByExample(@Param("record") Headquarters record, @Param("example") HeadquartersExample example);

    int updateByPrimaryKeySelective(Headquarters record);

    int updateByPrimaryKey(Headquarters record);
}
package com.xiangxing.mapper;

import com.xiangxing.model.SchoolImage;
import com.xiangxing.model.SchoolImageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SchoolImageMapper {
    int countByExample(SchoolImageExample example);

    int deleteByExample(SchoolImageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SchoolImage record);

    int insertSelective(SchoolImage record);

    List<SchoolImage> selectByExample(SchoolImageExample example);

    SchoolImage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SchoolImage record, @Param("example") SchoolImageExample example);

    int updateByExample(@Param("record") SchoolImage record, @Param("example") SchoolImageExample example);

    int updateByPrimaryKeySelective(SchoolImage record);

    int updateByPrimaryKey(SchoolImage record);
}
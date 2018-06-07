package com.xiangxing.mapper;

import com.xiangxing.model.Entry_form;
import com.xiangxing.model.Entry_formExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Entry_formMapper {
    int countByExample(Entry_formExample example);

    int deleteByExample(Entry_formExample example);

    int insert(Entry_form record);

    int insertSelective(Entry_form record);

    List<Entry_form> selectByExample(Entry_formExample example);

    int updateByExampleSelective(@Param("record") Entry_form record, @Param("example") Entry_formExample example);

    int updateByExample(@Param("record") Entry_form record, @Param("example") Entry_formExample example);
}
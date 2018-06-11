package com.xiangxing.mapper;

import com.xiangxing.model.EntryForm;
import com.xiangxing.model.EntryFormExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EntryFormMapper {
    int countByExample(EntryFormExample example);

    int deleteByExample(EntryFormExample example);

    int deleteByPrimaryKey(Long id);

    int insert(EntryForm record);

    int insertSelective(EntryForm record);

    List<EntryForm> selectByExample(EntryFormExample example);

    EntryForm selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") EntryForm record, @Param("example") EntryFormExample example);

    int updateByExample(@Param("record") EntryForm record, @Param("example") EntryFormExample example);

    int updateByPrimaryKeySelective(EntryForm record);

    int updateByPrimaryKey(EntryForm record);
}
package com.xiangxing.mapper;

import com.xiangxing.model.NoticeDetail;
import com.xiangxing.model.NoticeDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NoticeDetailMapper {
    int countByExample(NoticeDetailExample example);

    int deleteByExample(NoticeDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(NoticeDetail record);

    int insertSelective(NoticeDetail record);

    List<NoticeDetail> selectByExample(NoticeDetailExample example);

    NoticeDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") NoticeDetail record, @Param("example") NoticeDetailExample example);

    int updateByExample(@Param("record") NoticeDetail record, @Param("example") NoticeDetailExample example);

    int updateByPrimaryKeySelective(NoticeDetail record);

    int updateByPrimaryKey(NoticeDetail record);
}
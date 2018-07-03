package com.xiangxing.mapper;

import com.xiangxing.model.MessageQueue;
import com.xiangxing.model.MessageQueueExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessageQueueMapper {
    int countByExample(MessageQueueExample example);

    int deleteByExample(MessageQueueExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MessageQueue record);

    int insertSelective(MessageQueue record);

    List<MessageQueue> selectByExample(MessageQueueExample example);

    MessageQueue selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MessageQueue record, @Param("example") MessageQueueExample example);

    int updateByExample(@Param("record") MessageQueue record, @Param("example") MessageQueueExample example);

    int updateByPrimaryKeySelective(MessageQueue record);

    int updateByPrimaryKey(MessageQueue record);
}
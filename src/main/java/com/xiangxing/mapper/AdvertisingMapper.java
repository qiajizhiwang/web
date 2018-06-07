package com.xiangxing.mapper;

import com.xiangxing.model.Advertising;
import com.xiangxing.model.AdvertisingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdvertisingMapper {
    int countByExample(AdvertisingExample example);

    int deleteByExample(AdvertisingExample example);

    int insert(Advertising record);

    int insertSelective(Advertising record);

    List<Advertising> selectByExample(AdvertisingExample example);

    int updateByExampleSelective(@Param("record") Advertising record, @Param("example") AdvertisingExample example);

    int updateByExample(@Param("record") Advertising record, @Param("example") AdvertisingExample example);
}
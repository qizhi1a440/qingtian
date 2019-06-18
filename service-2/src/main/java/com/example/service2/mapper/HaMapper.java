package com.example.service2.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HaMapper {

    @Insert("insert into ha (haname) value(#{haname})")
    int insertHa(@Param("haname")String haname);
}

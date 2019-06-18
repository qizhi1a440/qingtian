package com.example.service1.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HiMapper {

    @Insert("insert into hi (hiname) value(#{hiname})")
    int insertHi(@Param("hiname")String hiname);
}

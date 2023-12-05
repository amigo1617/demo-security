package com.example.demo.ts.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TsMapper {
    Map<String,String> selectUser(String username) throws Exception;
}

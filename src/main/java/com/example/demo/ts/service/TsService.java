package com.example.demo.ts.service;

import com.example.demo.ts.mapper.TsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TsService {

    @Autowired
    private TsMapper tsMapper;

    public Map<String,String> selectUser(String username) throws Exception {
        return tsMapper.selectUser(username);
    }
}

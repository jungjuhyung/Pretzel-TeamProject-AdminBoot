package com.ict.pretzel_admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ict.pretzel_admin.common.SubsCountVO;
import com.ict.pretzel_admin.mapper.AdminMapper;

@Service
public class DashBoardService {
    
    @Autowired
    private AdminMapper adminMapper;

    public ResponseEntity<?> sub_count() {

        List<SubsCountVO> subs_count = adminMapper.sub_count();

        return ResponseEntity.ok(subs_count);
    }
}

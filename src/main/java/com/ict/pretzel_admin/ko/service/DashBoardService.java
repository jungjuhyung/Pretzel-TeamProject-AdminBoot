package com.ict.pretzel_admin.ko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ict.pretzel_admin.ko.mapper.DashBoardMapper;
import com.ict.pretzel_admin.ko.vo.MonthCountVO;
import com.ict.pretzel_admin.ko.vo.SubCountVO;
import com.ict.pretzel_admin.ko.vo.ThemaCountVO;
import com.ict.pretzel_admin.vo.MovieVO;

@Service
public class DashBoardService {
    
    @Autowired
    private DashBoardMapper dashBoardMapper;

    // 구독별 유저 카운트
    public ResponseEntity<?> sub_count() {

        List<SubCountVO> subs_count = dashBoardMapper.sub_count();

        return ResponseEntity.ok(subs_count);
    }

    // 월별 유저수
    public ResponseEntity<?> month_count() {

        List<MonthCountVO> month_count = dashBoardMapper.month_count();
        
        return ResponseEntity.ok(month_count);
    }


    // 영화 장르 카운트
    public ResponseEntity<?> thema_count() {

        List<ThemaCountVO> thema_count = dashBoardMapper.thema_count();

        return ResponseEntity.ok(thema_count);
    }

    // 시청률 순 상위 10개
    public ResponseEntity<?> top_view() {

        List<MovieVO> top_view = dashBoardMapper.top_view();

        return ResponseEntity.ok(top_view);
    }



}

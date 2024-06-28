package com.ict.pretzel_admin.ko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ict.pretzel_admin.ko.mapper.DashBoardMapper;
import com.ict.pretzel_admin.ko.vo.SubsCountVO;
import com.ict.pretzel_admin.ko.vo.ThemaCountVO;
import com.ict.pretzel_admin.vo.MovieVO;

@Service
public class DashBoardService {
    
    @Autowired
    private DashBoardMapper dashBoardMapper;

    // 구독별 유저 카운트
    public ResponseEntity<?> sub_count() {

        List<SubsCountVO> subs_count = dashBoardMapper.sub_count();

        return ResponseEntity.ok(subs_count);
    }

    // 월별 유저수


    // 영화 장르 카운트
    public ResponseEntity<?> thema_count() {

        List<ThemaCountVO> tema_count = dashBoardMapper.thema_count();

        return ResponseEntity.ok(tema_count);
    }

    // 시청률 순 상위 10개
    public ResponseEntity<?> top_view() {

        List<MovieVO> top_view = dashBoardMapper.top_view();

        return ResponseEntity.ok(top_view);
    }



}

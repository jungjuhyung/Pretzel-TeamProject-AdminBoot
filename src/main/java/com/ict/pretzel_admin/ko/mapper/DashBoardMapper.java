package com.ict.pretzel_admin.ko.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.pretzel_admin.ko.vo.MonthCountVO;
import com.ict.pretzel_admin.ko.vo.SubCountVO;
import com.ict.pretzel_admin.ko.vo.ThemaCountVO;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.MovieVO;

@Mapper
public interface DashBoardMapper {
    
    // 관리자 상세
    AdminVO admin_detail(@Param("admin_id") String admin_id);

    // 구독별 유저 카운트
    List<SubCountVO> sub_count();

    // 월별 유저수
    List<MonthCountVO> month_count();

    // 장르별 영화 카운트
    List<ThemaCountVO> thema_count();

    // 시청률 순 상위 10개
    List<MovieVO> top_view();


    
}

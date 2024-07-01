package com.ict.pretzel_admin.ko.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.QuestionVO;
import com.ict.pretzel_admin.vo.ReportVO;

@Mapper
public interface MasterAdminMapper {

    // 전체 관리자 수
    int total_admin();

    // 관리자 리스트
    List<AdminVO> admin_list(Paging paging);

    // 관리자 추가
    int admin_insert(AdminVO admin);
    
    // 신고처리 리스트
    List<ReportVO> admin_report(@Param("admin_id") String admin_id);

    // 1대1문의 처리 리스트
    List<QuestionVO> admin_quest(@Param("admin_id") String admin_id);

    // 관리자 수정
    int admin_update(AdminVO admin); 

}

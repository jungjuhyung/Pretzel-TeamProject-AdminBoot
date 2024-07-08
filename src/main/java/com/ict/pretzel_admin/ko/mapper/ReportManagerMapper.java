package com.ict.pretzel_admin.ko.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.vo.ReportVO;
import com.ict.pretzel_admin.vo.ReviewVO;

@Mapper
public interface ReportManagerMapper {

    /* 신고 관리 */
    int total_report();

    List<ReportVO> report_list(Paging paging);

    int report_count();

    ReportVO report_detail(@Param("report_idx") String report_idx);

    ReviewVO reported_review(@Param("review_idx") String review_idx);

    int report_ok(ReportVO report);

    void review_delete(ReportVO report);

}

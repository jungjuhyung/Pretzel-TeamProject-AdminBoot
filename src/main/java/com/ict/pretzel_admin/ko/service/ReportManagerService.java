package com.ict.pretzel_admin.ko.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.ko.mapper.DashBoardMapper;
import com.ict.pretzel_admin.ko.mapper.ReportManagerMapper;
import com.ict.pretzel_admin.ko.mapper.UserManagerMapper;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.ProfileVO;
import com.ict.pretzel_admin.vo.ReportVO;
import com.ict.pretzel_admin.vo.ReviewVO;

@Service
public class ReportManagerService {

    @Autowired
    private ReportManagerMapper reportManagerMapper;

    @Autowired
    private UserManagerMapper userManagerMapper;

     @Autowired
    private DashBoardMapper dashBoardMapper;

    @Autowired
    private Paging paging;

    public ResponseEntity<?> report_list(String cPage) {

        // 페이징 기법
		int count = reportManagerMapper.total_report();
		paging.setTotalRecord(count);

		if (paging.getTotalRecord() <= paging.getNumPerPage()) {
			paging.setTotalPage(1);
		} else {
			paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
			if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
				paging.setTotalPage(paging.getTotalPage() + 1);
			}
		}

		paging.setNowPage(Integer.parseInt(cPage));

		paging.setOffset(paging.getNumPerPage() * (paging.getNowPage() - 1));

		paging.setBeginBlock((int) ((paging.getNowPage() - 1) 
				/ paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);

		paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

		if (paging.getEndBlock() > paging.getTotalPage()) {
			paging.setEndBlock(paging.getTotalPage());
		}

        List<ReportVO> report_list = reportManagerMapper.report_list(paging);
        for (ReportVO k : report_list) {
            if(k.getAdmin_id() != null){
                AdminVO admin = dashBoardMapper.admin_detail(k.getAdmin_id());
                k.setAdmin_name(admin.getName());
            }
        }

        int report_count = reportManagerMapper.report_count();

        Map<String, Object> result = new HashMap<>();
        result.put("report_list", report_list);
        result.put("count", report_count);
        result.put("paging", paging);

        return ResponseEntity.ok(result);
    }

    // 신고 상세
    public ResponseEntity<?> report_detail(String report_idx, String admin_id) {
        
        // 신고 정보(신고유형, 프로필idx, 리뷰idx)
        ReportVO report = reportManagerMapper.report_detail(report_idx);

        // 신고한 프로필idx -> 신고한 프로필(프로필명, 유저아이디)
        ProfileVO report_profile = userManagerMapper.profile_detail(report.getProfile_idx());

        // 리뷰idx -> 신고당한 리뷰(리뷰내용, 프로필idx)
        ReviewVO reported_review = reportManagerMapper.reported_review(report.getReview_idx());

        // 신고당한 프로필idx -> 신고당한 프로필(유저아이디)
        ProfileVO reported_profile = userManagerMapper.profile_detail(reported_review.getProfile_idx());
        
        Map<String, Object> result = new HashMap<>();

        // 답변한 관리자 아이디 -> 답변한 관리자(이름)
        if (report.getAdmin_id() != null) {
            AdminVO admin = dashBoardMapper.admin_detail(report.getAdmin_id());
            result.put("admin_name", admin.getName()); // 답변한 관리자 이름
        }else{
            report.setAdmin_id(admin_id);
            AdminVO admin = dashBoardMapper.admin_detail(admin_id);
            result.put("admin_name", admin.getName()); // 현재 관리자 이름
        }

        result.put("report", report); // 신고VO
        result.put("profile_name", report_profile.getName()); // 신고한 프로필
        result.put("user_id", reported_profile.getUser_id()); // 신고당한 유저
        result.put("content", reported_review.getContent()); // 리뷰 내용
        result.put("movie_title", reported_review.getKorea_title()); // 리뷰쓴 영화

        return ResponseEntity.ok(result);
    }

    // 신고 처리
    @Transactional
    public ResponseEntity<?> report_ok(String admin_id, ReportVO report) {

        // 처리할 때만 리뷰 삭제
        if (report.getReview_idx().equals("0")) {
            reportManagerMapper.review_delete(report);
        }

        report.setAdmin_id(admin_id);
        int result = reportManagerMapper.report_ok(report);

        return ResponseEntity.ok(result);
    }


}

package com.ict.pretzel_admin.ko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.ko.mapper.MasterAdminMapper;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.QuestionVO;
import com.ict.pretzel_admin.vo.ReportVO;

@Service
public class MasterAdminService {

    @Autowired
    private MasterAdminMapper masterAdminMapper;

    @Autowired
    private Paging paging;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 관리자 리스트
    public ResponseEntity<?> admin_list(String cPage) {
        // 페이징 기법
		int count = masterAdminMapper.total_admin();
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

        List<AdminVO> admin_list = masterAdminMapper.admin_list(paging);

        return ResponseEntity.ok(admin_list);
    }

    // 관리자 추가
    public ResponseEntity<?> admin_insert(AdminVO admin) {

        admin.setPwd(passwordEncoder.encode(admin.getPwd()));

        int result = masterAdminMapper.admin_insert(admin);

        return ResponseEntity.ok(result);
    }

    // 신고처리 리스트(관리자 상세)
    public ResponseEntity<?> admin_report(String admin_id) {

        List<ReportVO> report_list = masterAdminMapper.admin_report(admin_id);

        return ResponseEntity.ok(report_list);
    }
    
    // 1대1문의 처리 리스트(관리자 상세)
    public ResponseEntity<?> admin_quest(String admin_id) {

        List<QuestionVO> quest_list = masterAdminMapper.admin_quest(admin_id);

        return ResponseEntity.ok(quest_list);
    }

    // 관리자 수정
    public ResponseEntity<?> admin_update(AdminVO admin) {

        int result = masterAdminMapper.admin_update(admin); 

        return ResponseEntity.ok(result);
    }


    
}

package com.ict.pretzel_admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.jwt.JwtDecode;
import com.ict.pretzel_admin.mapper.AdminMapper;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.ProfileVO;
import com.ict.pretzel_admin.vo.QuestionVO;
import com.ict.pretzel_admin.vo.ReportVO;
import com.ict.pretzel_admin.vo.ReviewVO;

@Service
public class QuestionManagerService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private Paging paging;
    
    // 1대1문의 리스트
    public ResponseEntity<?> quest_list(String cPage) {
        // 페이징 기법
		int count = adminMapper.total_quest();
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

        List<QuestionVO> quest_list = adminMapper.quest_list(paging);       
        int quest_count = adminMapper.quest_count();

        Map<String, Object> result = new HashMap<>();
        result.put("quest_list", quest_list);
        result.put("count", quest_count);

        return ResponseEntity.ok(result);
    }

    // 1대1문의 상세
    public ResponseEntity<?> quest_detail(String question_idx) {
        
        // 문의 정보(제목, 내용, 프로필idx, 작성날짜, 상태 / 답변, 답변날짜, 관리자아이디)
        QuestionVO question = adminMapper.question_detail(question_idx);

        // 문의한 프로필idx -> 문의한 프로필(프로필명, 유저아이디)
        ProfileVO quest_profile = adminMapper.profile_detail(question.getProfile_idx());

        Map<String, Object> result = new HashMap<>();
        
        // 답변한 관리자 아이디 -> 답변한 관리자(이름)
        if (question.getAdmin_id() != null) {
            AdminVO admin = adminMapper.admin_detail(question.getAdmin_id());
            result.put("admin_name", admin.getName()); // 답변한 관리자 이름
        }

        result.put("question", question); // 1대1문의VO
        result.put("quest_profile", quest_profile); // 문의한 프로필

        return ResponseEntity.ok(result);
    }

    // 1대1 문의 답변
    public ResponseEntity<?> quest_answer(QuestionVO question) {

        int result = adminMapper.quest_answer(question);

        return ResponseEntity.ok(result);
    }

}

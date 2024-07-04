package com.ict.pretzel_admin.ko.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.ko.mapper.DashBoardMapper;
import com.ict.pretzel_admin.ko.mapper.QuestionManagerMapper;
import com.ict.pretzel_admin.ko.mapper.UserManagerMapper;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.ProfileVO;
import com.ict.pretzel_admin.vo.QuestionVO;

@Service
public class QuestionManagerService {

    @Autowired
    private QuestionManagerMapper questionManagerMapper;

    @Autowired
    private DashBoardMapper dashBoardMapper;

    @Autowired
    private UserManagerMapper userManagerMapper;

    @Autowired
    private Paging paging;
    
    // 1대1문의 리스트
    public ResponseEntity<?> quest_list(String cPage) {
        // 페이징 기법
		int count = questionManagerMapper.total_quest();
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

        List<QuestionVO> quest_list = questionManagerMapper.quest_list(paging); 
        for (QuestionVO k : quest_list) {
            ProfileVO profile = userManagerMapper.profile_detail(k.getProfile_idx());
            k.setUser_id(profile.getUser_id());
            k.setName(profile.getName());
        }      
        int quest_count = questionManagerMapper.quest_count();

        Map<String, Object> result = new HashMap<>();
        result.put("quest_list", quest_list);
        result.put("count", quest_count);

        return ResponseEntity.ok(result);
    }

    // 1대1문의 상세
    public ResponseEntity<?> quest_detail(String question_idx) {
        
        // 문의 정보(제목, 내용, 프로필idx, 작성날짜, 상태 / 답변, 답변날짜, 관리자아이디)
        QuestionVO question = questionManagerMapper.question_detail(question_idx);

        // 문의한 프로필idx -> 문의한 프로필(프로필명, 유저아이디)
        ProfileVO quest_profile = userManagerMapper.profile_detail(question.getProfile_idx());

        Map<String, Object> result = new HashMap<>();
        
        // 답변한 관리자 아이디 -> 답변한 관리자(이름)
        if (question.getAdmin_id() != null) {
            AdminVO admin = dashBoardMapper.admin_detail(question.getAdmin_id());
            result.put("admin_name", admin.getName()); // 답변한 관리자 이름
        }

        result.put("question", question); // 1대1문의VO
        result.put("quest_profile", quest_profile); // 문의한 프로필

        return ResponseEntity.ok(result);
    }

    // 1대1 문의 답변
    public ResponseEntity<?> quest_answer(QuestionVO question) {

        int result = questionManagerMapper.quest_answer(question);

        return ResponseEntity.ok(result);
    }

}

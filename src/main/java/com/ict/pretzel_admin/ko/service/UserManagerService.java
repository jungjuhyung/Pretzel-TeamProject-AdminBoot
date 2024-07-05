package com.ict.pretzel_admin.ko.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.ko.mapper.UserManagerMapper;
import com.ict.pretzel_admin.vo.ProfileVO;
import com.ict.pretzel_admin.vo.UserVO;

@Service
public class UserManagerService {

    @Autowired
    private UserManagerMapper userManagerMapper;

    @Autowired
    private Paging paging;
    
    // 유저 리스트
    public ResponseEntity<?> user_list(String cPage, String keyword) {
        // 페이징 기법
		int count = userManagerMapper.total_user();
		paging.setTotalRecord(count);

		if (paging.getTotalRecord() <= paging.getNumPerPage()) {
			paging.setTotalPage(1);
		} else {
			paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
			if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
				paging.setTotalPage(paging.getTotalPage() + 1);
			}
		}

        paging.setNumPerPage(8);

		paging.setNowPage(Integer.parseInt(cPage));

		paging.setOffset(paging.getNumPerPage() * (paging.getNowPage() - 1));

		paging.setBeginBlock((int) ((paging.getNowPage() - 1) 
				/ paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);

		paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

		if (paging.getEndBlock() > paging.getTotalPage()) {
			paging.setEndBlock(paging.getTotalPage());
		}

        paging.setKeyword(keyword);
        List<UserVO> user_list = userManagerMapper.user_list(paging);

        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("user_list", user_list);
        return ResponseEntity.ok(result);
    }

    // 유저 상세
    public ResponseEntity<?> user_detail(String user_id){

        UserVO user = userManagerMapper.user_detail(user_id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.ok("0");
    }
    
    // 프로필 리스트
    public ResponseEntity<?> profile_list(String user_id){

        List<ProfileVO> profile_list = userManagerMapper.profile_list(user_id);
        if (profile_list.isEmpty()) {
            return ResponseEntity.ok("0");
        }
        return ResponseEntity.ok(profile_list);
    }

    
    // 정지 시키기
    public ResponseEntity<?> user_stop(String admin_id, String user_id) {
        
        UserVO user = new UserVO();
        user.setAdmin_id(admin_id);
        user.setUser_id(user_id);
        int result = userManagerMapper.user_stop(user);
        
        return ResponseEntity.ok(result);
    }
    
    // 정지 해제 (활성화)
    public ResponseEntity<?> user_recover(String admin_id, String user_id) {
        
        UserVO user = new UserVO();
        user.setAdmin_id(admin_id);
        user.setUser_id(user_id);
        int result = userManagerMapper.user_recover(user);
        
        return ResponseEntity.ok(result);
    }

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 비밀번호 초기화
    public ResponseEntity<?> pwd_reset(String user_id) {
        UserVO user = userManagerMapper.user_detail(user_id);
        if (user_id != null) {
            //	인증번호 6자리 만들기
            Random random = new Random();
            String randomNumber = String.valueOf(random.nextInt(1000000) % 1000000);
             if(randomNumber.length() < 6) {
                int substract = 6 - randomNumber.length();
                StringBuffer sb = new StringBuffer();
                for(int i=0; i<substract; i++) {
                    sb.append("0");
                }
                sb.append(randomNumber);
                randomNumber = sb.toString();
            }
            //	사용자 이메일로 인증번호 보내기
            mailService.sendEmail(randomNumber, user.getEmail());
            
            // 비밀번호 초기화 하기
            user.setPwd(passwordEncoder.encode(randomNumber));
            int result = userManagerMapper.pwd_reset(user);

            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok("0");
    }






}

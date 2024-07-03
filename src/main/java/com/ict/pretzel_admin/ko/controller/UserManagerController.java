package com.ict.pretzel_admin.ko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.pretzel_admin.jwt.JwtDecode;
import com.ict.pretzel_admin.ko.service.UserManagerService;
import com.ict.pretzel_admin.vo.UserVO;

@RestController
@RequestMapping("/user")
public class UserManagerController {
    
    @Autowired
    private UserManagerService userManagerService;

    // 유저 리스트(초기, 검색, 페이징 다 가능)
    @GetMapping("/user_list")
    public ResponseEntity<?> user_list(@RequestParam(value = "cPage", defaultValue = "1") String cPage, 
                @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return userManagerService.user_list(cPage, keyword);
    }
    
    // 유저 상세
    @PostMapping("/user_detail")
    public ResponseEntity<?> user_detail(@RequestBody UserVO user){
        return userManagerService.user_detail(user.getUser_id());
    }
    
    // 프로필 리스트
    @PostMapping("/profile_list")
    public ResponseEntity<?> profile_list(@RequestBody UserVO user){
        return userManagerService.profile_list(user.getUser_id());
    }
    

    // 정지 시키기
    @PostMapping("/user_stop")
    public ResponseEntity<?> user_stop(@RequestHeader("Authorization") String token, 
                                    @RequestBody UserVO user) {
        JwtDecode jwtDecode = new JwtDecode(token);
        return userManagerService.user_stop(jwtDecode.getAdmin_id(), user.getUser_id());
    }
    
    // 정지 해제 (활성화)
    @PostMapping("/user_recover")
    public ResponseEntity<?> user_recover(@RequestHeader("Authorization") String token, 
                                            @RequestBody UserVO user) {
        JwtDecode jwtDecode = new JwtDecode(token);
        return userManagerService.user_recover(jwtDecode.getAdmin_id(), user.getUser_id());
    }
    
    // 비밀번호 초기화
    @PostMapping("/pwd_reset")
    public ResponseEntity<?> pwd_reset(@RequestBody UserVO user){
        return userManagerService.pwd_reset(user.getUser_id());
    }




}

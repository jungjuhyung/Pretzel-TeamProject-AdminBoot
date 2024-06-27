package com.ict.pretzel_admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ict.pretzel_admin.jwt.JwtDecode;
import com.ict.pretzel_admin.service.UserManagerService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

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
    @GetMapping("/user_detail")
    public ResponseEntity<?> user_detail(@RequestParam("user_id") String user_id){
        return userManagerService.user_detail(user_id);
    }
    
    // 프로필 리스트
    @GetMapping("/profile_list")
    public ResponseEntity<?> profile_list(@RequestParam("user_id") String user_id){
        return userManagerService.profile_list(user_id);
    }
    

    // 정지 시키기
    @PostMapping("/user_fire")
    public ResponseEntity<?> user_stop(@RequestHeader("Authorization") String token, 
                                    @RequestParam("user_id") String user_id) {
        JwtDecode jwtDecode = new JwtDecode(token);
        return userManagerService.user_stop(jwtDecode.getAdmin_id(), user_id);
    }
    
    // 정지 해제 (활성화)
    @PostMapping("/user_recover")
    public ResponseEntity<?> user_recover(@RequestHeader("Authorization") String token, 
                                    @RequestParam("user_id") String user_id) {
        JwtDecode jwtDecode = new JwtDecode(token);
        return userManagerService.user_recover(jwtDecode.getAdmin_id(), user_id);
    }
    
    // 비밀번호 초기화
    @GetMapping("/pwd_reset")
    public ResponseEntity<?> pwd_reset(@RequestParam("user_id") String user_id){
        return userManagerService.pwd_reset(user_id);
    }




}

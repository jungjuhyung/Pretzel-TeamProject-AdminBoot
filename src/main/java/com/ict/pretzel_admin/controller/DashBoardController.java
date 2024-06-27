package com.ict.pretzel_admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.pretzel_admin.service.AuthSevice;
import com.ict.pretzel_admin.service.DashBoardService;
import com.ict.pretzel_admin.vo.DataVO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ict.pretzel_admin.vo.AdminVO;


@RestController
@RequestMapping("/main")
public class DashBoardController {

    @Autowired
    private AuthSevice authService;

    // 관리자 로그인
    @PostMapping("/admin_login")
    public ResponseEntity<?> admin_login(@RequestBody AdminVO admin) {
        // 정보도 토큰도 모두 다 있다.
        DataVO dataVO = authService.authenticate(admin);
        if (dataVO != null) {
            return ResponseEntity.ok(dataVO);
        }else {
            // 정보가 없으면 null 보내기
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("0");
        }
        //return  authService.authenticate(mvo);
    }

    @Autowired
    private DashBoardService dashBoardService;

    // 구독별 유저수
    @GetMapping("/sub_count")
    public ResponseEntity<?> sub_count() {
        return dashBoardService.sub_count();
    }
    

    

}

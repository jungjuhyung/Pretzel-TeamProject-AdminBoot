package com.ict.pretzel_admin.ko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.pretzel_admin.jwt.service.AuthSevice;
import com.ict.pretzel_admin.ko.service.DashBoardService;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.DataVO;

@RestController
@RequestMapping("/main")
public class DashBoardController {

    @Autowired
    private AuthSevice authService;

    @Autowired
    private DashBoardService dashBoardService;

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

    // 구독별 유저수
    @GetMapping("/sub_count")
    public ResponseEntity<?> sub_count() {
        return dashBoardService.sub_count();
    }
    
    // 월별 유저수
    


    // 영화 장르별 개수
    @GetMapping("/thema_count")
    public ResponseEntity<?> thema_count() {
        return dashBoardService.thema_count();
    }
    
    // 시청률 순 상위 10개
    @GetMapping("/top_view")
    public ResponseEntity<?> top_view() {
        return dashBoardService.top_view();
    }
    
    

}

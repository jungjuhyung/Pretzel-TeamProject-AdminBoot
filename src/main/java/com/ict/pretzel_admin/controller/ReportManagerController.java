package com.ict.pretzel_admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.pretzel_admin.jwt.JwtDecode;
import com.ict.pretzel_admin.service.ReportManagerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@RequestMapping("/report")
public class ReportManagerController {
    
    @Autowired
    private ReportManagerService reportManagerService;

    // 신고 목록 (신고글, 미처리신고 수)
    @GetMapping("/report_list")
    public ResponseEntity<?> report_list(@RequestParam(value = "cPage", defaultValue = "1") String cPage) {
        return reportManagerService.report_list(cPage);
    }

    // 신고 상세 페이지
    @GetMapping("/report_detail")
    public ResponseEntity<?> report_detail(@RequestParam("report_idx") String report_idx) {
        return reportManagerService.report_detail(report_idx);
    }
    
    // 신고 처리
    @PostMapping("/report_ok")
    public ResponseEntity<?> report_ok(@RequestHeader("Authorization") String token, 
                                    @RequestParam("report_idx") String report_idx) {
        JwtDecode jwtDecode = new JwtDecode(token);            
        return reportManagerService.report_ok(jwtDecode.getAdmin_id(), report_idx);
    }
    

}

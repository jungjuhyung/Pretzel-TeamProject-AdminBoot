package com.ict.pretzel_admin.ko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.pretzel_admin.ko.service.MasterAdminService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ict.pretzel_admin.vo.AdminVO;


@RestController
@RequestMapping("/master")
public class MasterAdminController {

    @Autowired
    private MasterAdminService masterAdminService;
    
    // 관리자 리스트
    @GetMapping("/admin_list")
    public ResponseEntity<?> admin_list(@RequestParam(value = "cPage", defaultValue = "1") String cPage) {
        return masterAdminService.admin_list(cPage);
    }

    // 관리자 추가
    @GetMapping("/admin_insert")
    public ResponseEntity<?> admin_insert(@RequestBody AdminVO admin) {
        return masterAdminService.admin_insert(admin);
    }
    
    // 신고 처리 리스트
    @GetMapping("/admin_report")
    public ResponseEntity<?> admin_report(@RequestParam("admin_id") String admin_id) {
        return masterAdminService.admin_report(admin_id);
    }
    
    // 1대1문의 처리 리스트
    @GetMapping("/admin_quest")
    public ResponseEntity<?> admin_quest(@RequestParam("admin_id") String admin_id) {
        return masterAdminService.admin_quest(admin_id);
    }

    // 관리자 수정
    @GetMapping("/admin_update")
    public ResponseEntity<?> admin_update(@RequestBody AdminVO admin) {
        return masterAdminService.admin_update(admin);
    }

}

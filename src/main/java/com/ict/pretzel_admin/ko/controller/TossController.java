package com.ict.pretzel_admin.ko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.pretzel_admin.ko.service.TossService;
import com.ict.pretzel_admin.ko.vo.TossVO;

@RestController
@RequestMapping("/toss")
public class TossController {

    @Autowired
    private TossService tossService;
    

    // 토스 결제 리스트
    @GetMapping("/toss_list")
    public ResponseEntity<?> tossList() {
        return tossService.tossList();
    }
    

    // 토스 결제 취소
    @PostMapping("/cancel")
    public ResponseEntity<?> tossCancel(@RequestBody TossVO toss) {
        return tossService.tossCancel(toss.getToss_idx(), toss.getCancelReason());
    }
    

}
package com.ict.pretzel_admin.ko.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ict.pretzel_admin.jwt.JwtDecode;
import com.ict.pretzel_admin.ko.service.QuestionManagerService;
import com.ict.pretzel_admin.vo.QuestionVO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@RequestMapping("/question")
public class QuestionManagerController {

    @Autowired
    private QuestionManagerService questionManagerService;
    
    // 1대1문의 목록
    @GetMapping("/quest_list")
    public ResponseEntity<?> quest_list(@RequestParam(value = "cPage", defaultValue = "1") String cPage) {
        return questionManagerService.quest_list(cPage);
    }
    
    // 1대1문의 상세 페이지
    @PostMapping("/quest_detail")
    public ResponseEntity<?> quest_detail(@RequestBody QuestionVO question) {
        return questionManagerService.quest_detail(question.getQuestion_idx());
    }

    // 1대1문의 답변
    @PostMapping("/quest_answer")
    public ResponseEntity<?> quest_answer(@RequestHeader("Authorization") String token, 
                                    @RequestBody QuestionVO question) {
        JwtDecode jwtDecode = new JwtDecode(token); 
        question.setAdmin_id(jwtDecode.getAdmin_id());  
        return questionManagerService.quest_answer(question);
    }
    

}  




    

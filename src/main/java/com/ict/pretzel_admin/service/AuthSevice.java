package com.ict.pretzel_admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ict.pretzel_admin.jwt.JWTUtil;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.DataVO;

@Service
public class AuthSevice {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AdminDetailsService adminDetailsService;
    
    
    public DataVO authenticate(AdminVO admin){
        DataVO dataVO = new DataVO();
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(admin.getAdmin_id(), admin.getPwd()));

            // DB 에서 사용자 정보 가져오기
            admin = adminDetailsService.getAdminDetail(admin.getAdmin_id());

            // 토큰 생성
            String jwt = jwtUtil.generateToken(admin.getAdmin_id());

            // 리턴할 dataVO 에 uvo 와 jwt 를 넣자
            dataVO.setSuccess(true);
            dataVO.setUserDetails(admin);
            dataVO.setToken(jwt);
            return dataVO;
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage(e.getMessage());
            return dataVO;
        }
    }
}

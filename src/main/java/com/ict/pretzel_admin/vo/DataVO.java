package com.ict.pretzel_admin.vo;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 클라이언트에 정보를 여러개 전달해야 할 경우 사용하는 방법
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVO {
    
    // 성공여부를 넣어준다.
    private boolean success;
    // 어떠한 데이터인지 모르므로 Object 사용
    private Object data;
    // 토큰 저장
    private String token;
    // 성공 또는 오류 메시지 전달
    private String message;
    // 사용자 정보 담기
    private UserDetails userDetails;

    /* 사용방법 */
    // DataVO dataVO = new DataVO();
    // dataVO.setSuccess(true);
    // dataVO.setData(List<MemberVO>); 또는 dataVO.setData(MemberVO);
    // dataVO.setToken(token);

    /* 만약에 데이터가 2개(객체가 2개일때) */
    // Map<String, Object> resultMap = new HashMap<>();
    // resultMap.put("AList", List<AVO>);
    // resultMap.put("BList", List<BVO>);
    // dataVO.setData(resultMap);

    

}

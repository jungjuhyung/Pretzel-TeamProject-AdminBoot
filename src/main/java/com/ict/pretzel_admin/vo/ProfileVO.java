package com.ict.pretzel_admin.vo;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import lombok.Data;

@Data
public class ProfileVO {
    private String profile_idx, name, img_name, regdate, user_id, subs, birth, gender;
    private int age;
    private List<String> like_thema;

    // like_thema를 문자열로 저장하기 위한 임시 필드
    private String like_themaAsString;

    // DB 에서 가져온 좋아하는 장르
    public void setLike_themaAsString(String like_themaAsString) {
        this.like_themaAsString = like_themaAsString;
        // 문자열을 리스트로 변환하여 필드에 저장
        if (like_themaAsString != null && !like_themaAsString.isEmpty()) {
            this.like_thema = Arrays.asList(like_themaAsString.split(","));
        } else {
            this.like_thema = null; 
        }
    }
    
    // DB 에 넣을 좋아하는 장르
    public String getLike_themaAsString() {
        if (like_thema == null || like_thema.isEmpty()) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(",");
        for (String thema : like_thema) {
            joiner.add(thema);
        }
        return joiner.toString();
    }

    // birth 설정 시 자동으로 나이를 계산하여 age에 저장
    public void setBirth(String birth) {
        this.birth = birth;
        this.age = calculateAge(birth);
    }

    // 나이 계산
    private int calculateAge(String birth) {
        if (birth == null || birth.isEmpty()) {
            return 0;
        }
        try {
            LocalDate birthDate = LocalDate.parse(birth);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (Exception e) {
            return 0;
        }
    }

}

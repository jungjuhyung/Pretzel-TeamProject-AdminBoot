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

    public void setLike_thema(String like_themaAsString) {
        if (like_themaAsString != null && !like_themaAsString.isEmpty()) {
            this.like_thema = Arrays.asList(like_themaAsString.split(","));
        } else {
            this.like_thema = null; // or initialize with an empty list if preferred
        }
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

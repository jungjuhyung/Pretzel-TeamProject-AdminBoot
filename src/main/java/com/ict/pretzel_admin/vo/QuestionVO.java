package com.ict.pretzel_admin.vo;

import lombok.Data;

@Data
public class QuestionVO {
    private String question_idx, profile_idx, title, content, answer, regdate, ansdate, status, 
                    admin_id, user_id, name;
}

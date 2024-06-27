package com.ict.pretzel_admin.vo;

import lombok.Data;

@Data
public class FaqVO {
    private String faq_idx, title, content, regdate, status, admin_id;
}

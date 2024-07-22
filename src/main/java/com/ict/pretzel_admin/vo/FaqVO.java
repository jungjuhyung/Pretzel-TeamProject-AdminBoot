package com.ict.pretzel_admin.vo;

import lombok.Data;

@Data
public class FaqVO {
    private String faq_idx, title, type, content, regdate, delete_date, status, insert_admin_id, delete_admin_id;
}

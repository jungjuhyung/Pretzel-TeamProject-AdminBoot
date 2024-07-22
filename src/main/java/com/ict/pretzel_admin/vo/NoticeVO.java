package com.ict.pretzel_admin.vo;

import lombok.Data;

@Data
public class NoticeVO {
    private String notice_idx, title, content, regdate, delete_date, status, insert_admin_id, delete_admin_id;
}

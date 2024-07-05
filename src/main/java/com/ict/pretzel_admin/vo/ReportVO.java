package com.ict.pretzel_admin.vo;

import lombok.Data;

@Data
public class ReportVO {
    private String report_idx, profile_idx, type, review_idx, regdate, ansdate, status, 
                    admin_id, admin_name, content;
}

package com.ict.pretzel_admin.vo;

import lombok.Data;

@Data
public class ReviewVO {
    private String review_idx, movie_idx, profile_idx, rating, content, regdate, status, admin_id;

}

package com.ict.pretzel_admin.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class VideoUploadVO {
    private String original_title, trans_title, movie_id, thema, movie_src;
    private MultipartFile video;
}

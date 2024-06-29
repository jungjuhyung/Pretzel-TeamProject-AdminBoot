package com.ict.pretzel_admin.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class VideoUploadVO {
    private String movie_id, tmdb_title, korea_title, english_title, thema,
    synopsis, movie_url, subtitle_url,trailer_url,poster_url,movie_grade,runtime,
    release_year,status,insert_time,update_time,delete_time,admin_id,watch_stack,synchro;
    private MultipartFile video;
    private MultipartFile subtitle;
}

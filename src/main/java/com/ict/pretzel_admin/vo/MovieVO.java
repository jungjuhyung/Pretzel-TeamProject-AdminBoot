package com.ict.pretzel_admin.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MovieVO {
    private String movie_idx, movie_id, tmdb_title, korea_title, english_title, thema,
    synopsis, movie_url, storage_name,trailer_url,poster_url,backdrop_url,movie_grade,runtime,
    ai_deep_syc,ai_emotion_syc,
    release_date,status,insert_time,update_time,delete_time,admin_id,watch_stack,synchro,
    teen_stack,twenty_stack,thirty_stack,forty_stack,fifty_stack,male_stack,female_stack;
    private MultipartFile movie;
    private MultipartFile subtitle;
}

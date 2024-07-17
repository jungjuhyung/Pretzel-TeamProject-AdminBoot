package com.ict.pretzel_admin.vo;

import java.util.Map; // 이기찬 Map import
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MovieVO {
    private String movie_idx, movie_id, tmdb_title, korea_title, english_title, thema,
    synopsis, movie_url, subtitle_url,trailer_url,poster_url,backdrop_url,movie_grade,runtime,
    release_date,status,insert_time,update_time,delete_time,admin_id,watch_stack,synchro,movie_del_name,sub_del_name,
    teen_stack,twenty_stack,thirty_stack,forty_stack,fifty_stack,male_stack,female_stack;
    private MultipartFile movie;
    private MultipartFile subtitle;

    // 이기찬 번역된 자막 파일의 URL을 저장할 필드 추가
    private Map<String, String> translated_subtitles;

    // 이기찬 번역된 자막 파일의 URL을 설정하는 메서드 추가
    public void setTranslated_subtitles(Map<String, String> translated_subtitles) {
        this.translated_subtitles = translated_subtitles;
    }

    // 이기찬 번역된 자막 파일의 URL을 반환하는 메서드 추가
    public Map<String, String> getTranslated_subtitles() {
        return translated_subtitles;
    }
}

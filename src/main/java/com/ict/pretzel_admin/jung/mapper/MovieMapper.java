package com.ict.pretzel_admin.jung.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.pretzel_admin.vo.CastVO;
import com.ict.pretzel_admin.vo.CrewVO;
import com.ict.pretzel_admin.vo.MovieVO;
import java.util.List;

@Mapper
public interface MovieMapper {
    int movie_insert(MovieVO movieVO);
    int cast_insert(CastVO castVO);
    int crew_insert(CrewVO crewVO);
    MovieVO movie_info(String movie_idx);
    int movie_update(MovieVO movieVO);
    int movie_delete(MovieVO movieVO);
    List<MovieVO> movie_list();
    int movie_synchro(String movie_idx);
    List<MovieVO> search_list(String keyword);
    int search_count(String keyword);
    int movie_count();
}
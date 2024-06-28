package com.ict.pretzel_admin.ko.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.vo.QuestionVO;

@Mapper
public interface QuestionManagerMapper {

    /* 1대1 문의 관리 */
    int total_quest();

    List<QuestionVO> quest_list(Paging paging);   
    
    int quest_count();

    QuestionVO question_detail(@Param("question_idx") String question_idx);

    int quest_answer(QuestionVO question);

}

package com.ict.pretzel_admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.common.SubsCountVO;
import com.ict.pretzel_admin.vo.AdminVO;
import com.ict.pretzel_admin.vo.ProfileVO;
import com.ict.pretzel_admin.vo.QuestionVO;
import com.ict.pretzel_admin.vo.ReportVO;
import com.ict.pretzel_admin.vo.ReviewVO;
import com.ict.pretzel_admin.vo.UserVO;

@Mapper
public interface  AdminMapper {

    /* 대쉬보드 */
    AdminVO login(@Param("admin_id") String admin_id);

    List<SubsCountVO> sub_count();

    /* 유저관리 */
    int total_user();
    
    List<UserVO> user_list(Paging paging);
    
    UserVO user_detail(@Param("user_id") String user_id);
    
    List<ProfileVO> profile_list(@Param("user_id") String user_id);

    ProfileVO profile_detail(@Param("profile_idx") String profile_idx);
    
    int user_stop(UserVO user);
    
    int user_recover(UserVO user);
    
    int pwd_reset(UserVO user);

    /* 신고 관리 */
    int total_report();

    List<ReportVO> report_list(Paging paging);

    int report_count();

    ReportVO report_detail(@Param("report_idx") String report_idx);

    ReviewVO reported_review(@Param("review_idx") String review_idx);

    int report_ok(ReportVO report);

    /* 1대1 문의 관리 */
    int total_quest();

    List<QuestionVO> quest_list(Paging paging);   
    
    int quest_count();

    QuestionVO question_detail(@Param("question_idx") String question_idx);

    int quest_answer(QuestionVO question);

    // 관리자 상세
    AdminVO admin_detail(@Param("admin_id") String admin_id);

}

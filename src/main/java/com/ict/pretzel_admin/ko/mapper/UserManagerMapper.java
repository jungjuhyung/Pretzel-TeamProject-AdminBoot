package com.ict.pretzel_admin.ko.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.vo.ProfileVO;
import com.ict.pretzel_admin.vo.UserVO;

@Mapper
public interface UserManagerMapper {

    /* 유저관리 */
    int total_user();
    
    List<UserVO> user_list(Paging paging);
    
    UserVO user_detail(@Param("user_id") String user_id);
    
    List<ProfileVO> profile_list(@Param("user_id") String user_id);

    ProfileVO profile_detail(@Param("profile_idx") String profile_idx);
    
    int user_stop(UserVO user);
    
    int user_recover(UserVO user);
    
    int pwd_reset(UserVO user);
    
}

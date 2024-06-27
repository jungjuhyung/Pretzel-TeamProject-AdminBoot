package com.ict.pretzel_admin.vo;

import lombok.Data;

@Data
public class UserVO {
    private String user_idx, user_id, pwd, name, email, subs, regdate,  
                last_login, status, admin_id;

}

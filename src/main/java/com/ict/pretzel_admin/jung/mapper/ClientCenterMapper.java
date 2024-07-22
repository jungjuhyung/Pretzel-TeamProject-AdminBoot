package com.ict.pretzel_admin.jung.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.vo.FaqVO;
import com.ict.pretzel_admin.vo.NoticeVO;



@Mapper
public interface ClientCenterMapper {
    int notice_count();
    int faq_count();
    List<NoticeVO> notice_list(Paging paging);
    List<FaqVO> faq_list(Paging paging);
    int notice_insert(NoticeVO noticeVO);
    int notice_delete(String delete_admin_id, String notice_idx);
    int faq_insert(FaqVO faqVO);
    int faq_delete(String delete_admin_id, String faq_idx);

}
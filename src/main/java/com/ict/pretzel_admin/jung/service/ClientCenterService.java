package com.ict.pretzel_admin.jung.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.jung.mapper.ClientCenterMapper;
import com.ict.pretzel_admin.vo.FaqVO;
import com.ict.pretzel_admin.vo.NoticeVO;

import java.util.List;

@Service
public class ClientCenterService {

    @Autowired
    private ClientCenterMapper clientCenterMapper;
        
    public int notice_count() {
        int res = clientCenterMapper.notice_count();
        if (res < 0) {
            return 0;
        }
        return res;
	}
    public int faq_count() {
        int res = clientCenterMapper.faq_count();
        if (res < 0) {
            return 0;
        }
        return res;
	}
    public List<NoticeVO> notice_list(Paging paging) {
        List<NoticeVO> result = clientCenterMapper.notice_list(paging);
        if (result != null) {
            return result;
        }
        return null;
	}
    public List<FaqVO> faq_list(Paging paging) {
        List<FaqVO> result = clientCenterMapper.faq_list(paging);
        if (result != null) {
            return result;
        }
        return null;
	}
    public int notice_insert(NoticeVO noticeVO) {
        int res = clientCenterMapper.notice_insert(noticeVO);
        if (res < 0) {
            return 0;
        }
        return res;
	}
    public int notice_delete(String delete_admin_id, String notice_idx) {
        int res = clientCenterMapper.notice_delete(delete_admin_id, notice_idx);
        if (res < 0) {
            return 0;
        }
        return res;
	}
    public int faq_insert(FaqVO faqVO) {
        int res = clientCenterMapper.faq_insert(faqVO);
        if (res < 0) {
            return 0;
        }
        return res;
	}
    public int faq_delete(String delete_admin_id, String faq_idx) {
        int res = clientCenterMapper.faq_delete(delete_admin_id, faq_idx);
        if (res < 0) {
            return 0;
        }
        return res;
	}
}

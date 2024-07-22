package com.ict.pretzel_admin.jung.controller;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.jung.service.ClientCenterService;
import com.ict.pretzel_admin.jwt.JwtDecode;
import com.ict.pretzel_admin.vo.FaqVO;
import com.ict.pretzel_admin.vo.MovieVO;
import com.ict.pretzel_admin.vo.NoticeVO;

import io.jsonwebtoken.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;





@RestController
@RequestMapping("/clientCenter")
public class ClientCenterController {

	@Autowired
    private Paging paging;

	@Autowired
	private ClientCenterService clientCenterService;

	@GetMapping("/notice_list")
	public ResponseEntity<?> notice_list(@RequestParam(value = "cPage", defaultValue = "1") String cPage) throws IOException {
		try {
            // 페이징 기법
            int count = clientCenterService.notice_count();
            paging.setTotalRecord(count);

            if (paging.getTotalRecord() <= paging.getNumPerPage()) {
                paging.setTotalPage(1);
            } else {
                paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
                if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
                    paging.setTotalPage(paging.getTotalPage() + 1);
                }
            }

            paging.setNowPage(Integer.parseInt(cPage));

            paging.setOffset(paging.getNumPerPage() * (paging.getNowPage() - 1));

            paging.setBeginBlock((int) ((paging.getNowPage() - 1) 
                    / paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);

            paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

            if (paging.getEndBlock() > paging.getTotalPage()) {
                paging.setEndBlock(paging.getTotalPage());
            }

            //  DB 갔다오기
            List<NoticeVO> notice_list = clientCenterService.notice_list(paging);

            Map<String, Object> result = new HashMap<>();
            result.put("notice_list", notice_list);
            result.put("paging", paging);
            result.put("count", count);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(0);
        }
	}
	@GetMapping("/faq_list")
	public ResponseEntity<?> faq_list(@RequestParam(value = "cPage", defaultValue = "1") String cPage) throws IOException {
		try {
            // 페이징 기법
            int count = clientCenterService.faq_count();
            paging.setTotalRecord(count);

            if (paging.getTotalRecord() <= paging.getNumPerPage()) {
                paging.setTotalPage(1);
            } else {
                paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
                if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
                    paging.setTotalPage(paging.getTotalPage() + 1);
                }
            }

            paging.setNowPage(Integer.parseInt(cPage));

            paging.setOffset(paging.getNumPerPage() * (paging.getNowPage() - 1));

            paging.setBeginBlock((int) ((paging.getNowPage() - 1) 
                    / paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);

            paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

            if (paging.getEndBlock() > paging.getTotalPage()) {
                paging.setEndBlock(paging.getTotalPage());
            }

            //  DB 갔다오기
            List<FaqVO> faq_list = clientCenterService.faq_list(paging);

            Map<String, Object> result = new HashMap<>();
            result.put("faq_list", faq_list);
            result.put("paging", paging);
            result.put("count", count);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(0);
        }
	}

	@PostMapping("/notice_insert")
	public ResponseEntity<?> notice_insert(@RequestHeader("Authorization") String token, @RequestBody NoticeVO noticeVO) {
		try {
			JwtDecode jwtDecode = new JwtDecode(token);
			noticeVO.setInsert_admin_id(jwtDecode.getAdmin_id());
			int result = clientCenterService.notice_insert(noticeVO);
			return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("0");
		}
	}

	@GetMapping("/notice_delete")
	public ResponseEntity<?> notice_delete(@RequestHeader("Authorization") String token, @RequestParam("notice_idx") String notice_idx) {
		try {
			JwtDecode jwtDecode = new JwtDecode(token);
			String delete_admin_id = jwtDecode.getAdmin_id();
            Map<String, Object> info = new HashMap<>();
            info.put("delete_admin_id", delete_admin_id);
            info.put("notice_idx", notice_idx);
			int result = clientCenterService.notice_delete(info);
			return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("0");
		}
	}
	@PostMapping("/faq_insert")
	public ResponseEntity<?> faq_insert(@RequestHeader("Authorization") String token, @RequestBody FaqVO faqVO) {
		try {
			JwtDecode jwtDecode = new JwtDecode(token);
			faqVO.setInsert_admin_id(jwtDecode.getAdmin_id());
			int result = clientCenterService.faq_insert(faqVO);
			return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("0");
		}
	}

	@GetMapping("/faq_delete")
	public ResponseEntity<?> faq_delete(@RequestHeader("Authorization") String token, @RequestParam("faq_idx") String faq_idx) {
		try {
			JwtDecode jwtDecode = new JwtDecode(token);
			String delete_admin_id = jwtDecode.getAdmin_id();
            Map<String, Object> info = new HashMap<>();
            info.put("delete_admin_id", delete_admin_id);
            info.put("faq_idx", faq_idx);
			int result = clientCenterService.faq_delete(info);
			return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("0");
		}
	}
}

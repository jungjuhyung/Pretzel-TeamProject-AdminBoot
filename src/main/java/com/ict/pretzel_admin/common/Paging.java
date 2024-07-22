package com.ict.pretzel_admin.common;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Paging {
	private String keyword = "";

	private int nowPage = 1;
	private int nowBlock = 1;
	
	// 한 페이지당 20줄
	private int numPerPage = 20;
	// 한 블록당 5개
	private int pagePerBlock = 5;
	// DB의 게시물 수
	private int totalRecord = 0;
	// 게시물의 수를 이용해서 전체 페이지의 수
	private int totalPage = 0;
	private int totalBlock = 0;
	
	// 한번에 가져올 게시물의 시작 번호, 끝 번호
	private int begin = 0;
	private int end = 0;
	
	private int beginBlock = 0;
	private int endBlock = 0;
	private int offset = 0;
	
}

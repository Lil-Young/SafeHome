package com.ssafy.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.board.model.BoardDetailDto;
import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.CommentDto;
import com.ssafy.util.PageNavigation;

public interface BoardService {

	int writeArticle(BoardDto boardDto) throws Exception;
	List<BoardDto> listArticle(Map<String, String> map) throws Exception;
	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	BoardDetailDto getArticle(int articleNo) throws Exception;
	void updateHit(int articleNo) throws Exception;
	void modifyArticle(BoardDto boardDto) throws Exception;
	void deleteArticle(int articleNo) throws Exception;
	int addComment(CommentDto commentDto) throws Exception;
	BoardDto getModifyData(int articleNo) throws Exception;
	
	List<BoardDto> searchAuthor(String term) throws Exception;
	List<BoardDto> searchTitle(String term) throws Exception;
	List<BoardDto> searchAll(String term) throws Exception;
}

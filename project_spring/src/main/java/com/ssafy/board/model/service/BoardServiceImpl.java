package com.ssafy.board.model.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.board.model.BoardDetailDto;
import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.CommentDto;
import com.ssafy.board.model.mapper.BoardMapper;
import com.ssafy.board.model.mapper.CommentMapper;
import com.ssafy.util.PageNavigation;
import com.ssafy.util.SizeConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper boardMapper;
	private final CommentMapper commentMapper;
	public BoardServiceImpl(BoardMapper boardMapper , CommentMapper commentMapper) {
		super();
		this.boardMapper = boardMapper;
		this.commentMapper = commentMapper;
		
	}

	@Override
	@Transactional
	public int writeArticle(BoardDto boardDto) throws Exception {
		boardMapper.writeArticle(boardDto);
		return boardDto.getArticleNo();
	}

	@Override
	public List<BoardDto> listArticle(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		String key = map.get("key");
		if("userid".equals(key))
			key = "b.user_id";
		param.put("key", key == null ? "" : key);
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int pgNo = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
		param.put("start", start);
		param.put("listsize", SizeConstant.LIST_SIZE);

		return boardMapper.listArticle(param);
	}
	
	@Override
	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception {
		PageNavigation pageNavigation = new PageNavigation();

		int naviSize = SizeConstant.NAVIGATION_SIZE;
		int sizePerPage = SizeConstant.LIST_SIZE;
		int currentPage = Integer.parseInt(map.get("pgno"));

		pageNavigation.setCurrentPage(currentPage);
		pageNavigation.setNaviSize(naviSize);
		Map<String, Object> param = new HashMap<String, Object>();
		String key = map.get("key");
		if ("userid".equals(key))
			key = "user_id";
		param.put("key", key == null ? "" : key);
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int totalCount = boardMapper.getTotalArticleCount(param);
		pageNavigation.setTotalCount(totalCount);
		int totalPageCount = (totalCount - 1) / sizePerPage + 1;
		pageNavigation.setTotalPageCount(totalPageCount);
		boolean startRange = currentPage <= naviSize;
		pageNavigation.setStartRange(startRange);
		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
		pageNavigation.setEndRange(endRange);
		pageNavigation.makeNavigator();

		return pageNavigation;
	}

	@Override
	public BoardDetailDto getArticle(int articleNo) throws Exception {
		return boardMapper.getArticle(articleNo);
	}

	@Override
	public void updateHit(int articleNo) throws Exception {
		boardMapper.updateHit(articleNo);
	}

	@Override
	public void modifyArticle(BoardDto boardDto) throws Exception {
		// TODO : BoardDaoImpl의 modifyArticle 호출
		boardMapper.modifyArticle(boardDto);
	}

	@Override
	@Transactional
	public void deleteArticle(int articleNo) throws Exception {
		// TODO : BoardDaoImpl의 deleteArticle 호출
		boardMapper.deleteArticle(articleNo);
	}

	@Override
	public int addComment(CommentDto commentDto) throws Exception {
		
		return commentMapper.addComment(commentDto);
	}

	@Override
	public BoardDto getModifyData(int articleNo) throws Exception {
		return boardMapper.getModifyData(articleNo);
	}

	@Override
	public List<BoardDto> searchAuthor(String term) throws Exception {
		// TODO Auto-generated method stub
		
		return boardMapper.searchAuthor(term);
	}

	@Override
	public List<BoardDto> searchTitle(String term) throws Exception {
		// TODO Auto-generated method stub
		return boardMapper.searchTitle(term);
	}

	@Override
	public List<BoardDto> searchAll(String term) throws Exception {
		// TODO Auto-generated method stub
		return boardMapper.searchAll(term);
	}

}

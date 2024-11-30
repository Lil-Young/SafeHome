package com.ssafy.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.board.model.CommentDto;

public interface CommentService {
	void writeComment(CommentDto commentDto) throws Exception;
	List<CommentDto> listComment(Map<String, String> map) throws Exception;
	void deleteComment(int commentId) throws Exception;
	List<CommentDto> listReply(int commentId) throws Exception;
}

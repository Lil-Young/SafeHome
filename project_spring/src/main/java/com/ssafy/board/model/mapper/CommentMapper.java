package com.ssafy.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.board.model.CommentDto;
@Mapper
public interface CommentMapper {
	void writeComment(CommentDto commentDto) throws Exception;
	List<CommentDto> listComment(Map<String, String> map) throws Exception;
	void modifyComment(CommentDto commentDto) throws Exception;
	void deleteComment(int commentId) throws Exception;
	List<CommentDto> listReply(int commentId);
	int addComment(CommentDto commentDto);
}

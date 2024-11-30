package com.ssafy.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.board.model.CommentDto;
import com.ssafy.board.model.mapper.CommentMapper;

public class CommentServiceImpl implements CommentService{
	
	private final CommentMapper commentMapper;
	
	public CommentServiceImpl(CommentMapper commentMapper) {
		super();
		this.commentMapper = commentMapper;
	}

	@Override
	public void writeComment(CommentDto commentDto) throws Exception {
		// TODO Auto-generated method stub
		commentMapper.writeComment(commentDto);
	}

	@Override
	public List<CommentDto> listComment(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub	
		return commentMapper.listComment(map);
	}

	@Override
	public void deleteComment(int commentId) throws Exception {
		// TODO Auto-generated method stub
		commentMapper.deleteComment(commentId);
	}
	
    @Override
    public List<CommentDto> listReply(int commentId) throws Exception {
        return commentMapper.listReply(commentId);
    }
}

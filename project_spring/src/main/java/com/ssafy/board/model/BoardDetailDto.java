package com.ssafy.board.model;

import java.util.*;

public class BoardDetailDto {
	private int articleNo;
	private String boardUserId;
	private String boardSubject;
	private String content;
	private int hit;
	private String boardRegisterTime;
	List<ReplyDto> comments;

	public String getBoardSubject() {
		return boardSubject;
	}

	public void setBoardSubject(String boardSubject) {
		this.boardSubject = boardSubject;
	}

	public int getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(int articleNo) {
		this.articleNo = articleNo;
	}

	public String getBoardUserId() {
		return boardUserId;
	}

	public void setBoardUserId(String boardUserId) {
		this.boardUserId = boardUserId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getBoardRegisterTime() {
		return boardRegisterTime;
	}

	public void setBoardRegisterTime(String boardRegisterTime) {
		this.boardRegisterTime = boardRegisterTime;
	}

	public List<ReplyDto> getComments() {
		return comments;
	}

	public void setComments(List<ReplyDto> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "BoardDetailDto [articleNo=" + articleNo + ", boardUserId=" + boardUserId + ", content=" + content
				+ ", hit=" + hit + ", boardRegisterTime=" + boardRegisterTime + ", comments=" + comments + "]"
				+ comments.size();
	}

}

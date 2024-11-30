package com.ssafy.board.model;

import java.util.*;

public class ReplyDto {
	private int commentId;
	private String commentUserId;
	private String commentContent;
	private String commentRegisterTime;
	private List<ReplyDto> childComments;
	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentRegisterTime() {
		return commentRegisterTime;
	}
	public void setCommentRegisterTime(String commentRegisterTime) {
		this.commentRegisterTime = commentRegisterTime;
	}
	public List<ReplyDto> getChildComments() {
		return childComments;
	}
	public void setChildComments(List<ReplyDto> childComments) {
		this.childComments = childComments;
	}
	
	@Override
	public String toString() {
		return "ReplyDto [commentId=" + commentId + ", commentUserId=" + commentUserId + ", commentContent="
				+ commentContent + ", commentRegisterTime=" + commentRegisterTime + ", childComments=" + childComments
				+ "]";
	}	
	
}

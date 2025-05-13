package com.example.messageboad.dto;

import lombok.Data;

@Data
public class MessageBoardDetailDto {
	private int postId;
	private String content;
	private String postBy;
	private String createdAt;
	private int likeCount;
    private boolean alreadyLiked;
}

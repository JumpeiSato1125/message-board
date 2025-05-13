package com.example.messageboad.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageBoardDto implements Serializable{
	
	private int id;
	private String title;
	private String thumbnailPath;
	private String description;
	private String createdAt;
	private int viewCount;
	private int likeAllCount;
	private int categoryId;
	private String categoryName;
}

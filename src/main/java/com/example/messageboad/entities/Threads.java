package com.example.messageboad.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Threads {
	
	@Id
	@Column
	private int id;
	@Column(length = 50, nullable = false)
	private String title;
	@Column(length = 512)
	private String thumbnailPath;
	@Column
	private int categoryId;
	@Column
	private String description;
	@Column(length = 255, nullable = false)
	private String createdBy;
	@Column(nullable = false)
	private LocalDateTime createdAt;
	@Column
	private int postCount;
	@Column
	private int likeAllCount;
	@Column(nullable = false)
	private int deleted;
	
	public int getId() {
		return id;
	}
	public void setId(int thread_id) {
		this.id = thread_id;;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public int getLikeAllCount() {
		return likeAllCount;
	}
	public void setLikeAllCount(int likeAllCount) {
		this.likeAllCount = likeAllCount;
	}
	public int getViewCount() {
		return postCount;
	}
	public void setViewCount(int viewCount) {
		this.postCount = viewCount;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
}
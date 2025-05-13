package com.example.messageboad.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.messageboad.entities.Posts;

@Repository
public interface PostsDao extends JpaRepository<Posts, Integer>{
	
	Page<Posts> findByThreadIdAndDeleteFlg(int threadId, int deleteFlg, PageRequest pageRequest);
	
	@Modifying
	@Query("UPDATE Posts p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
	int incrementLikeCount(@Param("postId") int postId);
}

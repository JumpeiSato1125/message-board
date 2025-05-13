package com.example.messageboad.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.messageboad.entities.Likes;

@Repository
public interface LikesDao extends JpaRepository<Likes, Integer>{ 
	
	boolean existsByPostIdAndUserId(int postId, String userId);
}

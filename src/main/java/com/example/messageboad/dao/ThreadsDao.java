package com.example.messageboad.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.messageboad.entities.Threads;

@Repository
public interface ThreadsDao extends JpaRepository<Threads, Integer>{
	
	Optional<Threads> findByIdAndDeleted(int id, int deleted);
	
	Page<Threads> findByDeleted(int deleted, PageRequest pageRequest);
	
	Page<Threads> findByTitleContainingIgnoreCaseAndDeleted(String title, int deleted, PageRequest pageRequest);
	
	Page<Threads> findByCategoryIdAndDeleted(int categoryId, int deleted, PageRequest pageRequest);

	List<Threads> findTop6ByDeletedOrderByCreatedAtDesc(int deleted);
	
	List<Threads> findTop10ByDeletedOrderByPostCountDesc(int deleted);
	
	List<Threads> findTop10ByDeletedOrderByLikeAllCountDesc(int deleted);
	
	@Modifying
	@Query("UPDATE Threads t SET t.likeAllCount = t.likeAllCount + 1 WHERE t.id = :threadId")
	int incrementLikeCount(@Param("threadId") int threadId);
	
	@Modifying
	@Query("UPDATE Threads t SET t.postCount = t.postCount + 1 WHERE t.id = :threadId")
	int incrementPostCount(@Param("threadId") int threadId);
	
}
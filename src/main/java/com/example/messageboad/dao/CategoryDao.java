package com.example.messageboad.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.messageboad.entities.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer>{
	List<Category> findByDeleted(int deleted);
}

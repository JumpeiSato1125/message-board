package com.example.messageboad.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.messageboad.entities.Account;

@Repository
public interface AccountDao extends JpaRepository<Account, String>{
	
	 Optional<Account> findById(String id);

}

package com.example.messageboad.service;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.messageboad.beans.LoginBean;
import com.example.messageboad.dao.AccountDao;
import com.example.messageboad.entities.Account;

@Service
public class AccountService {

	private final AccountDao accountDao;
	
    @Autowired
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
	
	public LoginBean checkPassword(String id,String password) {
		LoginBean lb = new LoginBean();
		Account ac = accountDao.findById(id).orElse(null);
		if (ac != null && BCrypt.checkpw(password, ac.getPassword())){
			lb.setId(ac.getId());
            return lb;
        } else {
           return null;
        }
	}
}

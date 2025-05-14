package com.example.messageboad.beans;

import java.util.ArrayList;

import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LoginBean {
	@NotEmpty(message = "idを入力してください")
	private String id;
	@NotEmpty(message = "パスワードを入力してください")
	private String password;
	private ArrayList<String> errorList;
	
	public LoginBean() {
		errorList = new ArrayList<String>();
	}
}

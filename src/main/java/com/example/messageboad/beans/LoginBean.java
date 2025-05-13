package com.example.messageboad.beans;

import java.util.ArrayList;

import lombok.Data;

@Data
public class LoginBean {
	private String id;
	private String password;
	private ArrayList<String> errorList;
	
	public LoginBean() {
		errorList = new ArrayList<String>();
	}
}

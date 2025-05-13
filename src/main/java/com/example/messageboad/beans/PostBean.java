package com.example.messageboad.beans;

import java.util.ArrayList;

import lombok.Data;
@Data
public class PostBean {
	private String content; 
	private int threadId;
	private int page;
	ArrayList<String> errorList;
}

package com.example.messageboad.beans;

import java.util.ArrayList;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateThreadBean {
	@NotEmpty(message = "タイトルを入力してください")
	@Size(max = 50, message="タイトルは50文字までで入力してください")
	private String title;
	@NotEmpty(message = "説明を入力してください")
	@Size(max = 255, message="説明は255文字までで入力してください")
    private String description;
	@NotNull(message = "カテゴリを選択してください")
    private int categoryId;
    private MultipartFile file;
    private ArrayList<String> errorList;
}

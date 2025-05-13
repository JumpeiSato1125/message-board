package com.example.messageboad.controller;

import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.messageboad.beans.LoginBean;
import com.example.messageboad.service.AccountService;

@Controller
public class LoginController {
	
	@Autowired
	AccountService accountService;

	@GetMapping("/login")
	public ModelAndView login(ModelAndView mav) {
		mav.addObject("loginBean", new LoginBean());
		mav.setViewName("login");
		return mav;
	}

	@PostMapping("/login")
	public ModelAndView form(@ModelAttribute LoginBean loginBean, HttpSession session, ModelAndView mav) {
		
		// 変数宣言
		String id = loginBean.getId();
		String password = loginBean.getPassword();
		
		// エラーチェック
		loginBean.setErrorList(errorCheck(id, password));
		if(!loginBean.getErrorList().isEmpty()) {
			// エラー画面に遷移
			mav.addObject("loginBean", loginBean);
			mav.setViewName("login");
			return mav;
		}
		
		// パスワードチェック
		LoginBean lb = accountService.checkPassword(id, password);
		if(lb == null) {
			// エラー画面に遷移
			ArrayList<String> erorrList = new ArrayList<String>();
			erorrList.add("パスワードが照合できませんでした。");
			loginBean.setErrorList(erorrList);
			mav.addObject("loginBean", loginBean);
			mav.setViewName("login");
			return mav;
		} else {
			// セッションに登録して遷移
			lb.setPassword(null);
			session.setAttribute("lb", lb);
			
		}
		mav.setViewName("redirect:/messageBoard/index");
		return mav;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/login";
	}
	
	public ArrayList<String> errorCheck(String id, String password){
		ArrayList<String> list = new ArrayList<String>();
		if(id == null || id.isEmpty()) {
			list.add("idを入力してください。");
		}
		if(!chekAlphaNumeral(id)) {
			list.add("idは半角英数値を入力してください");
		};
		if(password == null || password.isEmpty()) {
			list.add("passwordを入力してください。");
		}
		if(!chekAlphaNumeral(password)) {
			list.add("パスワードは半角英数値を入力してください");
		};
		return list;
	}
	
	public boolean chekAlphaNumeral(String s) {
		if(s == null || s.isEmpty()) return false;
		return s.matches("^[A-Za-z0-9]+$");
	}
}

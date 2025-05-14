package com.example.messageboad.controller;

import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.messageboad.beans.LoginBean;
import com.example.messageboad.service.AccountService;

@Controller
public class LoginController {

	@Autowired
	AccountService accountService;

	@GetMapping("/login")
	public String login(Model model) {
		if (!model.containsAttribute("loginBean")) {
			LoginBean loginBean = new LoginBean();
	        model.addAttribute("loginBean", loginBean);
	    }
		return "login";
	}

	@PostMapping("/login")
	public ModelAndView form(@Valid @ModelAttribute LoginBean loginBean, BindingResult bindingResult,
			HttpSession session, ModelAndView mav,RedirectAttributes redirectAttributes) {

		// エラーチェック
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("loginBean", loginBean);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginBean",
					bindingResult);
			mav.setViewName("redirect:/login");
			return mav;
		}

		// 変数宣言
		String id = loginBean.getId();
		String password = loginBean.getPassword();


		// パスワードチェック
		LoginBean lb = accountService.checkPassword(id, password);
		if (lb == null) {
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
}

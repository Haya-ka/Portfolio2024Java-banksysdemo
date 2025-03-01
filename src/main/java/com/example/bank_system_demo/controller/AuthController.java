package com.example.bank_system_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bank_system_demo.model.User;
import com.example.bank_system_demo.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
public class AuthController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
		if (userService.authenticate(email, password)) {
			//メールアドレスでユーザーを取得
			User user = userService.getUserByEmail(email).orElse(null);
			//セッションにユーザー情報を格納
			session.setAttribute("user", user);
			//認証成功後リダイレクト
			return "redirect:/home";
		}else {
			model.addAttribute("error", "Invalid email or password.");
			// 認証失敗時に再度ログインページを表示
			return "login";
		}
	}
}

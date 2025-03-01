package com.example.bank_system_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bank_system_demo.model.Admin;
import com.example.bank_system_demo.service.AccountService;
import com.example.bank_system_demo.service.AdminService;
import com.example.bank_system_demo.service.PasswordHasher;
import com.example.bank_system_demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	@Autowired
	private AdminService adminService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;
	
	@GetMapping("/admin/login")
	public String showAdminLogin() {
		return "admin/login";
		}
	
	@PostMapping("/admin/login")
	public String processAdminLogin(@RequestParam("email") String email, 
			@RequestParam("password") String password,
			HttpSession session) {
		
		Admin admin = adminService.findByEmail(email);
		
		if (admin != null && admin.getPassword().equals(PasswordHasher.hashPassword(password))) {
			session.setAttribute("admin", admin);
			return "redirect:/admin/dashboard";
		}else {
			return "redirect:/admin/login?error";
		}
	}
	
	@GetMapping("/admin/dashboard")
	public String showAdminDashboard(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) {
			return "redirect:/admin/login";
		}
		model.addAttribute("adminEmail", admin.getEmail());
		return "admin/dashboard";
	}
	
	@GetMapping("/admin/addUser")
	public String showAddUserPage() {
		return "admin/addUser";
	}
	
	@PostMapping("/admin/addUser")
	public String addUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			RedirectAttributes redirectAttributes) {
		
		boolean success = userService.createUser(name, email, password);
		
		if (success) {
			redirectAttributes.addFlashAttribute("successMessage", "ユーザーを追加しました！");
		}else {
			redirectAttributes.addFlashAttribute("errorMessage", "エラー: メールアドレスが既に登録されています。");
		}
		return "redirect:/admin/addUser";
	}
	
	@GetMapping("/admin/createAccount")
	public String showCreateAccountPage(Model model) {
		return "admin/createAccount";
	}
	
	@PostMapping("/admin/createAccount")
	public String createAccount(@RequestParam("userId") int userId,
			@RequestParam("initialBalance") Double initialBalance,
			RedirectAttributes redirectAttributes) {
		
		try{
			accountService.createAccount(userId, initialBalance);
			redirectAttributes.addFlashAttribute("successMessage", "口座を作成しました！");
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "エラー: " + e.getMessage());
		}
		return "redirect:/admin/createAccount";
	}
	
	@PostMapping("/admin/logout")
	public String adminLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}
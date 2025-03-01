package com.example.bank_system_demo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bank_system_demo.model.Account;
import com.example.bank_system_demo.model.DepositForm;
import com.example.bank_system_demo.model.User;
import com.example.bank_system_demo.repository.AccountRepository;
import com.example.bank_system_demo.service.AccountService;
import com.example.bank_system_demo.service.TransactionService;
import com.example.bank_system_demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
    private AccountRepository accountRepository;
	@Autowired
	private TransactionService transactionService;
	
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		//セッションからユーザー情報を取得
		User user = (User) session.getAttribute("user");
		if(user != null) {
			//ユーザー名をモデルに追加しhome.thmlを表示
			model.addAttribute("name", user.getName());
			return "home";
		//ユーザーがセッションにいない場合はログインページにリダイレクト
		}else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/checkBalance")
	public String checkBalance(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		//ユーザーの口座残高を取得
		BigDecimal balance = userService.getUserBalance(user.getId());
		//モデルにユーザー名と残高を追加
		model.addAttribute("name", user.getName());
		model.addAttribute("balance", balance);
		return "checkBalance";
	}
	
	@GetMapping("/deposit")
	public String showDepositPage(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("name", user.getName());
		//フォーム用のmodel作成
		model.addAttribute("depositForm", new DepositForm());
		return "deposit";
	}
	
	@PostMapping("/deposit")
	public String deposit(@RequestParam("amount") BigDecimal amount, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		//入金処理を行う
		accountService.deposit(user.getId(), amount);
		System.out.println("入金に成功しました。");
		return "redirect:/home";
	}
	
	@GetMapping("/withdraw")
	public String withdraw(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("name", user.getName());
		return "withdraw";
	}
	
	@PostMapping("/withdraw")
	public String withdraw(@RequestParam("amount") BigDecimal amount, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		//出金処理を行う
		boolean success = accountService.withdraw(user.getId(), amount);
		if(success) {
			System.out.println("出金に成功しました。");
			return "redirect:/home";
		}else {
			System.out.println("出金に失敗しました。");
			return "redirect:/home";
		}
	}
	
	@GetMapping("/transfer")
	public String showTransferPage(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		//ユーザーの口座リストを取得
	    List<Account> accounts = accountRepository.findByUserId(user.getId());
		model.addAttribute("name", user.getName());
	    model.addAttribute("accounts", accounts);
		return "transfer";
	}
	
	@PostMapping("/transfer")
	public String processTransfer(@RequestParam("senderAccountId") int senderAccountId,
			@RequestParam("recipientEmail") String recipientEmail,
			@RequestParam("amount") BigDecimal amount,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/login";
		}
		
		//送金処理
		boolean success = transactionService.transfer(senderAccountId, recipientEmail, amount);
		if (success) {
			redirectAttributes.addFlashAttribute("successMessage", "送金が完了しました！");
			return "redirect:/home";
		}else {
			redirectAttributes.addFlashAttribute("errorMessage", "送金に失敗しました。");
			return "redirect:/transfer";
		}
	}
	
	@GetMapping("/createAccount")
	public String createAccount() {
		return "createAccount";
	}
	
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		//セッションをクリア
		session.invalidate();
		return "redirect:/login";
	}
}

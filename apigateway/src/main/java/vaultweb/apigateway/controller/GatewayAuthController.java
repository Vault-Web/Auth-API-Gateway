package vaultweb.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class GatewayAuthController {

	@GetMapping("/me")
	public String getMyData(){
		return "My Data!";
	}

	@PostMapping("/login")
	public String login(){
		return "login";
	}

	@PostMapping("/register")
	public String register(){
		return "register";

	}

	@PostMapping("/logout")
	public String logout(){
		return "logout";
	}

	@PostMapping("change-username")
	public String changeUsername(){
		return "changeUsername";
	}

	@PostMapping("change-email")
	public String changeEmail(){
		return "changeEmail";
	}

	@PostMapping("change-password")
	public String changePassword(){
		return "changePassword";
	}

	@PostMapping("/switch-jwt")
	public String switchJwtToken(){
		return "switchJwtToken";
	}

	@PostMapping("/reset-password")
	public String resetPassword(){
		return "resetPassword";
	}

	@PostMapping("/verify-email")
	public String verifyEmail(){
		return "verifyEmail";
	}

}

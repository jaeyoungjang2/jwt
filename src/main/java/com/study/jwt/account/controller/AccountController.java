package com.study.jwt.account.controller;

import com.study.jwt.account.dto.LoginReqDto;
import com.study.jwt.account.dto.AccountReqDto;

import com.study.jwt.dto.GlobalResDto;
import com.study.jwt.jwt.util.JwtUtil;
import com.study.jwt.security.user.UserDetailsImpl;
import com.study.jwt.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

	private final JwtUtil jwtUtil;
	private final AccountService accountService;

	@PostMapping("/account/signup")
	public GlobalResDto signup(@RequestBody @Valid AccountReqDto accountReqDto) {
		return accountService.signup(accountReqDto);
	}

	@PostMapping("/account/login")
	public GlobalResDto login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
		return accountService.login(loginReqDto, response);
	}

	// accesstoken이 만료되었을 때 refreshtoken이 발행되는 부분입니다.
	@GetMapping("/issue/token")
	public GlobalResDto issuedToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){
		response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getAccount().getEmail(), "Access"));
		return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
	}

}

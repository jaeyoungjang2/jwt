package com.study.jwt.service;

import com.study.jwt.account.dto.LoginReqDto;
import com.study.jwt.account.entity.Account;
import com.study.jwt.account.entity.RefreshToken;
import com.study.jwt.account.repository.AccountRepository;
import com.study.jwt.account.dto.AccountReqDto;
import com.study.jwt.dto.GlobalResDto;
import com.study.jwt.jwt.dto.TokenDto;
import com.study.jwt.jwt.util.JwtUtil;
import com.study.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public GlobalResDto signup(AccountReqDto accountReqDto) {
		// email 중복 검사
		if(accountRepository.findByEmail(accountReqDto.getEmail()).isPresent()){
			throw new RuntimeException("Overlap Check");
		}

		accountReqDto.setEncodePwd(passwordEncoder.encode(accountReqDto.getPassword()));
		Account account = new Account(accountReqDto);

		accountRepository.save(account);
		return new GlobalResDto("Success signup", HttpStatus.OK.value());
	}

	@Transactional
	public GlobalResDto login(LoginReqDto loginReqDto, HttpServletResponse response) {

		Account account = accountRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(
				() -> new RuntimeException("Not found Account")
		);

		if(!passwordEncoder.matches(loginReqDto.getPassword(), account.getPassword())) {
			throw new RuntimeException("Not matches Password");
		}

		TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getEmail());

		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(loginReqDto.getEmail());

		if(refreshToken.isPresent()) {
			refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
		}else {
			RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getEmail());
			refreshTokenRepository.save(newToken);
		}

		setHeader(response, tokenDto);

		return new GlobalResDto("Success Login", HttpStatus.OK.value());

	}

	private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
		response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
		response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
	}
}

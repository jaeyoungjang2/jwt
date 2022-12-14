package com.study.jwt.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.jwt.dto.GlobalResDto;
import com.study.jwt.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	// HTTP 요청이 오면 WAS(tomcat)가 HttpServletRequest, HttpServletResponse 객체를 만들어 줍니다.
	// 만든 인자 값을 받아옵니다.
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// WebSecurityConfig 에서 보았던 UsernamePasswordAuthenticationFilter 보다 먼저 동작을 하게 됩니다.

		// 헤더에서 토큰을 가져옵니다.
		String accessToken = jwtUtil.getHeaderToken(request, "Access");
		String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

		// accessToken 검증
		if(accessToken != null) {
			if(!jwtUtil.tokenValidation(accessToken)){
				jwtExceptionHandler(response, "AccessToken Expired", HttpStatus.BAD_REQUEST);
				return;
			}
			setAuthentication(jwtUtil.getEmailFromToken(accessToken));
		// refereshToken 검증
		}else if(refreshToken != null) {
			if(!jwtUtil.refreshTokenValidation(refreshToken)){
				jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
				return;
			}
			setAuthentication(jwtUtil.getEmailFromToken(refreshToken));
		}

		// customizing filter가 끝나고, 원래 진행해야 하는 filter가 추가적으로 진행할 수 있도록 함
		filterChain.doFilter(request,response);
	}

	private void setAuthentication(String email) {
		Authentication authentication = jwtUtil.createAuthentication(email);
		// security가 만들어주는 securityContextHolder 그 안에 authentication을 넣어줍니다.
		// security가 securitycontextholder에서 인증 객체를 확인하는데
		// jwtAuthfilter에서 authentication을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않습니다.
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
		response.setStatus(status.value());
		response.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new GlobalResDto(msg, status.value()));
			response.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}

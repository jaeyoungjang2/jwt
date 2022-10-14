package com.study.jwt.jwt.filter;

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

		// 위, 변조 검증
		// expired date 확인
		// 토큰 만들어주고
		// 토큰에서 값을 가져올 수 있고


		// 헤더에서 토큰을 가져옵니다.
		String accessToken = jwtUtil.getHeaderToken(request, "Access");
		String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

		if(accessToken != null) {
			if(!jwtUtil.tokenValidation(accessToken)){
				jwtExceptionHandler(response, "AccessToken Expired", HttpStatus.BAD_REQUEST);
				return;
			}
			setAuthentication(jwtUtil.getEmailFromToken(accessToken));
		}else if(refreshToken != null) {
			if(!jwtUtil.refreshTokenValidation(refreshToken)){
				jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
				return;
			}
			setAuthentication(jwtUtil.getEmailFromToken(refreshToken));
		}

		filterChain.doFilter(request,response);
	}

	public void setAuthentication(String email) {
		Authentication authentication = jwtUtil.createAuthentication(email);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
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

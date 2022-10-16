package com.study.jwt.security;

import com.study.jwt.jwt.filter.JwtAuthFilter;
import com.study.jwt.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 설정 파일을 등록하기 위함입니다.
@Configuration
// security를 사용하겠습니다.
@EnableWebSecurity
// 매개변수가 있는 생성자를 만들겠습니다.
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtUtil jwtUtil;

	@Bean
	// password를 암호화 하기 위해 사용할 예정입니다.
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 공식 문서를 참고해 봅시다.
	// https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
	// securityFilterChain 보다 먼저 실행이 됩니다.
	// h2-console, swagger 설정을 이곳에서 사용합니다.
	// 인증과 상관이 없는 부분을 설정합니다.
	@Bean
	public WebSecurityCustomizer ignorigCustomizer() {
		return (web) -> web.ignoring().antMatchers("/h2-console/**");
	}

	// security는 session 방식을 사용합니다.
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors();
		http.csrf().disable();

		// jwt 방식을 사용하기 위해서 옵션을 추가합니다.
		// spring security 세션 정책에 관한 설정입니다.
		// SessionCreationPolicy.ALWAYS      - 스프링시큐리티가 항상 세션을 생성
		// SessionCreationPolicy.IF_REQUIRED - 스프링시큐리티가 필요시 생성(기본)
		// SessionCreationPolicy.NEVER       - 스프링시큐리티가 생성하지않지만, 기존에 존재하면 사용
		// SessionCreationPolicy.STATELESS   - 스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


		http.authorizeRequests().antMatchers("/api/account/**").permitAll()
				.anyRequest().authenticated()
				.and()
				// filter와 intercepter의 개념이 있습니다.
				// customizing한 filter를
				// addFilterBefore -> 인자 순서대로 filter를 수행하게 됩니다.
				// JwtAuthFilter는 직접 만든 filter 입니다.
				// UsernamePasswordAuthenticationFilter -> security 자체가 username과 password로 인증 인가를 하는 것입니다
				.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}
}

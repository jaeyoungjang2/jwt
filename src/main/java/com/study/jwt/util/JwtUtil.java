package com.study.jwt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
// bean으로 등록을 합니다.
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private final UserDetailsServiceImpl userDetailsService;
	private final RefreshTokenRepository refreshTokenRepository;

	private static final long ACCESS_TIME = 10 * 1000L;
	private static final long REFRESH_TIME = 60 * 1000L;
	public static final String ACCESS_TOKEN = "Access_Token";
	public static final String REFRESH_TOKEN = "Refresh_Token";

	// application.properties에서 설정한 값을 가지고 옵니다.
	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	// bean으로 등록 되면서 딱 한번 실행이 됩니다.
	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	// header 토큰을 가져오는 기능
	public String getHeaderToken(HttpServletRequest request, String type) {
		return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) :request.getHeader(REFRESH_TOKEN);
	}

	// 토큰 생성
	public TokenDto createAllToken(String email) {
		return new TokenDto(createToken(email, "Access"), createToken(email, "Refresh"));
	}

	public String createToken(String email, String type) {

		Date date = new Date();

		// Access token과 Refresh token의 expired time이 다릅니다. 아래 조건문을 통해 expired time을 들고와서 jwt token을 만들어주는데 사용합니다.
		long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

		// jwt token을 실제로 만들어 주는 부분입니다.
		return Jwts.builder()
				.setSubject(email)
				.setExpiration(new Date(date.getTime() + time))
				.setIssuedAt(date)
				.signWith(key, signatureAlgorithm)
				.compact();

	}

	// 토큰 검증
	public Boolean tokenValidation(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}

	// refreshToken 토큰 검증
	public Boolean refreshTokenValidation(String token) {

		// 1차 토큰 검증
		if(!tokenValidation(token)) return false;

		// DB에 저장한 토큰 비교
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(getEmailFromToken(token));

		return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
	}

	// 인증 객체 생성
	public Authentication createAuthentication(String email) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// 토큰에서 email 가져오는 기능
	public String getEmailFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

}

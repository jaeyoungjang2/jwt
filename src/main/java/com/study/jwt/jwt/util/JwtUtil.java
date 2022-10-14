package com.study.jwt.jwt.util;

import com.study.jwt.account.entity.RefreshToken;
import com.study.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

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
				// 현재 시간 + expired time -> token의 만료기한을 설정하는 것입니다.
				.setExpiration(new Date(date.getTime() + time))
				.setIssuedAt(date)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

	// 토큰 검증
	public Boolean tokenValidation(String token) {
		try {
			// jwt에서 토큰을 검증하는 기능을 미리 만들어 놓았습니다.
			// 분해하기 위해서 만들때 사용했던 key값을 넣어줍니다.
			// jwt에 들어가 있는 정보는 누구나 볼 수 있지만, key값을 모르는한 위 변조를 할 수 없습니다.
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
			// jwt에서 문제가 발생할 시에는 exception이 처리됩니다.
			// 현재는 Exception 전체를 가지고 와서 처리를 하지만
			// ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException 등등 case 마다 처리하는 것이 더 좋습니다.
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}

	// refreshToken 토큰 검증
	// db에 저장되어 있는 token과 비교
	// db에 저장한다는 것이 jwt token을 사용한다는 강점을 상쇄시킨다.
	// redis를 사용하는 것이 더욱 좋다. (in-memory db기 때문에 조회속도가 빠르고 주기적으로 삭제하는 기능이 기본적으로 존재합니다.)
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
		// jwt토큰을 생성할 때 email을 subject안에 넣어뒀기 때문에, getSubject를 이용하여 토큰안에 있는 email을 들고옵니다.
		// email을 들고 오는 step은 token 검증을 완료하고 난 다음 진행하기 때문에, exception은 발생하지 않을 것입니다.
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

}

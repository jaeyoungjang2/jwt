package com.study.jwt.security.user;

import com.study.jwt.account.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// UserDetails는 security에서 지원하는 것입니다.
// 사용해야 하는 필수 목록은 아니지만, 컨트롤러에서 user 객체를 쉽게 들고올 수 있는 등 많은 장점을 가지고 있기 때문에 자주 사용하는 편입니다.
public class UserDetailsImpl implements UserDetails {
	private Account account;

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}

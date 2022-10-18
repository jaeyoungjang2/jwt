package com.study.jwt.account.entity;

import com.study.jwt.account.dto.AccountReqDto;
import com.study.jwt.like.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String phoneNumber;

	@OneToMany(mappedBy = "account")
	private List<Like> likes;

	public Account(AccountReqDto accountReqDto) {
		this.email = accountReqDto.getEmail();
		this.password = accountReqDto.getPassword();
		this.phoneNumber = accountReqDto.getPhoneNumber();
	}

}

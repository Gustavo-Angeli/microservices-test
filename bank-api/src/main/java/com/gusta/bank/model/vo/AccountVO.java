package com.gusta.bank.model.vo;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountVO extends RepresentationModel<AccountVO> {
	private String username;
	private String password;
	private Double accountBalance;
}

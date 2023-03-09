package com.gusta.bank.model.entity;


import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "username", nullable = false, length = 20)
	private String username;
	private Double accountBalance;
}

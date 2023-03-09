package com.gusta.bank.controllers;

import com.gusta.bank.model.vo.*;
import com.gusta.bank.proxy.*;
import com.gusta.bank.services.AccountServices;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.gusta.bank.utils.ParamValidation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank/v1")
public class AccountController {

	private final AccountServices service;

	@PostMapping(value = "/create")
	public ResponseEntity<AccountVO> create(@RequestBody AccountVO accountVO){
		checkIfIsNullOrBlankThrowingEx(
				accountVO.getUsername(),
				accountVO.getPassword()
		);
		return new ResponseEntity<>(service.createAccount(accountVO), HttpStatus.CREATED);
	}

	@PutMapping(value = "/deposit")
	public ResponseEntity deposit (@RequestBody DepositVO deposit) {
		checkIfIsNullOrBlankThrowingEx(
				deposit.getDepositTo(),
				deposit.getValue()
		);
		return new ResponseEntity<>(service.deposit(deposit), HttpStatus.OK);
	}

	@PutMapping(value = "/transfer")
	public ResponseEntity transfer(
			@RequestBody TransferVO transfer,
			@RequestHeader(name = "Authorization") String token
	) {
		checkIfIsNullOrBlankThrowingEx(
				transfer.getTransferTo(),
				transfer.getValue(),
				token
		);
		return new ResponseEntity<>(service.transfer(transfer, token), HttpStatus.OK);
	}

}

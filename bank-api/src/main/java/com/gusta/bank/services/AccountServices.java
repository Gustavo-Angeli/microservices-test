package com.gusta.bank.services;

import com.gusta.bank.exceptions.*;
import com.gusta.bank.model.entity.*;
import com.gusta.bank.model.vo.*;
import com.gusta.bank.proxy.*;
import com.gusta.bank.repositories.AccountRepository;
import com.gusta.bank.controllers.*;
import lombok.*;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
@RequiredArgsConstructor
public class AccountServices {

	private final AccountRepository repository;
	private final AuthProxy proxy;
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServices.class);

	public AccountVO createAccount(AccountVO accountVO) {
		String username = accountVO.getUsername();
		String password = accountVO.getPassword();

		if (!repository.findByUsername(username).isEmpty()) {
			LOGGER.info("Username: {} already being used");
			throw new RepeatedAccountException();
		}

		LOGGER.info("Creating account credentials");
		proxy.create(
				UserCredentialsVO.builder()
						.username(username)
						.password(password)
						.build()
		);
		LOGGER.info("Account credentials created");

		LOGGER.info("Creating account data");
		repository.save(
				Account.builder()
						.username(accountVO.getUsername())
						.accountBalance(0D)
						.build()
		);
		LOGGER.info("Account data created");

		accountVO
				.add(
						linkTo(methodOn(AccountController.class)
								.deposit(new DepositVO())).withSelfRel(),
						linkTo(methodOn(AccountController.class)
								.transfer(new TransferVO(), "jwtToken")).withSelfRel()
				)
				.setAccountBalance(0D);

		return accountVO;
	}

	public String deposit(DepositVO deposit) {
		if (deposit.getValue() <= 0) throw new InvalidValueException();

		LOGGER.info("finding the account");
		Account account = repository.findByUsername(deposit.getDepositTo())
				.orElseThrow(() -> new IllegalArgumentException("Username " + deposit.getDepositTo() + " not found!"));

		LOGGER.info("Updating the balance of {} account", account.getUsername());
		account.setAccountBalance(account.getAccountBalance() + deposit.getValue());
		LOGGER.info("Account balance is updated");

		LOGGER.info("Saving the new balance of {} account", account.getUsername());
		repository.save(account);
		LOGGER.info("Account balance of {} is updated", account.getUsername());

		return String.format("Deposit to %s account, has been successful", deposit.getDepositTo());
	}// end of the deposit method

	public String transfer(TransferVO transfer, String token) {
		if (!proxy.validateToken(token)) throw new IllegalArgumentException("Invalid token!");

		LOGGER.info("Finding the user account via JWT token");
		Account fromAccount = repository.findByUsername(proxy.getSubject(token))
				.orElseThrow(() -> new IllegalArgumentException("User not found!"));

		if (transfer.getValue() <= 0 || transfer.getValue() > fromAccount.getAccountBalance()) {
			LOGGER.error("{} is a invalid value", transfer.getValue());
			throw new InvalidValueException();
		}
		if (fromAccount.getUsername().equals(transfer.getTransferTo())) {
			LOGGER.error("Not possible transfer money to yourself");
			throw new IllegalArgumentException("Not possible transfer money to yourself");
		}

		LOGGER.info("Finding the destiny account by name, username: {}", transfer.getTransferTo());
		Account toAccount = repository.findByUsername(transfer.getTransferTo())
				.orElseThrow(() -> new IllegalArgumentException("Username " + transfer.getTransferTo() + " not found!"));

		LOGGER.info("setting the balance of {} account", fromAccount.getUsername());
		fromAccount.setAccountBalance(fromAccount.getAccountBalance() - transfer.getValue());

		LOGGER.info("setting the balance of the {} account", toAccount.getUsername());
		toAccount.setAccountBalance(toAccount.getAccountBalance() + transfer.getValue());

		repository.save(fromAccount);
		LOGGER.info("the balance of {} account is updated", fromAccount.getUsername());
		repository.save(toAccount);
		LOGGER.info("the balance of {} account is updated", toAccount.getUsername());

		return String.format("Transfer to %s account, has been successful", toAccount.getUsername());
	}// end of the transfer method

}
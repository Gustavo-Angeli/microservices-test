package com.gusta.bank.integrationtests.data.vo.v1;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement
public class AccountVO {


	private Long key;
	private String accountName;
	private String accountPassword;
	private Double accountBalance;

	public AccountVO() {
	}

	public AccountVO(String accountName, String accountPassword) {
		this.accountName = accountName;
		this.accountPassword = accountPassword;
	}

	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}

	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPassword() {
		return accountPassword;
	}
	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountBalance, accountName, accountPassword, key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountVO other = (AccountVO) obj;
		return Objects.equals(accountBalance, other.accountBalance) && Objects.equals(accountName, other.accountName)
				&& Objects.equals(accountPassword, other.accountPassword) && Objects.equals(key, other.key);
	}

}

package com.gusta.bank.unittests.mapper.mocks;

import com.gusta.bank.data.vo.v1.*;
import com.gusta.bank.model.vo.*;

public class MockDeposit {

    public DepositVO mockDeposit() {
        return mockDeposit(0);
    }

    public DepositVO mockDeposit(Integer number) {
        DepositVO deposit = new DepositVO();
        deposit.setDepositTo("Account name " + number);
        deposit.setValue(10D);
        return deposit;
    }


}
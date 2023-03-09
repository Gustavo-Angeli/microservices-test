package com.gusta.bank.unittests.mapper.mocks;

import com.gusta.bank.data.vo.v1.*;
import com.gusta.bank.model.vo.*;

public class MockTransfer {

    public TransferVO mockTransfer() {
        return mockTransfer(0);
    }

    public TransferVO mockTransfer(Integer number) {
        TransferVO transfer = new TransferVO();
        transfer.setDestinyAccountName("Destiny account " + number);
        transfer.setValueTransfer(10D);
        return transfer;
    }


}
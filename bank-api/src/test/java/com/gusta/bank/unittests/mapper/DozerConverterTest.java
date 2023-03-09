package com.gusta.bank.unittests.mapper;

import com.gusta.bank.model.vo.AccountVO;
import com.gusta.bank.mapper.*;
import com.gusta.bank.model.entity.Account;
import com.gusta.bank.unittests.mapper.mocks.MockAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DozerConverterTest {

    MockAccount inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockAccount();
    }

    @Test
    public void parseEntityToVOTest() {
        AccountVO output = DozerMapper.parseObject(inputObject.mockEntity(), AccountVO.class);
        assertEquals("Account name 0", output.getAccountName());
        assertEquals("Account password 0", output.getAccountPassword());
        assertEquals(100D, output.getAccountBalance());
    }
    @Test
    public void parseVOToEntityTest() {
        Account output = DozerMapper.parseObject(inputObject.mockVO(), Account.class);
        assertEquals("Account name 0", output.getAccountName());
        assertEquals("Account password 0", output.getAccountPassword());
        assertEquals(100D, output.getAccountBalance());
    }

}

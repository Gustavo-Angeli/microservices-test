//package com.gusta.bank.unittests.services;
//
//import com.gusta.bank.data.vo.v1.*;
//import com.gusta.bank.exceptions.*;
//import com.gusta.bank.model.entity.*;
//import com.gusta.bank.model.vo.*;
//import com.gusta.bank.repositories.*;
//import com.gusta.bank.services.*;
//import com.gusta.bank.unittests.mapper.mocks.*;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.*;
//import org.mockito.*;
//import org.mockito.junit.jupiter.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ExtendWith(MockitoExtension.class)
//public class AccountServicesTest {
//
//    private final MockAccount input = new MockAccount();
//    private Account entity = input.mockEntity();
//    private final Account persisted = entity;
//    private AccountVO vo = input.mockVO();
//    private DepositVO deposit = new MockDeposit().mockDeposit();
//    private TransferVO transfer = new MockTransfer().mockTransfer();
//
//    @InjectMocks
//    private AccountServices service;
//    @Mock
//    private AccountRepository repository;
//
//    @BeforeAll
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//    @AfterEach
//    void reset() {
//        entity = input.mockEntity();
//        vo = input.mockVO();
//        deposit = new MockDeposit().mockDeposit();
//        transfer = new MockTransfer().mockTransfer();
//    }
//
//    //start tests (Create method)
//    @Test
//    void testCreateAccount_NullFields() {
//        vo.setAccountName(null);
//        vo.setAccountPassword(null);
//        assertThrows(NullPointerException.class, () -> service.createAccount(vo));
//    }
//    @Test
//    void testCreateAccount_AccountExistent() {
//        when(repository.checkIfExists(vo.getAccountName())).thenReturn(entity.getAccountName());
//
//        assertNotNull(vo.getAccountName());
//        assertNotNull(vo.getAccountPassword());
//        assertNotEquals("", vo.getAccountName());
//        assertNotEquals("", vo.getAccountPassword());
//
//        assertThrows(RepeatedAccountException.class, () -> service.createAccount(vo));
//    }
//    @Test
//    void testCreateAccount_CreatedAccount() {
//        AccountVO result = service.createAccount(vo);
//
//        assertNotNull(vo.getAccountName());
//        assertNotNull(vo.getAccountPassword());
//        assertNotEquals("", vo.getAccountName());
//        assertNotEquals("", vo.getAccountPassword());
//
//        Account verifyDoesNotExist = repository.findByUsername(vo.getAccountName());
//        assertNull(verifyDoesNotExist);
//
//        assertNotEquals("", vo.getAccountName());
//        assertNotEquals("", vo.getAccountPassword());
//
//        assertEquals("Account name 0", result.getAccountName());
//        assertEquals(100D, result.getAccountBalance());
//    }
//    @Test
//    void testCreateAccount_EmptyFields() {
//        vo.setAccountName("");
//        vo.setAccountPassword("");
//
//        assertThrows(IllegalArgumentException.class, () -> service.createAccount(vo));
//        assertEquals("", vo.getAccountName());
//        assertEquals("", vo.getAccountPassword());
//    }
//    // end of tests (Create method)
//
//    // start tests (Deposit method)
//    @Test
//    void testReceive_AccountFieldsNull() {
//        deposit.setAccountName(null);
//        deposit.setDepositValue(null);
//        assertThrows(NullPointerException.class, () -> service.deposit(deposit));
//    }
//    @Test
//    void testReceive_AccountFieldsEmpty() {
//        deposit.setAccountName("");
//        assertThrows(IllegalArgumentException.class, () -> service.deposit(deposit));
//    }
//    @Test
//    void testReceive_NonExistentAccount() {
//        when(repository.checkIfExists(deposit.getAccountName())).thenReturn(null);
//
//        assertThrows(RequiredObjectIsNullException.class, () -> service.deposit(deposit));
//
//        assertNotNull(deposit.getAccountName());
//        assertNotEquals("", deposit.getAccountName());
//    }
//    @Test
//    void testReceive_DepositValueEqualsOrLessThanZero() {
//        deposit.setDepositValue(-1D);
//
//        assertThrows(InvalidValueException.class, () -> service.deposit(deposit));
//        assertNotNull(deposit.getAccountName());
//        assertNotEquals("", deposit.getAccountName());
//    }
//    @Test
//    void testReceive_DepositSuccessfully() {
//        when(repository.checkIfExists(deposit.getAccountName())).thenReturn(entity.getAccountName());
//        when(repository.getAccountBalanceByUsername(entity.getAccountName())).thenReturn(entity.getAccountBalance());
//
//        service.deposit(deposit);
//
//        System.out.println(entity.getAccountBalance());
//        assertNotNull(deposit.getAccountName());
//        assertNotNull(deposit.getDepositValue());
//        assertNotEquals("", deposit.getAccountName());
//        assertNotEquals("", deposit.getDepositValue().toString());
//        assertTrue(deposit.getDepositValue() > 0);
//        assertEquals(110D, deposit.getDepositValue() + entity.getAccountBalance());
//    }
//    //end of tests (Deposit method)
//
//    //start tests (Transfer method)
//    @Test
//    void testTransfer_InvalidValue() {
//        transfer.setValueTransfer(1000D);
//        when(repository.findByUsername(entity.getAccountName())).thenReturn(new MockAccount().mockEntity());
//
//        service.loadUserByUsername(entity.getAccountName());
//        assertThrows(InvalidValueException.class, () -> service.transfer(transfer));
//    }
//    @Test
//    void testTransfer_DestinyAccountParamNull() {
//        transfer.setDestinyAccountName(null);
//        assertThrows(NullPointerException.class, () -> service.transfer(transfer));
//    }
//    @Test
//    void testTransfer_DestinyAccountParamEmpty() {
//        when(repository.findByUsername(entity.getAccountName())).thenReturn(persisted);
//        transfer.setDestinyAccountName("");
//
//        service.loadUserByUsername(entity.getAccountName());
//
//        assertThrows(IllegalArgumentException.class, () -> service.transfer(transfer));
//    }
//    @Test
//    void testTransfer_DestinyAccountNonExistent() {
//        when(repository.findByUsername(entity.getAccountName())).thenReturn(persisted);
//
//        service.loadUserByUsername(entity.getAccountName());
//        assertThrows(RequiredObjectIsNullException.class, () -> service.transfer(transfer));
//
//        assertNotNull(transfer.getDestinyAccountName());
//        assertNotEquals("", transfer.getDestinyAccountName());
//    }
//    @Test
//    void testTransfer_DestinyAccountEqualsLoggedAccount() {
//        transfer.setDestinyAccountName(entity.getAccountName());
//
//        when(repository.findByUsername(entity.getAccountName())).thenReturn(persisted);
//        when(repository.findByUsername(transfer.getDestinyAccountName())).thenReturn(persisted);
//
//        service.loadUserByUsername(entity.getAccountName());
//        assertThrows(IllegalArgumentException.class, () -> service.transfer(transfer));
//    }
//    @Test
//    void testTransfer_TransferSuccessful () {
//        when(repository.findByUsername(entity.getAccountName())).thenReturn(persisted);
//        when(repository.checkIfExists(transfer.getDestinyAccountName())).thenReturn(entity.getAccountName());
//        when(repository.getAccountBalanceByUsername(transfer.getDestinyAccountName())).thenReturn(entity.getAccountBalance());
//
//        service.loadUserByUsername(entity.getAccountName());
//        service.transfer(transfer);
//
//        assertNotNull(transfer.getDestinyAccountName());
//        assertNotNull(transfer.getValueTransfer());
//        assertNotEquals("", transfer.getDestinyAccountName());
//        assertNotEquals("", transfer.getValueTransfer().toString());
//
//        assertTrue(transfer.getValueTransfer() <= entity.getAccountBalance());
//
//        assertEquals(90D, vo.getAccountBalance() - transfer.getValueTransfer());
//        assertEquals(110D, vo.getAccountBalance() + transfer.getValueTransfer());
//    }
//    //end of tests (Transfer method)
//}

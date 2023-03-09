package com.gusta.bank.integrationtests.controller;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.gusta.bank.configs.*;
import com.gusta.bank.integrationtests.data.operations.v1.*;
import com.gusta.bank.integrationtests.data.vo.v1.*;
import com.gusta.bank.integrationtests.testcontainers.*;
import com.gusta.bank.model.vo.security.*;
import io.restassured.builder.*;
import io.restassured.filter.log.*;
import io.restassured.specification.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerJsonTests extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static AccountVO vo;
    private static Deposit deposit;
    private static Transfer transfer;

    @BeforeAll
    public static void setUp() {
        vo = new AccountVO();
        deposit = new Deposit();
        transfer = new Transfer();
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @AfterAll
    public static void reset() {
        vo = new AccountVO();
        deposit = new Deposit();
        transfer = new Transfer();
    }

    @Test
    @Order(1)
    public void testCreateAccount_Success() throws JsonProcessingException {

        mockVO();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/bank/v1/create")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(vo)
                    .when()
                    .post()
                .then()
                    .statusCode(201)
                        .extract()
                            .body()
                                .asString();

        //Account info
        assertTrue(content.contains(vo.getAccountName()));
        assertTrue(content.contains(vo.getAccountBalance().toString()));
        assertFalse(content.contains(vo.getAccountPassword()));

        //Links
        assertTrue(content.contains("http://localhost:8888/api/bank/v1/login"));
        assertTrue(content.contains("http://localhost:8888/api/bank/v1/deposit"));
    }

    @Test
    @Order(2)
    public void testCreateAccount_NullParameters() {

        mockVO();
        vo.setAccountName(null);
        vo.setAccountName(null);

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(vo)
                    .when()
                    .post()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("Not possible create a account with null fields"));
    }

    @Test
    @Order(2)
    public void testCreateAccount_BlankParameters() {

        mockVO();
        vo.setAccountName("");
        vo.setAccountName("");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(vo)
                    .when()
                    .post()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("Not possible create a account with empty fields"));
    }

    @Test
    @Order(3)
    public void testCreateAccount_ExistentAccount() {

        mockVO();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(vo)
                    .when()
                    .post()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("This name is already being used, please try other name"));
    }

    @Test
    @Order(4)
    public void testReceive_AccountFieldsNull() {

        mockDeposit();
        deposit.setAccountName(null);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/bank/v1/deposit")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(deposit)
                    .when()
                    .put()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("Enter a value in the parameters"));
    }

    @Test
    @Order(5)
    public void testReceive_AccountFieldsBlank() {

        mockDeposit();
        deposit.setAccountName("");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(deposit)
                    .when()
                    .put()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("Enter a valid value in the parameters"));
    }

    @Test
    @Order(6)
    public void testReceive_NonExistentAccount() {

        mockVO();
        vo.setAccountName("nonExistent");
        mockDeposit();
        deposit.setAccountName(vo.getAccountName());

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(deposit)
                    .when()
                    .put()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("This account does not exists, please register in /api/bank/v1/create"));
    }

    @Test
    @Order(7)
    public void testReceive_DepositValueEqualsOrLessThanZero() {

        mockDeposit();
        deposit.setDepositValue(0D);

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(deposit)
                    .when()
                    .put()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("Put a valid value!"));
    }

    @Test
    @Order(8)
    public void testReceive_DepositSuccessfully() {

        mockDeposit();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(deposit)
                    .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("the deposit is completed"));
    }

    @Test
    @Order(9)
    public void testTransfer_NonLoggedAccount() {

        mockTransfer();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/bank/v1/transfer")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(transfer)
                .when()
                .put()
            .then()
                .statusCode(403);
    }

    @Test
    @Order(10)
    public void authorization() {

        AccountVO user = new AccountVO("Janel", "pass");

        var accessToken = given()
                .basePath("/api/bank/v1/login")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                    .when()
                .post()
                    .then()
                        .statusCode(201)
                            .extract()
                                .body()
                                    .as(TokenVO.class)
                                        .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/bank/v1/transfer")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(11)
    public void testTransfer_InvalidValue() {

        mockTransfer();
        transfer.setValueTransfer(0D);

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(transfer)
                    .when()
                    .put()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("Put a valid value!"));
    }

    @Test
    @Order(12)
    public void testTransfer_DestinyAccountParamNull() {

        mockTransfer();
        transfer.setDestinyAccountName(null);

        var content = given().spec(specification)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(transfer)
                        .when()
                        .put()
                    .then()
                        .statusCode(400)
                                .extract()
                                        .body()
                                                .asString();

        assertTrue(content.contains("Enter a value in the parameters"));
    }

    @Test
    @Order(13)
    public void testTransfer_DestinyAccountParamEmpty() {

        mockTransfer();
        transfer.setDestinyAccountName("");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(transfer)
                    .when()
                    .put()
                .then()
                    .statusCode(400)
                            .extract()
                                    .body()
                                            .asString();

        assertTrue(content.contains("Enter a valid value in the parameters"));
    }

    @Test
    @Order(14)
    public void testTransfer_DestinyAccountNonExistent() {

        mockTransfer();
        transfer.setDestinyAccountName("NonExistent");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(transfer)
                    .when()
                    .put()
                .then()
                    .statusCode(400)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("This account does not exists, please register in /api/bank/v1/create"));
    }

    @Test
    @Order(15)
    public void testTransfer_DestinyAccountEqualsLoggedAccount() {

        mockTransfer();
        transfer.setDestinyAccountName("Janel");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(transfer)
                    .when()
                    .put()
                .then()
                    .statusCode(400)
                            .extract()
                                    .body()
                                            .asString();

        assertTrue(content.contains("Not possible transfer money to yourself"));
    }

    @Test
    @Order(16)
    public void testTransfer_TransferSuccessful() {

        mockTransfer();
        transfer.setDestinyAccountName("tests");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(transfer)
                    .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        assertTrue(content.contains("Transfer successful"));
    }

    private void mockVO() {
        vo.setAccountName("tests");
        vo.setAccountPassword("1234");
        vo.setAccountBalance(100D);
    }

    private void mockDeposit() {
        deposit.setAccountName("gusta");
        deposit.setDepositValue(100D);
    }

    private void mockTransfer() {
        transfer.setDestinyAccountName("gusta");
        transfer.setValueTransfer(100D);
    }

}

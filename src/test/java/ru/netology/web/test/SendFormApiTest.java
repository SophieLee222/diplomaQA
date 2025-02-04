package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SqlHelper;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SendFormApiTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should receive APPROVED status")
    void shouldGetApprovedStatus() {
        // проверяем статусы апи и базы данных
        assertAll("Checking APPROVED status from API and DB",
                () -> assertEquals("APPROVED", DataHelper.getResponseStatus(DataHelper.getValidUser()),
                        "Expected API status to be 'APPROVED'"),
                () -> assertEquals("APPROVED", SqlHelper.getStatus(),
                        "Expected DB status to be 'APPROVED'")
        );
        SqlHelper.cleanTransactions();
    }

    @Test
    @DisplayName("Should receive DECLINED status")
    void shouldGetDeclinedStatus() {
        // проверяем статусы апи и базы данных
        assertAll("Checking DECLINED status from API and DB",
                () -> assertEquals("DECLINED", DataHelper.getResponseStatus(DataHelper.getDeclinedCardUser()),
                        "Expected API status to be 'DECLINED'"),
                () -> assertEquals("DECLINED", SqlHelper.getStatus(),
                        "Expected DB status to be 'DECLINED'")
        );
        SqlHelper.cleanTransactions();
    }
}

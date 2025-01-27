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

import static io.restassured.RestAssured.given;
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
        DataHelper.CardInfo validUser = DataHelper.getValidUser();

        Response response = RestAssured.given()
                .baseUri("http://localhost:8080")
                .header("Content-Type", "application/json")
                .body(validUser) // Передаём изменённый JSON
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .extract()
                .response();
        // Извлекаем статус из ответа
        String status = response.jsonPath().getString("status");

        // Проверяем, что статус равен "APPROVED"
        assertEquals("APPROVED", status, "Expected status to be 'APPROVED'");
    }

    @Test
    @DisplayName("Should receive DECLINED status")
    void shouldGetDeclinedStatus() {
        DataHelper.CardInfo declinedCardUser = DataHelper.getDeclinedCardUser();

        Response response = RestAssured.given()
                .baseUri("http://localhost:8080")
                .header("Content-Type", "application/json")
                .body(declinedCardUser)
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .extract()
                .response();
        // Извлекаем статус из ответа
        String status = response.jsonPath().getString("status");

        // Проверяем, что статус равен "DECLINED"
        assertEquals("DECLINED", status, "Expected status to be 'APPROVED'");
    }
}

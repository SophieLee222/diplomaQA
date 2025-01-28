package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SqlHelper;
import ru.netology.web.page.FormPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SendFormTest {
    private FormPage formPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:8080");
        formPage = new FormPage();
        formPage.openDebitCardForm();
    }

    @Test
    @DisplayName("Should Send Valid Form and receive APPROVED status")
    void shouldSendValidFormAndGetApprovedStatus() {
        var expectedStatus = "APPROVED";
        String actualStatus;
        boolean notificationCheckFailed = false;

        try {
            formPage.sendValidForm();
        } catch (UIAssertionError e) {
            notificationCheckFailed = true;
            System.err.println("Уведомление об успехе операции не обнаружено: " + e.getMessage());
        }

        // Проверка статуса из базы данных даже при отсутствии уведомления
        actualStatus = SqlHelper.getStatus();
        assertEquals(expectedStatus, actualStatus, "Expected status to be 'APPROVED'");

        // Очистка транзакций
        SqlHelper.cleanTransactions();

        // Если уведомление не найдено, явно провалить тест после проверки статуса
        if (notificationCheckFailed) {
            fail("Уведомление об успехе операции не было отображено, но статус операции в БД верный");
        }
    }


    @Test
    @DisplayName("Should Display Declined Card Notification and receive DECLINED status")
    void shouldGetDeclinedCardNotificationAndDeclinedStatus() {
        var expectedStatus = "DECLINED";
        String actualStatus;
        boolean notificationCheckFailed = false;

        try {
            formPage.sendDeclinedCardForm();
        } catch (UIAssertionError e) {
            notificationCheckFailed = true;
            System.err.println("Уведомление об ошибке операции не обнаружено: " + e.getMessage());
        }

        // Проверка статуса из базы данных даже при отсутствии уведомления
        actualStatus = SqlHelper.getStatus();
        assertEquals(expectedStatus, actualStatus, "Expected status to be 'DECLINED'");

        // Очистка транзакций
        SqlHelper.cleanTransactions();

        // Если уведомление не найдено, явно провалить тест после проверки статуса
        if (notificationCheckFailed) {
            fail("Уведомление об ошибке операции не было отображено, но статус операции в БД верный");
        }
    }

    @Test
    @DisplayName("Should Display Wrong Card Format Error")
    void shouldGetWrongCardFormatError() {
        formPage.fillInvalidCardFormAndCheckError();
    }

    @Test
    @DisplayName("Should Display Wrong Month Format Error")
    void shouldGetWrongMonthFormatError() {
        formPage.fillInvalidMonthForm();
    }

    @Test
    @DisplayName("Should Display Wrong Year Format Error")
    void shouldGetWrongYearFormatError() {
        formPage.fillInvalidYearForm();
    }

    @Test
    @DisplayName("Should Display Wrong Owner Format Error")
    void shouldGetWrongOwnerFormatError() {
        formPage.fillInvalidOwnerForm();
    }

    @Test
    @DisplayName("Should Display Wrong CVC Format Error")
    void shouldGetWrongCvcFormatError() {
        formPage.fillInvalidCvcForm();
    }

    @Test
    @DisplayName("Should Display Expired Month Error")
    void shouldGetExpiredMonthError() {
        formPage.fillExpiredMonthFormAndCheckError();
    }

    @Test
    @DisplayName("Should Display Expired Year Error")
    void shouldGetExpiredYearError() {
        formPage.fillExpiredYearFormAndCheckError();
    }

    @Test
    @DisplayName("Should Display Empty Card Number Error")
    void shouldDisplayEmptyCardNumberError() {
        formPage.checkEmptyCardNumberField();
    }

    @Test
    @DisplayName("Should Display Empty Month Error")
    void shouldDisplayEmptyMonthError() {
        formPage.checkEmptyMonthField();
    }

    @Test
    @DisplayName("Should Display Empty Year Error")
    void shouldDisplayEmptyYearError() {
        formPage.checkEmptyYearField();
    }

    @Test
    @DisplayName("Should Display Empty Owner Error")
    void shouldDisplayEmptyOwnerError() {
        formPage.checkEmptyOwnerField();
    }

    @Test
    @DisplayName("Should Display Empty CVC Error")
    void shouldDisplayEmptyCvcError() {
        formPage.checkEmptyCvcField();
    }
}
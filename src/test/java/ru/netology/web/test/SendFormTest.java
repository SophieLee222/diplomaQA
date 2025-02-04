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

import static org.junit.jupiter.api.Assertions.*;

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
        formPage.fillForm(DataHelper.getValidUser());

        assertAll("Checking UI notification and DB status",
                () -> formPage.verifySuccessNotification(), // Проверка уведомления
                () -> assertEquals("APPROVED", SqlHelper.getStatus(),
                        "Expected status to be 'APPROVED'")
        );

        SqlHelper.cleanTransactions();
    }

    @Test
    @DisplayName("Should Display Declined Card Notification and receive DECLINED status")
    void shouldGetDeclinedCardNotificationAndDeclinedStatus() {
        formPage.fillForm(DataHelper.getDeclinedCardUser());

        assertAll("Checking UI notification and DB status",
                () -> formPage.verifyErrorNotification(), // Проверка уведомления
                () -> assertEquals("DECLINED", SqlHelper.getStatus(),
                        "Expected status to be 'DECLINED'")
        );

        SqlHelper.cleanTransactions();
    }

    @Test
    @DisplayName("Should Display Wrong Card Format Error")
    void shouldGetWrongCardFormatError() {
        DataHelper.CardInfo invalidCardUser = DataHelper.getInvalidCardUser();
        formPage.fillForm(invalidCardUser);
        formPage.checkCardFieldError("Неверный формат");
    }

    @Test
    @DisplayName("Should Display Wrong Month Format Error")
    void shouldGetWrongMonthFormatError() {
        DataHelper.CardInfo invalidMonthUser = DataHelper.getInvalidMonthUser();
        formPage.fillForm(invalidMonthUser);
        formPage.checkMonthFieldError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Should Display Wrong Year Format Error")
    void shouldGetWrongYearFormatError() {
        DataHelper.CardInfo invalidYearUser = DataHelper.getInvalidYearUser();
        formPage.fillForm(invalidYearUser);
        formPage.checkYearFieldError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Should Display Wrong Owner Format Error")
    void shouldGetWrongOwnerFormatError() {
        DataHelper.CardInfo invalidOwnerUser = DataHelper.getInvalidOwnerUser();
        formPage.fillForm(invalidOwnerUser);
        formPage.checkOwnerFieldError("Неверный формат");
    }

    @Test
    @DisplayName("Should Display Wrong CVC Format Error")
    void shouldGetWrongCvcFormatError() {
        DataHelper.CardInfo invalidCvcUser = DataHelper.getInvalidCvcUser();
        formPage.fillForm(invalidCvcUser);
        formPage.checkCvcFieldError("Неверный формат");
    }

    @Test
    @DisplayName("Should Display Expired Month Error")
    void shouldGetExpiredMonthError() {
        DataHelper.CardInfo expiredMonthUser = DataHelper.getExpiredMonthUser();
        formPage.fillForm(expiredMonthUser);
        formPage.checkMonthFieldError("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Should Display Expired Year Error")
    void shouldGetExpiredYearError() {
        DataHelper.CardInfo expiredYearUser = DataHelper.getExpiredYearUser();
        formPage.fillForm(expiredYearUser);
        formPage.checkYearFieldError("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Should Display Empty Card Number Error")
    void shouldDisplayEmptyCardNumberError() {
        DataHelper.CardInfo emptyCardUser = DataHelper.getEmptyCardUser();
        formPage.fillForm(emptyCardUser);
        formPage.checkCardFieldError("Неверный формат");
    }

    @Test
    @DisplayName("Should Display Empty Month Error")
    void shouldDisplayEmptyMonthError() {
        DataHelper.CardInfo emptyMonthUser = DataHelper.getEmptyMonthUser();
        formPage.fillForm(emptyMonthUser);
        formPage.checkMonthFieldError("Неверный формат");
    }

    @Test
    @DisplayName("Should Display Empty Year Error")
    void shouldDisplayEmptyYearError() {
        DataHelper.CardInfo emptyYearUser = DataHelper.getEmptyYearUser();
        formPage.fillForm(emptyYearUser);
        formPage.checkYearFieldError("Неверный формат");
    }

    @Test
    @DisplayName("Should Display Empty Owner Error")
    void shouldDisplayEmptyOwnerError() {
        DataHelper.CardInfo emptyOwnerUser = DataHelper.getEmptyOwnerUser();
        formPage.fillForm(emptyOwnerUser);
        formPage.checkOwnerFieldError("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Should Display Empty CVC Error")
    void shouldDisplayEmptyCvcError() {
        DataHelper.CardInfo emptyCvcUser = DataHelper.getEmptyCvcUser();
        formPage.fillForm(emptyCvcUser);
        formPage.checkCvcFieldError("Неверный формат");
    }
}
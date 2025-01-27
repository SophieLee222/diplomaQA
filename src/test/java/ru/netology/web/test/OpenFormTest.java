package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.page.FormPage;

public class OpenFormTest {
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
    }

    @Test
    @DisplayName("Should Open Debit Card Form")
    void shouldOpenDebitCardForm() {
        formPage.openDebitCardForm();
    }

    @Test
    @DisplayName("Should Open Credit Card Form")
    void shouldOpenCreditCardForm() {
        formPage.openCreditCardForm();
    }
}

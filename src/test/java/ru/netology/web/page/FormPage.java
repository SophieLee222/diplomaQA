package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.netology.web.data.DataHelper;

import java.time.Duration;
import java.util.Arrays;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FormPage {
    //поля формы
    private final SelenideElement cardNumberField = $$("span").findBy(text("Номер карты")).$("input");
    private final SelenideElement monthField = $$("span").findBy(text("Месяц")).$("input");
    private final SelenideElement yearField = $$("span.input__top").findBy(text("Год"))
            .parent().parent().$("input.input__control");
    private final SelenideElement ownerField = $$("span").findBy(text("Владелец")).$("input");
    private final SelenideElement cvcField = $$("span.input__top").findBy(text("CVC/CVV"))
            .parent().parent().$("input.input__control");

    //кнопка отправки формы
    private final SelenideElement continueButton = $$("button").findBy(text("Продолжить"));

    //уведомления
    private final SelenideElement successNotification = $(".notification_status_ok .notification__content");
    private final SelenideElement errorNotification = $(".notification_status_error .notification__content");

    //ошибки полей
    private final SelenideElement cardError = $$(".form-field span.input__top").findBy(text("Номер карты"))
            .closest(".form-field").$(".input__sub");
    private final SelenideElement monthError = $$(".form-field span.input__top").findBy(text("Месяц"))
            .parent().parent().$(".input__sub");
    private final SelenideElement yearError = $$(".form-field span.input__top").findBy(text("Год"))
            .closest(".form-field").$(".input__sub");
    private final SelenideElement ownerError = $$(".form-field span.input__top").findBy(text("Владелец"))
            .closest(".form-field").$(".input__sub");
    private final SelenideElement cvcError = $$(".form-field span.input__top").findBy(text("CVC/CVV"))
            .closest(".form-field").$(".input__sub");


    //открыть форму для дебетововой карты
    public void openDebitCardForm() {
        $$("button span.button__text").findBy(exactText("Купить")).click();
        $$("h3").get(1).shouldHave(text("Оплата по карте")).shouldBe(visible);
    }

    //открыть форму для кредитной карты
    public void openCreditCardForm() {
        $$("button").findBy(exactText("Купить в кредит")).click();
        $$("h3").get(1).shouldHave(text("Кредит по данным карты")).shouldBe(visible);
    }

    //заполнить форму данными
    public void fillForm(DataHelper.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getOwner());
        cvcField.setValue(String.valueOf(cardInfo.getCvc()));
        continueButton.click();
    }

    //проверить наличие уведомления об успехе операции
    public void verifySuccessNotification() {
        successNotification.shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Операция одобрена Банком"));
    }

    //проверить наличие уведомления об ошибке
    public void verifyErrorNotification() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    // Проверка текстов ошибок полей

    public void checkCardFieldError(String expectedErrorText) {
        cardError.shouldBe(visible).shouldHave(text(expectedErrorText));
    }

    public void checkMonthFieldError(String expectedErrorText) {
        monthError.shouldBe(visible).shouldHave(text(expectedErrorText));
    }

    public void checkYearFieldError(String expectedErrorText) {
        yearError.shouldBe(visible).shouldHave(text(expectedErrorText));
    }

    public void checkOwnerFieldError(String expectedErrorText) {
        ownerError.shouldBe(visible).shouldHave(text(expectedErrorText));
    }

    public void checkCvcFieldError(String expectedErrorText) {
        cvcError.shouldBe(visible).shouldHave(text(expectedErrorText));
    }
}

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
    private void fillForm(DataHelper.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getOwner());
        cvcField.setValue(String.valueOf(cardInfo.getCvc()));
        continueButton.click();
    }

    //проверить наличие уведомления об успехе операции
    private void verifySuccessNotification() {
        successNotification.shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Операция одобрена Банком"));
    }

    //проверить наличие уведомления об ошибке
    private void verifyErrorNotification() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    // Заполнить форму валидными данными
    public void sendValidForm() {
        fillForm(DataHelper.getValidUser());
        verifySuccessNotification();
    }

    //Метод проверки статуса через БД
    public void checkResponseStatus() {

    }

    //заполнить форму declined картой + остальнами валидными данными
    public void sendDeclinedCardForm() {
        fillForm(DataHelper.getDeclinedCardUser());
        verifyErrorNotification();
    }

    // Общий метод для проверки ошибок формы
    private void fillFormAndCheckError(DataHelper.CardInfo cardInfo, SelenideElement errorElement, String expectedErrorText) {
        fillForm(cardInfo);
        if (errorElement.exists()) {
            errorElement.shouldBe(visible).shouldHave(text(expectedErrorText));
        } else {
            throw new AssertionError("Ошибка для указанного поля не найдена.");
        }
    }

// Используем общий метод для проверки всех полей

    // Проверка невалидного формата карты
    public void fillInvalidCardFormAndCheckError() {
        DataHelper.CardInfo invalidUser = DataHelper.getInvalidInfoUser();
        DataHelper.CardInfo validUser = DataHelper.getValidUser();
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        invalidUser.getCardNumber(),
                        validUser.getMonth(),
                        validUser.getYear(),
                        validUser.getOwner(),
                        validUser.getCvc()
                ),
                cardError,
                "Неверный формат"
        );
    }

    // Проверка невалидного формата месяца
    public void fillInvalidMonthForm() {
        DataHelper.CardInfo invalidUser = DataHelper.getInvalidInfoUser();
        DataHelper.CardInfo validUser = DataHelper.getValidUser();
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        validUser.getCardNumber(),
                        invalidUser.getMonth(),
                        validUser.getYear(),
                        validUser.getOwner(),
                        validUser.getCvc()
                ),
                monthError,
                "Неверно указан срок действия карты"
        );
    }

    // Проверка невалидного формата года
    public void fillInvalidYearForm() {
        DataHelper.CardInfo invalidUser = DataHelper.getInvalidInfoUser();
        DataHelper.CardInfo validUser = DataHelper.getValidUser();
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        validUser.getCardNumber(),
                        validUser.getMonth(),
                        invalidUser.getYear(),
                        validUser.getOwner(),
                        validUser.getCvc()
                ),
                yearError,
                "Неверно указан срок действия карты"
        );
    }

    // Проверка невалидного формата владельца
    public void fillInvalidOwnerForm() {
        DataHelper.CardInfo invalidUser = DataHelper.getInvalidInfoUser();
        DataHelper.CardInfo validUser = DataHelper.getValidUser();
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        validUser.getCardNumber(),
                        validUser.getMonth(),
                        validUser.getYear(),
                        invalidUser.getOwner(),
                        validUser.getCvc()
                ),
                ownerError,
                "Неверный формат"
        );
    }

    // Проверка невалидного формата CVC
    public void fillInvalidCvcForm() {
        DataHelper.CardInfo invalidUser = DataHelper.getInvalidInfoUser();
        DataHelper.CardInfo validUser = DataHelper.getValidUser();
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        validUser.getCardNumber(),
                        validUser.getMonth(),
                        validUser.getYear(),
                        validUser.getOwner(),
                        invalidUser.getCvc()
                ),
                cvcError,
                "Неверный формат"
        );
    }

    // Проверка формы с истёкшим месяцем
    public void fillExpiredMonthFormAndCheckError() {
        fillFormAndCheckError(
                DataHelper.getExpiredMonthUser(),
                monthError,
                "Истёк срок действия карты"
        );
    }

    // Проверка формы с истёкшим годом
    public void fillExpiredYearFormAndCheckError() {
        fillFormAndCheckError(
                DataHelper.getExpiredYearUser(),
                yearError,
                "Истёк срок действия карты"
        );
    }

    // Проверка пустого поля номера карты
    public void checkEmptyCardNumberField() {
        fillFormAndCheckError(
                new DataHelper.CardInfo("", // Пустое поле
                        DataHelper.getValidUser().getMonth(),
                        DataHelper.getValidUser().getYear(),
                        DataHelper.getValidUser().getOwner(),
                        DataHelper.getValidUser().getCvc()
                ),
                cardError,
                "Неверный формат"
        );
    }

    // Проверка пустого поля месяца
    public void checkEmptyMonthField() {
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        DataHelper.getValidUser().getCardNumber(),
                        "", // Пустое поле
                        DataHelper.getValidUser().getYear(),
                        DataHelper.getValidUser().getOwner(),
                        DataHelper.getValidUser().getCvc()
                ),
                monthError,
                "Неверный формат"
        );
    }

    // Проверка пустого поля года
    public void checkEmptyYearField() {
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        DataHelper.getValidUser().getCardNumber(),
                        DataHelper.getValidUser().getMonth(),
                        "", // Пустое поле
                        DataHelper.getValidUser().getOwner(),
                        DataHelper.getValidUser().getCvc()
                ),
                yearError,
                "Неверный формат"
        );
    }

    // Проверка пустого поля владельца карты
    public void checkEmptyOwnerField() {
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        DataHelper.getValidUser().getCardNumber(),
                        DataHelper.getValidUser().getMonth(),
                        DataHelper.getValidUser().getYear(),
                        "", // Пустое поле
                        DataHelper.getValidUser().getCvc()
                ),
                ownerError,
                "Поле обязательно для заполнения"
        );
    }

    // Проверка пустого поля CVC
    public void checkEmptyCvcField() {
        fillFormAndCheckError(
                new DataHelper.CardInfo(
                        DataHelper.getValidUser().getCardNumber(),
                        DataHelper.getValidUser().getMonth(),
                        DataHelper.getValidUser().getYear(),
                        DataHelper.getValidUser().getOwner(),
                        0 // Пустое поле
                ),
                cvcError,
                "Неверный формат"
        );
    }
}

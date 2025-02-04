package ru.netology.web.data;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Value;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataHelper {
    //статичные методы для хранения информации о данных карты
    // для карты может быть approved number, declined number, invalid number
    public static String getApprovedCardNumber() {
        return new String("1111 2222 3333 4444");
    }

    public static String getDeclinedCardNumber() {
        return new String("5555 6666 7777 8888");
    }

    public static String getResponseStatus(DataHelper.CardInfo user) {
        Response response = RestAssured.given()
                .baseUri("http://localhost:8080")
                .header("Content-Type", "application/json")
                .body(user) // Передаём изменённый JSON
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .extract()
                .response();
        // Извлекаем статус из ответа
        String status = response.jsonPath().getString("status");
        return status;
    }

    public static String generateInvalidCardNumber() {
        int invalidNumber = new Random().nextInt(1000000000) + 1;
        return String.valueOf(invalidNumber);
    }

    //Месяц и Год: сначала нужна полностью валидная дата, далее истекший месяц, невалидный месяц, истекший год, невалидный год

    public static String generateValidMonth() {
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
        return month;
    }

    public static String generateValidYear() {
        String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }

    public static String generateExpiredMonth() {
        String month = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
        return month;
    }

    public static String generateInvalidMonth() {
        int invalidMonth = new Random().nextInt(87) + 13; // Генерация числа от 13 до 99
        return String.valueOf(invalidMonth);
    }

    public static String generateExpiredYear() {
        String year = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }

    public static String generateInvalidYear() {
        String invalidYear = LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy"));
        return invalidYear;
    }

    //Метод для герерации имени
    public static String generateOwnerName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    //CVC может быть валидным и невалидным

    // Метод генерации валидного CVC
    public static int generateValidCVC() {
        String validCvc = new Faker().numerify("###");
        return Integer.parseInt(validCvc);
    }

    // Метод генерации невалидного CVC
    public static int generateInvalidCVC() {
        String invalidCvc = new Faker().numerify("##");
        return Integer.parseInt(invalidCvc);
    }

    //валидный юзер
    public static CardInfo getValidUser() {
        String validYear = generateValidYear();
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(), validYear,
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с declined картой
    public static CardInfo getDeclinedCardUser() {
        return new CardInfo(getDeclinedCardNumber(),
                generateValidMonth(), generateValidYear(),
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с невалидной картой
    public static CardInfo getInvalidCardUser() {
        return new CardInfo(generateInvalidCardNumber(),
                generateValidMonth(), generateValidYear(),
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с невалидным месяцем
    public static CardInfo getInvalidMonthUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateInvalidMonth(), generateValidYear(),
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с невалидным годом
    public static CardInfo getInvalidYearUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(), generateInvalidYear(),
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с невалидным именем
    public static CardInfo getInvalidOwnerUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(), generateValidYear(),
                generateOwnerName("ru"), generateValidCVC());
    }

    //юзер с невалидным cvc
    public static CardInfo getInvalidCvcUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(), generateValidYear(),
                generateOwnerName("en"), generateInvalidCVC());
    }

    //юзер с полностью невалидными форматами данных
    public static CardInfo getInvalidInfoUser() {
        return new CardInfo(generateInvalidCardNumber(),
                generateInvalidMonth(), generateInvalidYear(),
                generateOwnerName("ru"), generateInvalidCVC());
    }

    //юзер с истекшим месяцем
    public static CardInfo getExpiredMonthUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateExpiredMonth(), generateValidYear(),
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с истекшим годом
    public static CardInfo getExpiredYearUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(),generateExpiredYear() ,
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с пустым полем карты
    public static CardInfo getEmptyCardUser() {
        return new CardInfo("",
                generateValidMonth(),generateValidYear() ,
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с пустым полем месяца
    public static CardInfo getEmptyMonthUser() {
        return new CardInfo(getApprovedCardNumber(),
                "",generateValidYear() ,
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с пустым полем года
    public static CardInfo getEmptyYearUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(),"",
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с пустым полем владельца
    public static CardInfo getEmptyOwnerUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(),generateValidYear(),
                "", generateValidCVC());
    }

    //юзер с пустым полем cvc
    public static CardInfo getEmptyCvcUser() {
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(),generateValidYear(),
                generateOwnerName("en"), 0);
    }

    //дата класс для предоставления информации о карте и ее держателе
    @Value
    public static class CardInfo {
        @JsonProperty("number")
        String cardNumber;
        @JsonProperty("month")
        String month;
        @JsonProperty("year")
        String year;
        @JsonProperty("holder")
        String owner;
        @JsonProperty("cvc")
        int cvc;
    }
}

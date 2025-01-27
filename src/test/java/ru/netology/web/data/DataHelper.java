package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.YearMonth;
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

    public static String generateInvalidCardNumber() {
        Random random = new Random();

        // Сгенерировать число цифр (от 1 до 15)
        int numDigits = random.nextInt(15) + 1;

        // Построить string из этих цифр
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < numDigits; i++) {
            int digit = random.nextInt(10); // Сгенерировать цифру (0-9)

            number.append(digit);
        }

        return number.toString();
    }

    //Месяц и Год: сначала нужна полностью валидная дата, далее истекший месяц, невалидный месяц, истекший год, невалидный год

    public static int getCurrentYear() {
        return YearMonth.now().getYear();
    }

    public static String getCurrentMonth() {
        int currentMonth = YearMonth.now().getMonthValue();
        return String.format("%02d", currentMonth);
    }

    // Метод для генерации валидного года
    public static String generateValidYear() {
        Random random = new Random();

        // Сгенерировать год в диапазоне от текущего до +5 лет
        int fullYear = getCurrentYear() + random.nextInt(6); // Генерация года (от текущего до +5 лет)

        // Извлечь последние две цифры с помощью % 100 и отформатировать
        return String.format("%02d", fullYear % 100);
    }


    public static String generateValidMonth(String year) {
        Random random = new Random();

        // Преобразуем строку year в int
        int numericYear = Integer.parseInt(year);

        // Преобразуем текущий месяц из строки в int
        int currentMonth = Integer.parseInt(getCurrentMonth());
        int month;

        if (numericYear == getCurrentYear()) {
            // Если год совпадает с текущим, выбрать текущий или будущий месяц
            month = currentMonth + random.nextInt(13 - currentMonth); // (currentMonth..12)
        } else {
            // Если год не текущий, выбрать любой месяц (1..12)
            month = random.nextInt(12) + 1;
        }

        // Возвращаем форматированное значение с ведущим нулём
        return String.format("%02d", month);
    }


    // Метод для генерации истекшего месяца на основе текущего года
    public static String generateExpiredMonth() {
        // Преобразуем текущий месяц из строки в int
        int currentMonth = Integer.parseInt(getCurrentMonth());
        int expiredMonth;

        if (currentMonth == 1) {
            // Если текущий месяц - январь, то истекший месяц - декабрь прошлого года
            expiredMonth = 12;
        } else {
            // Для других месяцев просто вычитаем 1
            expiredMonth = currentMonth - 1;
        }

        // Возвращаем результат в формате "MM" с ведущим нулём
        return String.format("%02d", expiredMonth);
    }

    //МЕНЯТЬ ГОД НА ПРОШЛЫЙ, ЕСЛИ СЕЙЧАС ЯНВАРЬ (для проверок на истекший месяц)
    public static String getFalseOrTrueCurrentYear(String month) {
        int yearToReturn;

        // Проверяем, является ли месяц "01" (январь)
        if (month.equals("01")) {
            yearToReturn = getCurrentYear() - 1; // Прошлый год
        } else {
            yearToReturn = getCurrentYear(); // Текущий год
        }

        // Возвращаем последние две цифры года
        return String.format("%02d", yearToReturn % 100);
    }

    // Метод для генерации невалидного месяца
    public static String generateInvalidMonth() {
        Random random = new Random();
        int invalidMonth = random.nextInt(87) + 13; // Генерация числа от 13 до 99
        return String.valueOf(invalidMonth); // Преобразование числа в строку
    }

    //Метод генерации истекшего года
    public static String generateExpiredYear() {
        Random random = new Random();
        int expiredYear = random.nextInt(getCurrentYear() - 2000) + 2000; // Случайный год от 2000 до (currentYear - 1)
        return String.format("%02d", expiredYear % 100); // Преобразование в две последние цифры
    }

    // Метод для генерации невалидного года
    public static String generateInvalidYear() {
        Random random = new Random();
        int startYear = getCurrentYear() + 21; // Начинаем с +21 года
        int invalidYear = random.nextInt(100 - (startYear % 100)) + (startYear % 100); // Генерируем в пределах текущего столетия
        return String.valueOf(invalidYear); // Возвращаем только последние две цифры
    }

    //Метод для герерации имени
    public static String generateOwnerName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    //CVC может быть валидным и невалидным

    // Метод генерации валидного CVC
    public static int generateValidCVC() {
        Random random = new Random();
        return random.nextInt(900) + 100; // Генерирует число от 100 до 999
    }

    // Метод генерации невалидного CVC
    public static int generateInvalidCVC() {
        Random random = new Random();
        return random.nextInt(90) + 10; // Генерирует число от 10 до 99
    }

    //валидный юзер
    public static CardInfo getValidUser() {
        String validYear = generateValidYear();
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(validYear), validYear,
                generateOwnerName("en"), generateValidCVC());
    }

    //юзер с declined картой
    public static CardInfo getDeclinedCardUser() {
        String validYear = generateValidYear();
        return new CardInfo(getDeclinedCardNumber(),
                generateValidMonth(validYear), validYear,
                generateOwnerName("en"), generateValidCVC());
    }

    //создать юзера с полностью невалидными форматами данных
    public static CardInfo getInvalidInfoUser() {
        return new CardInfo(generateInvalidCardNumber(),
                generateInvalidMonth(), generateInvalidYear(),
                generateOwnerName("ru"), generateInvalidCVC());
    }

    //создать юзера с истекшим месяцем
    public static CardInfo getExpiredMonthUser() {
        String currentMonth = String.valueOf(getCurrentMonth());
        return new CardInfo(getApprovedCardNumber(),
                generateExpiredMonth(), getFalseOrTrueCurrentYear(currentMonth),
                generateOwnerName("en"), generateValidCVC());
    }

    //создать юзера с истекшим годом
    public static CardInfo getExpiredYearUser() {
        String validYear = generateValidYear();
        return new CardInfo(getApprovedCardNumber(),
                generateValidMonth(validYear),generateExpiredYear() ,
                generateOwnerName("en"), generateValidCVC());
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

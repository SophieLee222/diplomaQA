# Отчёт по итогам тестирования

## 1. Краткое описание

Проведено тестирование функционала отправки формы с банковскими картами. В тестировании использовались API- и UI-тесты, направленные на проверку корректности обработки данных и отображения ошибок.

## 2. Количество тест-кейсов

Общее количество тест-кейсов: **16**

## 3. Процент успешных и неуспешных тест-кейсов

- **Успешные тест-кейсы:** 13
- **Неуспешные тест-кейсы:** 3
- **Процент успешных тестов:** 81.25%
- **Процент неуспешных тестов:** 18.75%
  
<img width="1469" alt="AllureFullReport" src="https://github.com/user-attachments/assets/df3a3d4e-b530-4e13-9a8c-97e70abb2433" />

<img width="576" alt="AllFailedTests" src="https://github.com/user-attachments/assets/5d97338e-1d3f-4e06-b0a5-e8879e64eb36" />

## 4. Общие рекомендации

- Проверить корректность обработки статусов карт в UI.
- Проверить тексты ошибок при неверном вводе данных в форму.
- Проверить обработку данных формы в соответствии с требованиями
- Перезапустить тестирование после внесения исправлений и убедиться в успешном прохождении всех тест-кейсов.

### Детализация ошибок:

- **Should Display Declined Card Notification and receive DECLINED status** - [Issue #1](https://github.com/SophieLee222/diplomaQA/issues/1)
  - Поменять уведомление об успешном списании на уведомление об отказе списания.
 
    <img width="1290" alt="DeclinedStatus1" src="https://github.com/user-attachments/assets/a254bc66-46c1-4f6d-af5d-a7fe232392de" />
    <img width="1290" alt="DeclinedStatus2" src="https://github.com/user-attachments/assets/857852fe-c8cf-467d-8892-02e031c4f83c" />
    
- **Should Display Expired Month Error** - [Issue #2](https://github.com/SophieLee222/diplomaQA/issues/2)
  - Исправить текст ошибки об истекшем месяце карты.
 
    <img width="1291" alt="ExpiredMonth1" src="https://github.com/user-attachments/assets/f6e09f71-7bbe-480b-bafa-c068c8af8354" />
    <img width="1292" alt="ExpiredMonth2" src="https://github.com/user-attachments/assets/e93be681-570b-4113-a653-22080bc52a36" />
    
- **Should Display Wrong Owner Format Error** - [Issue #3](https://github.com/SophieLee222/diplomaQA/issues/3)
  - Исправить успешную обработку формы с невалидным именем на соответствующую ошибку.
 
    <img width="1291" alt="WrongOwnerFormat1" src="https://github.com/user-attachments/assets/b55e6dc7-e4aa-407f-a139-0ddd1d424cda" />
    <img width="1288" alt="WrongOwnerFormat2" src="https://github.com/user-attachments/assets/b8b11b8a-6471-403a-afee-c8d70decc484" />

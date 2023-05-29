# kpo-ihv4

Для реализации данной работы были использованы Java 20 и  Spring 3.1.0. В качестве базы данных была выбрана PostgreSQL, понравившаяся мне после курсовой работы. 
Подробную информацию о БД можно найти в файле [`application.properties`](https://github.com/KcasTischaWattt/kpo-ihv4/blob/main/src/main/resources/application.properties). Для обеспечения удобства тестирования, база данных очищается при каждом запуске программы.

Тестировал с помощью Postman. Реализовать мне удалось лишь регистрацию пользователя, так что вариативность запросов была значительно уже, чем если бы был реализован весь фенкционал.

Сценарий таков: сперва необходимо зарегестрироваться, далее войти в аккаунт, после чего взять токен, и уже с его помощью получить информацию о пользователе.

Внимание! Токен заканчивает своё действие спустя 10 минут.

## Как тестировать:

### 1. Регистрация

Сперва пользователя нудно зарегестировать. Для этогонужно сделать следующие шаги

> * POST `localhost:8080/api/user/register`
>

``` json
{
    "username": "Admin",
    "email": "Admin@hse.ru",
    "passwordHash": "secret",
    "role": "admin"
}
```

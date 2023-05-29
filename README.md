# kpo-ihv4

Для реализации данной работы были использованы Java 20 и  Spring 3.1.0. В качестве базы данных была выбрана PostgreSQL, понравившаяся мне после курсовой работы. 
Подробную информацию о БД можно найти в файле [`application.properties`](https://github.com/KcasTischaWattt/kpo-ihv4/blob/main/src/main/resources/application.properties). Для обеспечения удобства тестирования, база данных очищается при каждом запуске программы.

Тестировал с помощью Postman. Реализовать мне удалось лишь регистрацию пользователя, так что вариативность запросов была значительно уже, чем если бы был реализован весь фенкционал.

Сценарий таков: сперва необходимо зарегестрироваться, далее войти в аккаунт, после чего взять токен, и уже с его помощью получить информацию о пользователе.

Внимание! Токен заканчивает своё действие спустя 10 минут.

## Как тестировать:

### 1. Регистрация

Сперва пользователя нудно зарегестировать. Для этого нужно сделать следующие шаги:

 * POST `localhost:8080/api/user/register`
 * В *body* выбираем *raw*, далее *json*, и вводим данные в следующем виде:

``` json
{
    "username": "Admin",
    "email": "Admin@hse.ru",
    "passwordHash": "secret",
    "role": "admin"
}
```
### 2. Авторизация

После этого у пользователя появляется возможность войти в аккаунт. Делается это так:

 * GET `localhost:8080/api/user/login`
 * В *params* ставим `key: username; value: Admin`, `key: password; value: secret`

### 3. Получение информации о пользователе

После успешной авторизации будет возвращён токен. Его можно использовать для полученя информации о пользователе следующим образом:

 * GET `localhost:8080/api/user/get_info`
 * В *authorization* выбираем *bearer token*, и вставляем токен

Ура - мы получили информацию о пользователе!

В [`postman` коллекции](https://github.com/KcasTischaWattt/kpo-ihv4/blob/main/Postman%20Tests.postman_collection.json), приложенной к проекту, есть примеры этих запросов. Не все они будут работать сразу, некторые вообще не будут из-за особенностей генерации токенов, но если создать собственные запросы по приведённой выше инструкции, то всё должно прекрасно работать.



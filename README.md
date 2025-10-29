Проект: Hotel Service + Booking Service  
Технологии: Spring Boot 3.5.7, Spring Security, JWT, H2, JPA, Lombok, Eureka, Spring Cloud Gateway  
Описание: Микросервисная система для управления отелями и бронированиями с аутентификацией через JWT и централизованным сервис-дискавери через Eureka.

---

1. Архитектура проекта

[Client] --> [Gateway] --> [Booking Service] --> [Hotel Service]
-> [Eureka Server]


- **Eureka Server** – централизованное обнаружение сервисов (порт 8761)
- **Gateway** – маршрутизация запросов, проверка JWT
- **Booking Service** – управление бронированиями
- **Hotel Service** – управление отелями и номерами
- **JWT** – аутентификация и авторизация пользователей

---

2. Запуск проектов

2.1 Требования
- JDK 17+
- Maven
- IntelliJ IDEA / Eclipse / VS Code

2.2 Порядок запуска
1. Запустить **Eureka Server** (`http://localhost:8761`)
2. Запустить **Hotel Service** (`http://localhost:8082`)
3. Запустить **Booking Service** (`http://localhost:8081`)
4. Gateway (если есть) — проверка маршрутизации запросов

> Все сервисы автоматически регистрируются в Eureka.


## 3. Конфигурация

### 3.1 Booking Service (`application.properties`)
```properties
spring.application.name=booking-service
server.port=8081

eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
3.2 Hotel Service (application.properties)
properties
Копировать код
spring.application.name=hotel-service
server.port=8082

eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
4. Endpoints
4.1 Пользователи (Booking Service)
Метод	URL	Описание
POST	/user/save	Создать пользователя
POST	/user/auth	Аутентификация, возвращает JWT
GET	/user/all	Получить всех пользователей (для теста)
GET	/user/{username}	Получить пользователя по username
DELETE	/user/delete/{id}	Удалить пользователя

4.2 Бронирования (Booking Service)
Метод	URL	Описание
POST	/booking/create	Создать бронирование (проверка доступности номера)
GET	/booking/all	Список всех бронирований
GET	/booking/{id}	Получить бронирование по ID
DELETE	/booking/{id}	Отмена бронирования

4.3 Отели и номера (Hotel Service)
Метод	URL	Описание
POST	/hotel/save	Создать отель
POST	/room/save	Создать номер в отеле
GET	/hotel/all	Список отелей
GET	/room/all	Список номеров
GET	/room/{id}	Получить номер по ID

5. Аутентификация JWT
Создать пользователя:

bash
Копировать код
POST http://localhost:8081/user/save
{
  "username": "admin",
  "password": "12345",
  "role": "ADMIN"
}
Получить JWT:

bash
Копировать код
POST http://localhost:8081/user/auth
{
  "username": "admin",
  "password": "12345"
}
Ответ:

json
Копировать код
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
Использовать токен для доступа к защищённым эндпоинтам:

makefile
Копировать код
Authorization: Bearer <token>
6. Пример сценария бронирования
Создать отель и номера через Hotel Service

Создать пользователя через Booking Service

Получить JWT токен

Создать бронирование:

bash
Копировать код
POST http://localhost:8081/booking/create
Authorization: Bearer <token>
{
  "userId": 1,
  "roomId": 2,
  "dateFrom": "2025-11-01",
  "dateTo": "2025-11-05"
}
Проверка состояния бронирования: PENDING → CONFIRMED (сага с компенсацией при ошибках)

7. H2 Database
H2 консоль Booking Service: http://localhost:8081/h2-console

H2 консоль Hotel Service: http://localhost:8082/h2-console

JDBC URL: jdbc:h2:mem:testdb

Username/Password: по умолчанию

8. Eureka Server
Просмотр зарегистрированных сервисов: http://localhost:8761

Все сервисы автоматически регистрируются и доступны для discovery

9. Тестирование
Проверка создания пользователя и получения JWT

Проверка CRUD бронирований и отелей

Проверка саги бронирования: отмена при конфликте или недоступности номера

Проверка авторизации: доступ к защищённым эндпоинтам только с JWT

10. Рекомендации
Пароли пользователей хранятся plain text (демо). Для боевого проекта использовать BCryptPasswordEncoder.

JWT токен действителен 1 час.

Можно расширить функционал Gateway с фильтрами безопасности, логированием и трассировкой запросов.
Создать Dockerfile для каждого сервиса

Собрать и запустить контейнеры через docker-compos

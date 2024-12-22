# Server NewsApp Backend

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.4-brightgreen.svg)

## Описание

**Server NewsApp Backend** — это бэкенд часть приложения для просмотра новостей, реализованная на базе Spring Boot. Приложение предоставляет RESTful API для аутентификации пользователей, управления сообществами, новостями, комментариями и взаимодействия пользователей с сообществами.

## Структура проекта

```
server_newsapp/
├── src/
│   ├── main/
│   │   ├── java/com/logic/server_newsapp/
│   │   │   ├── controllers/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CommunityController.java
│   │   │   │   ├── CommentsController.java
│   │   │   │   ├── NewsController.java
│   │   │   │   ├── UserCommunityController.java
│   │   │   │   └── UsersController.java
│   │   │   ├── models/
│   │   │   ├── services/
│   │   │   └── jwt/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## Технологии

- **Java 11+**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Maven**
- **Log4j2**
- **PostgreSQL** (или другая СУБД по выбору)

## Установка

1. **Клонируйте репозиторий:**

   ```bash
   git clone https://github.com/ваше-имя-пользователя/server_newsapp.git
   cd server_newsapp
   ```

2. **Настройте базу данных:**

   Убедитесь, что у вас установлена и настроена база данных (например, PostgreSQL). Создайте базу данных и обновите параметры подключения в файле `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/newsapp
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Установите зависимости Maven:**

   ```bash
   mvn clean install
   ```

## Запуск приложения

Для запуска приложения используйте Maven:

```bash
mvn spring-boot:run
```

Приложение будет доступно по умолчанию на [http://localhost:8080](http://localhost:8080).

## API Документация

### Аутентификация

#### Регистрация пользователя

- **URL:** `/register`
- **Метод:** `POST`
- **Описание:** Регистрирует нового пользователя и возвращает JWT токен.
- **Параметры:**
  - `User` объект в формате JSON.

- **Пример запроса:**

  ```json
  {
    "login": "user123",
    "password": "password",
    "email": "user@example.com"
  }
  ```

- **Пример ответа:**

  ```json
  {
    "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
  }
  ```

#### Вход пользователя

- **URL:** `/login`
- **Метод:** `POST`
- **Описание:** Аутентифицирует пользователя и возвращает JWT токен.
- **Параметры:**
  - `User` объект в формате JSON.

- **Пример запроса:**

  ```json
  {
    "login": "user123",
    "password": "password"
  }
  ```

- **Пример ответа при успешной аутентификации:**

  ```json
  {
    "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
  }
  ```

- **Пример ответа при неудачной аутентификации:**

  ```json
  {
    "error": "Invalid credentials"
  }
  ```

### Управление сообществами

#### Создание или обновление сообщества

- **URL:** `/api/community`
- **Метод:** `POST`
- **Описание:** Создает новое сообщество или обновляет существующее.
- **Параметры:**
  - `Community` объект в формате JSON.

- **Пример запроса:**

  ```json
  {
    "nameCommunity": "Tech News",
    "description": "Latest updates in technology."
  }
  ```

- **Пример ответа:**

  ```json
  {
    "id": 1,
    "nameCommunity": "Tech News",
    "description": "Latest updates in technology."
  }
  ```

#### Получение всех сообществ

- **URL:** `/api/community`
- **Метод:** `GET`
- **Описание:** Возвращает список всех сообществ.

- **Пример ответа:**

  ```json
  [
    {
      "id": 1,
      "nameCommunity": "Tech News",
      "description": "Latest updates in technology."
    },
    {
      "id": 2,
      "nameCommunity": "Sports",
      "description": "All about sports."
    }
  ]
  ```

#### Получение сообщества по ID

- **URL:** `/api/community/{id}`
- **Метод:** `GET`
- **Описание:** Возвращает сообщество по указанному ID.

- **Пример ответа:**

  ```json
  {
    "id": 1,
    "nameCommunity": "Tech News",
    "description": "Latest updates in technology."
  }
  ```

#### Удаление сообщества по ID

- **URL:** `/api/community/{id}`
- **Метод:** `DELETE`
- **Описание:** Удаляет сообщество по указанному ID.

- **Пример ответа:**
  - **204 No Content** при успешном удалении.

### Управление новостями

#### Получение всех новостей

- **URL:** `/api/news`
- **Метод:** `GET`
- **Описание:** Возвращает список всех новостей.

#### Получение всех новостей, отсортированных по новизне

- **URL:** `/api/news/date/new`
- **Метод:** `GET`
- **Описание:** Возвращает список новостей, отсортированных по дате от новых к старым.

#### Получение всех новостей, отсортированных по давности

- **URL:** `/api/news/date/old`
- **Метод:** `GET`
- **Описание:** Возвращает список новостей, отсортированных по дате от старых к новым.

#### Получение новости по ID

- **URL:** `/api/news/{id}`
- **Метод:** `GET`
- **Описание:** Возвращает новость по указанному ID.

#### Получение новостей по названию

- **URL:** `/api/news/name/{name}`
- **Метод:** `GET`
- **Описание:** Возвращает список новостей по заданному названию.

#### Создание новой новости

- **URL:** `/api/news`
- **Метод:** `POST`
- **Описание:** Создает новую новость.
- **Параметры:**
  - `News` объект в формате JSON.
  - `communityName` — название сообщества (параметр запроса).

- **Пример запроса:**

  ```
  POST /api/news?communityName=Tech%20News
  ```

  ```json
  {
    "title": "New Java Release",
    "content": "Java 17 has been released with new features..."
  }
  ```

- **Пример ответа:**

  ```json
  {
    "id": 10,
    "title": "New Java Release",
    "content": "Java 17 has been released with new features...",
    "publishDate": "2024-12-22T10:15:30",
    "community": {
      "id": 1,
      "nameCommunity": "Tech News",
      "description": "Latest updates in technology."
    }
  }
  ```

#### Обновление новости

- **URL:** `/api/news/{id}`
- **Метод:** `PUT`
- **Описание:** Обновляет существующую новость по ID.
- **Параметры:**
  - `id` — ID новости.
  - `updatedNews` — объект с обновленными данными.

#### Удаление новости

- **URL:** `/api/news/{id}`
- **Метод:** `DELETE`
- **Описание:** Удаляет новость по указанному ID.

### Управление комментариями

#### Создание или обновление комментария

- **URL:** `/api/comments`
- **Метод:** `POST`
- **Описание:** Создает новый комментарий или обновляет существующий.
- **Параметры:**
  - `Comments` объект в формате JSON.

#### Получение всех комментариев

- **URL:** `/api/comments`
- **Метод:** `GET`
- **Описание:** Возвращает список всех комментариев.

#### Получение комментария по ID

- **URL:** `/api/comments/{id}`
- **Метод:** `GET`
- **Описание:** Возвращает комментарий по указанному ID.

#### Удаление комментария по ID

- **URL:** `/api/comments/{id}`
- **Метод:** `DELETE`
- **Описание:** Удаляет комментарий по указанному ID.

### Управление пользователями и сообществами

#### Подписка пользователя на сообщество

- **URL:** `/userCommunity`
- **Метод:** `POST`
- **Описание:** Подписывает пользователя на сообщество.
- **Параметры:**
  - `login` — логин пользователя (параметр запроса).
  - `nameCommunity` — название сообщества (параметр запроса).

#### Получение пользователей по сообществу

- **URL:** `/userCommunity`
- **Метод:** `GET`
- **Описание:** Возвращает список пользователей, подписанных на указанное сообщество.
- **Параметры:**
  - `nameCommunity` — название сообщества (параметр запроса).

### Управление пользователями

#### Получение всех пользователей

- **URL:** `/users`
- **Метод:** `GET`
- **Описание:** Возвращает список всех пользователей.

#### Получение роли пользователя по логину

- **URL:** `/users/role/{login}`
- **Метод:** `GET`
- **Описание:** Возвращает роль пользователя по заданному логину.

#### Получение пользователя по ID

- **URL:** `/users/{id}`
- **Метод:** `GET`
- **Описание:** Возвращает пользователя по указанному ID.

#### Получение пользователя по логину

- **URL:** `/users/login/{login}`
- **Метод:** `GET`
- **Описание:** Возвращает пользователя по заданному логину.

#### Создание нового пользователя

- **URL:** `/users`
- **Метод:** `POST`
- **Описание:** Создает нового пользователя.
- **Параметры:**
  - `User` объект в формате JSON.

#### Обновление роли пользователя до EDITOR

- **URL:** `/users/updateUserRoleEditor`
- **Метод:** `PUT`
- **Описание:** Обновляет роль пользователя до EDITOR при наличии прав ADMIN.
- **Параметры:**
  - `login` — логин пользователя (тело запроса).
  - `role` — текущая роль пользователя (тело запроса).

#### Обновление роли пользователя до ADMIN

- **URL:** `/users/updateUserRoleAdmin`
- **Метод:** `PUT`
- **Описание:** Обновляет роль пользователя до ADMIN при наличии прав ADMIN.
- **Параметры:**
  - `login` — логин пользователя (тело запроса).
  - `role` — текущая роль пользователя (тело запроса).

#### Обновление пользователя

- **URL:** `/users/{id}`
- **Метод:** `PUT`
- **Описание:** Обновляет данные пользователя по указанному ID.
- **Параметры:**
  - `id` — ID пользователя.
  - `updatedUser` — объект с обновленными данными пользователя.

#### Удаление пользователя

- **URL:** `/users/{id}`
- **Метод:** `DELETE`
- **Описание:** Удаляет пользователя по указанному ID.

## Примеры использования

### Регистрация пользователя

```bash
curl -X POST http://localhost:8080/register \
-H "Content-Type: application/json" \
-d '{
  "login": "user123",
  "password": "password",
  "email": "user@example.com"
}'
```

### Вход пользователя

```bash
curl -X POST http://localhost:8080/login \
-H "Content-Type: application/json" \
-d '{
  "login": "user123",
  "password": "password"
}'
```

### Получение всех новостей

```bash
curl -X GET http://localhost:8080/api/news
```

## Вклад

Если вы хотите внести свой вклад в проект, пожалуйста, создайте [issue](https://github.com/ваше-имя-пользователя/server_newsapp/issues) или отправьте [pull request](https://github.com/ваше-имя-пользователя/server_newsapp/pulls).

## Лицензия

Этот проект лицензирован под лицензией MIT. Подробности см. в [LICENSE](LICENSE).

# Дополнительная информация

Для более детальной информации о каждом контроллере и сервисе, пожалуйста, обратитесь к исходному коду проекта.

# Контакты

Если у вас есть вопросы или предложения, свяжитесь с нами по [email@example.com](mailto:email@example.com).

---

**Приятной работы с Server NewsApp Backend!**

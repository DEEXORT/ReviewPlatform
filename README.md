### Файл `README.md`

# ⚡ Terminal Review Platform

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.9-green?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=for-the-badge&logo=docker)

Интерактивная платформа для обмена отзывами о IT-инструментах или других вещах, выполненная в визуальном стиле киберпанк-терминала. 

<img width="1679" height="831" alt="image" src="https://github.com/user-attachments/assets/e5dac8d7-9d1d-464a-affd-95b51142af59" />

## 🚀 Быстрый запуск (Docker Compose)

Самый простой способ развернуть систему — использовать Docker. Система автоматически поднимет базу данных PostgreSQL и само приложение.

1. **Запуск всей инфраструктуры:**
   ```bash
   docker-compose up -d --build
   ```

2. **Просмотр логов в реальном времени:**
   ```bash
   docker logs -f review_platform
   ```


3. **Доступ к интерфейсу:** Откройте [http://localhost:8080](https://www.google.com/search?q=http://localhost:8080)
4. **Вход в систему (Default Users):**
Используйте следующие учетные данные для тестирования уровней доступа:

| Роль | Логин | Пароль | Описание |
| --- | --- | --- | --- |
| **Администратор** | `admin` | `admin` | Полный доступ, удаление любых данных. |
| **Менеджер** | `manager` | `manager` | Расширенный доступ к управлению контентом. |
| **Пользователь** | `cyber_user` | `cyber_user` | Создание и правка собственных отзывов. |

---

## 🛠 Технологический стек

* **Backend:** Java 21 / Spring Boot 3.5
* **Database:** PostgreSQL 15
* **Migrations:** Liquibase (автоматическое наполнение данными)
* **Security:** Spring Security (RBAC: User, Admin)
* **Frontend:** Thymeleaf + Bootstrap 5 (Custom CSS)

---

## ⚙️ Локальная настройка (без Docker)

Если вы запускаете проект напрямую через IDE:

1. Создайте базу данных PostgreSQL с именем `project`.
2. В файле `src/main/resources/application.yml` укажите ваши `username` и `password`.
3. Соберите и запустите проект:
```bash
mvn clean spring-boot:run

```



---

## 🛡 Безопасность и Роли

Система поддерживает два уровня доступа:

* **USER**: Может создавать отзывы, редактировать и удалять только свои записи.
* **ADMIN**: Имеет доступ к удалению любых логов в системе для модерации.

---

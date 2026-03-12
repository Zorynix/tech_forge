# Tech Forge — Маркетплейс электроники

Полнофункциональный маркетплейс электроники, построенный на микросервисной архитектуре с **Spring Boot 3** бэкендом и **Next.js 16** фронтендом.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green?logo=spring-boot)
![Next.js](https://img.shields.io/badge/Next.js-16-black?logo=next.js)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)

---

## Архитектура

```
┌──────────────┐     ┌──────────────────┐     ┌──────────────────┐
│   Frontend   │────▶│   API Gateway    │────▶│ Product Service  │
│  (Next.js)   │     │     :8080        │     │     :8081        │
│   :3000      │     └──────────────────┘     └──────────────────┘
└──────────────┘              │
                              ├───────────▶ Auth Service    :8082
                              ├───────────▶ Order Service   :8083
                              └───────────▶ Notification    :8084
                                            Service

         ┌────────────┐  ┌────────────┐  ┌────────────┐
         │ PostgreSQL  │  │ Dragonfly  │  │  Redpanda  │
         │   :5433     │  │  (Redis)   │  │  (Kafka)   │
         │             │  │   :6379    │  │  :19092    │
         └────────────┘  └────────────┘  └────────────┘
```

### Сервисы

| Сервис | Порт | Описание |
|--------|------|----------|
| **api-gateway** | 8080 | Spring Cloud Gateway — маршрутизация, rate-limiting, CORS |
| **product-service** | 8081 | Каталог товаров, категории, отзывы и рейтинги |
| **auth-service** | 8082 | Регистрация, аутентификация, JWT-токены, email-подтверждение |
| **order-service** | 8083 | Управление заказами, интеграция с Kafka |
| **notification-service** | 8084 | Уведомления: email (SMTP) и Telegram Bot |
| **frontend** | 3000 | Next.js SPA — каталог, корзина, профиль, заказы |

### Технологический стек

**Backend:**
- Java 21 (GraalVM), Spring Boot 3.4.3
- Гексагональная архитектура (Ports & Adapters)
- PostgreSQL 17 + Flyway миграции
- Dragonfly (Redis-совместимый кэш)
- Redpanda (Kafka-совместимый брокер сообщений)
- JWT аутентификация (JJWT 0.12.6)

**Frontend:**
- Next.js 16, React 19, TypeScript
- Tailwind CSS v4 + glassmorphism UI
- shadcn/ui (на базе @base-ui/react)
- Zustand (state management)
- Framer Motion (анимации)
- Three.js / React Three Fiber (3D)

**Инфраструктура:**
- Docker Compose (PostgreSQL, Dragonfly, Redpanda, Redpanda Console)

---

## Быстрый старт

### Предварительные требования

- **Java 21** (рекомендуется GraalVM)
- **Node.js 20+** и **npm**
- **Docker** и **Docker Compose**

### 1. Запуск инфраструктуры

```bash
docker compose up -d
```

Будут подняты:
- PostgreSQL — `localhost:5433`
- Dragonfly (Redis) — `localhost:6379`
- Redpanda (Kafka) — `localhost:19092`
- Redpanda Console — `localhost:8180`

### 2. Настройка переменных окружения

Скопируйте `.env.example` в `.env` и заполните реальные значения:

```bash
cp .env.example .env
```

> Для разработки значения по умолчанию из `.env.example` уже подходят для работы с Docker Compose. Нужно заполнить только `TELEGRAM_BOT_TOKEN`, `MAIL_USERNAME` и `MAIL_PASSWORD` если требуются уведомления.

### 3. Запуск бэкенда

```bash
# Сборка всех сервисов
./gradlew build

# Запуск каждого сервиса (в отдельных терминалах)
./gradlew :api-gateway:bootRun
./gradlew :product-service:bootRun
./gradlew :auth-service:bootRun
./gradlew :order-service:bootRun
./gradlew :notification-service:bootRun
```

### 4. Запуск фронтенда

```bash
cd frontend
cp .env.example .env.local
npm install
npm run dev
```

Приложение будет доступно по адресу: **http://localhost:3000**

---

## Переменные окружения

| Переменная | Описание | По умолчанию |
|---|---|---|
| `DB_HOST` | Хост PostgreSQL | `localhost` |
| `DB_PORT` | Порт PostgreSQL | `5433` |
| `DB_NAME` | Имя базы данных | `techmarket` |
| `DB_USERNAME` | Пользователь БД | `techmarket` |
| `DB_PASSWORD` | Пароль БД | `techmarket_secret` |
| `REDIS_HOST` | Хост Dragonfly/Redis | `localhost` |
| `REDIS_PORT` | Порт Dragonfly/Redis | `6379` |
| `KAFKA_BOOTSTRAP_SERVERS` | Адрес Redpanda/Kafka | `localhost:19092` |
| `JWT_SECRET` | Секретный ключ JWT (мин. 32 символа) | — |
| `FRONTEND_ORIGIN` | URL фронтенда для CORS | `http://localhost:3000` |
| `MAIL_USERNAME` | Email для SMTP | — |
| `MAIL_PASSWORD` | App Password для SMTP | — |
| `TELEGRAM_BOT_TOKEN` | Токен Telegram бота | — |
| `TELEGRAM_BOT_USERNAME` | Username Telegram бота | — |
| `AUTH_SERVICE_URL` | URL auth-service | `http://localhost:8082` |
| `NEXT_PUBLIC_API_URL` | URL API для фронтенда | `http://localhost:8080/api/v1` |

---

## Структура проекта

```
tech_market/
├── api-gateway/          # API Gateway (Spring Cloud Gateway)
├── auth-service/         # Сервис аутентификации
├── product-service/      # Сервис товаров и отзывов
├── order-service/        # Сервис заказов
├── notification-service/ # Сервис уведомлений
├── frontend/             # Next.js приложение
├── infrastructure/       # SQL-инициализация БД
├── docker-compose.yml    # Инфраструктура в Docker
├── build.gradle          # Корневой Gradle build
├── settings.gradle       # Gradle multi-module конфигурация
└── .env.example          # Шаблон переменных окружения
```

Каждый бэкенд-сервис следует **гексагональной архитектуре**:

```
service/src/main/java/com/techmarket/*/
├── domain/
│   ├── model/          # Доменные сущности
│   ├── port/
│   │   ├── in/         # Входящие порты (use-cases)
│   │   └── out/        # Исходящие порты (репозитории)
│   └── service/        # Доменная логика
├── adapter/
│   ├── in/
│   │   └── web/        # REST контроллеры
│   └── out/
│       └── persistence/# JPA репозитории, маппинг
└── config/             # Spring конфигурация
```

---

## API Endpoints

Все запросы идут через API Gateway (`localhost:8080`):

- `POST /api/v1/auth/register` — Регистрация
- `POST /api/v1/auth/login` — Вход
- `GET  /api/v1/products` — Список товаров (пагинация, сортировка)
- `GET  /api/v1/products/{id}` — Товар по ID
- `GET  /api/v1/products/search?query=` — Поиск
- `GET  /api/v1/categories` — Категории
- `POST /api/v1/products/{id}/reviews` — Добавить отзыв
- `GET  /api/v1/products/{id}/reviews` — Отзывы товара
- `POST /api/v1/orders` — Создать заказ
- `GET  /api/v1/orders` — Мои заказы

---

## Лицензия

Этот проект создан в учебных целях.

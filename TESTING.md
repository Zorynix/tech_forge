# Тестирование Tech Forge

## Требования

- **Docker** — для Testcontainers (PostgreSQL, Redis, Kafka)
- **Java 21**
- **k6** — для нагрузочного тестирования (`brew install k6` / `snap install k6`)

---

## Структура тестов

| Сервис                | Unit-тесты | Интеграционные тесты | Testcontainers |
|-----------------------|------------|----------------------|----------------|
| auth-service          | 3          | 3                    | PostgreSQL, Redis |
| product-service       | 3          | 2                    | PostgreSQL, Redis |
| order-service         | 4          | 3                    | PostgreSQL, Redis, Kafka |
| notification-service  | 2          | 2                    | PostgreSQL, Redis, Kafka |

Нагрузочные тесты: `load-tests/` (k6)

---

## Запуск тестов

### Все тесты всех сервисов

```bash
./gradlew test
```

### Тесты конкретного сервиса

```bash
./gradlew :auth-service:test
./gradlew :product-service:test
./gradlew :order-service:test
./gradlew :notification-service:test
```

### Только unit-тесты (без Testcontainers, быстро)

Unit-тесты не наследуют `BaseIntegrationTest` и не требуют Docker:

```bash
./gradlew :auth-service:test --tests "*.application.service.*"
./gradlew :product-service:test --tests "*.application.service.*"
./gradlew :order-service:test --tests "*.application.service.*" --tests "*.domain.model.*"
./gradlew :notification-service:test --tests "*.application.service.*"
```

### Только интеграционные тесты

```bash
./gradlew :auth-service:test --tests "*IntegrationTest"
./gradlew :product-service:test --tests "*IntegrationTest"
./gradlew :order-service:test --tests "*IntegrationTest"
./gradlew :notification-service:test --tests "*IntegrationTest"
```

### Конкретный тест-класс

```bash
./gradlew :auth-service:test --tests "com.techmarket.authservice.adapter.in.web.AuthControllerIntegrationTest"
```

### Конкретный тест-метод

```bash
./gradlew :order-service:test --tests "*.OrderControllerIntegrationTest.fullOrderFlow"
```

---

## Отчёты о покрытии (JaCoCo)

JaCoCo отчёты генерируются автоматически после каждого `test`.

### Покрытие по сервису

```bash
./gradlew :auth-service:test
# HTML-отчёт: auth-service/build/reports/jacoco/test/html/index.html

./gradlew :product-service:test
# HTML-отчёт: product-service/build/reports/jacoco/test/html/index.html

./gradlew :order-service:test
# HTML-отчёт: order-service/build/reports/jacoco/test/html/index.html

./gradlew :notification-service:test
# HTML-отчёт: notification-service/build/reports/jacoco/test/html/index.html
```

### Агрегированный отчёт (все сервисы)

```bash
./gradlew jacocoAggregatedReport
# HTML-отчёт: build/reports/jacoco/aggregated/index.html
```

### Проверка минимального покрытия (60%)

```bash
./gradlew jacocoTestCoverageVerification
```

### Открыть отчёт в браузере

```bash
# Linux
xdg-open auth-service/build/reports/jacoco/test/html/index.html

# macOS
open auth-service/build/reports/jacoco/test/html/index.html

# Агрегированный
xdg-open build/reports/jacoco/aggregated/index.html
```

---

## Нагрузочные тесты (k6)

> Требуют запущенных сервисов (через Docker Compose или локально).

### Product Service

```bash
k6 run load-tests/product-service-load.js
```

Не требует авторизации. Тестирует листинг, поиск, фильтрацию, категории.

**Пороги:** p95 < 500ms, p99 < 1000ms, ошибки < 5%

### Auth Service

```bash
k6 run load-tests/auth-service-load.js
```

Тестирует регистрацию и аутентификацию.

**Пороги:** p95 < 800ms, p99 < 1500ms, ошибки < 10%

### Order Flow (E2E)

```bash
k6 run load-tests/order-flow-load.js
```

Тестирует полный флоу: авторизация → корзина → оформление заказа.

**Пороги:** p95 < 1000ms, p99 < 2000ms, ошибки < 10%

### С кастомным URL

```bash
k6 run -e BASE_URL=http://localhost:8080 load-tests/product-service-load.js
```

### С HTML-отчётом

```bash
k6 run --out json=results.json load-tests/product-service-load.js
```

---

## Полезные команды

```bash
# Запустить все тесты + покрытие + агрегированный отчёт
./gradlew test jacocoAggregatedReport

# Очистить и перезапустить
./gradlew clean test

# Посмотреть результаты тестов (HTML)
# auth-service/build/reports/tests/test/index.html
# product-service/build/reports/tests/test/index.html
# order-service/build/reports/tests/test/index.html
# notification-service/build/reports/tests/test/index.html
```

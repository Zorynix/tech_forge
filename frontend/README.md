# Tech Forge — Frontend

Клиентская часть маркетплейса электроники **Tech Forge** (テックフォージ) — SPA с японской эстетикой, glassmorphism-интерфейсом и поддержкой 3D-просмотра товаров.

![Next.js](https://img.shields.io/badge/Next.js-16-black?logo=next.js)
![React](https://img.shields.io/badge/React-19-blue?logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5-blue?logo=typescript)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4-38bdf8?logo=tailwindcss)

---

## Стек технологий

| Технология | Назначение |
|---|---|
| **Next.js 16** (App Router) | Фреймворк, SSR/SSG, маршрутизация |
| **React 19** | UI-библиотека |
| **TypeScript 5** | Типизация |
| **Tailwind CSS v4** | Стилизация, кастомная дизайн-система |
| **shadcn/ui** (base-nova) | Компонентная библиотека на базе `@base-ui/react` |
| **Zustand 5** | Управление состоянием (auth, cart) |
| **Framer Motion** | Анимации и переходы |
| **Three.js + React Three Fiber** | 3D-просмотр товаров (GLB-модели) |
| **Axios** | HTTP-клиент с JWT-интерсептором |
| **next-themes** | Переключение тёмной/светлой темы |
| **Lucide React** | Иконки |
| **Sonner** | Toast-уведомления |

---

## Страницы

| Маршрут | Описание |
|---|---|
| `/` | Главная — витрина, категории, 3D-герой |
| `/catalog` | Каталог с фильтрами, сортировкой и пагинацией |
| `/product/[id]` | Карточка товара — галерея, 3D-модель, отзывы |
| `/search` | Поиск по каталогу |
| `/cart` | Корзина (синхронизация гость/авторизованный) |
| `/orders` | История заказов |
| `/profile` | Профиль пользователя |
| `/about` | О проекте |
| `/roadmap` | Дорожная карта развития |
| `/privacy` | Политика конфиденциальности |

---

## Структура проекта

```
frontend/
├── src/
│   ├── app/                  # App Router — страницы и layout
│   │   ├── layout.tsx        # Корневой layout (шрифты, провайдеры, Header/Footer)
│   │   ├── globals.css       # Дизайн-система, CSS-переменные, кастомные утилиты
│   │   └── */page.tsx        # Страницы
│   ├── components/
│   │   ├── ui/               # shadcn/base-nova компоненты (button, card, dialog...)
│   │   ├── layout/           # Header, Footer
│   │   ├── auth/             # Модалка авторизации (телефон/email + код)
│   │   └── product/          # Карточки, фильтры, 3D-вьюер, отзывы
│   ├── lib/
│   │   └── api.ts            # Axios-клиент, JWT refresh, API-функции
│   ├── stores/
│   │   ├── auth-store.ts     # Аутентификация, JWT-токены
│   │   └── cart-store.ts     # Корзина (API + localStorage для гостей)
│   └── types/
│       └── index.ts          # TypeScript-типы (Product, Order, User...)
├── public/
│   ├── logo.png              # Логотип Tech Forge
│   ├── images/products/      # Фото товаров
│   ├── models/               # 3D GLB-модели (iPhone, MacBook)
│   └── fonts/                # Самохостящийся шрифт Kashima
└── [конфигурационные файлы]
```

---

## Дизайн-система

### Цветовая палитра (Oklch)

| Цвет | Light | Dark |
|---|---|---|
| Crimson (primary) | `oklch(0.55 0.22 15)` | `oklch(0.62 0.2 15)` |
| Gold (accent) | `oklch(0.73 0.12 75)` | `oklch(0.76 0.12 75)` |
| Фон | Тёплый ivory | Deep navy |

### Шрифты

- **Comfortaa** — основной (Google Fonts, кириллица)
- **Kashima** — дисплейный, японский стиль (self-hosted)
- **Geist Mono** — моноширинный

### Кастомные CSS-утилиты

- `.seigaiha` — японский волновой паттерн
- `.glow-gold` / `.glow-crimson` — свечение
- `.glass-panel` — glassmorphism-панели
- `.gold-line` — декоративная градиентная линия

---

## Быстрый старт

### Предварительные требования

- **Bun** (рекомендуется) или Node.js 20+
- Запущенный бэкенд (API Gateway на `localhost:8080`)

### Установка и запуск

```bash
# Установка зависимостей
bun install

# Настройка окружения
cp .env.example .env.local

# Запуск dev-сервера
bun dev
```

Приложение будет доступно по адресу: **http://localhost:3000**

### Переменные окружения

| Переменная | Описание | По умолчанию |
|---|---|---|
| `NEXT_PUBLIC_API_URL` | URL API Gateway | `http://localhost:8080/api/v1` |

---

## Аутентификация

Авторизация через телефон или email с 6-значным кодом подтверждения. JWT-токены хранятся в `localStorage` с автоматическим refresh через Axios-интерсептор. Корзина гостя автоматически синхронизируется с сервером после входа.

---

## 3D-просмотр

Для избранных товаров доступен интерактивный 3D-просмотр (GLB-модели) через React Three Fiber. Модели хранятся в `public/models/`.

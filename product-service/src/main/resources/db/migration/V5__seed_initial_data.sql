-- Categories
INSERT INTO product_service.categories (id, name, slug, description, parent_category_id, image_url) VALUES
('a1b2c3d4-0001-4000-8000-000000000001', 'Смартфоны', 'smartphones', 'Смартфоны от ведущих производителей', NULL, NULL),
('a1b2c3d4-0002-4000-8000-000000000002', 'Ноутбуки', 'laptops', 'Ноутбуки для работы и игр', NULL, NULL),
('a1b2c3d4-0003-4000-8000-000000000003', 'Наушники', 'headphones', 'Наушники и гарнитуры', NULL, NULL),
('a1b2c3d4-0004-4000-8000-000000000004', 'Мыши', 'mice', 'Игровые и офисные мыши', NULL, NULL),
('a1b2c3d4-0005-4000-8000-000000000005', 'Клавиатуры', 'keyboards', 'Механические и мембранные клавиатуры', NULL, NULL);

-- Products
-- 1. Pwnage StormBreaker White (mouse, 360° spin!)
INSERT INTO product_service.products (id, name, description, price, category_id, image_urls, spin_images, specifications, stock_quantity, active, created_at, updated_at) VALUES
(
    'b1b2c3d4-0005-4000-8000-000000000005',
    'Pwnage StormBreaker White',
    'Беспроводная игровая мышь массой 56 г. Сенсор PAW3950, 8000 Гц polling rate, Bluetooth + 2.4 ГГц.',
    12990,
    'a1b2c3d4-0004-4000-8000-000000000004',
    '["https://pwnage.com/cdn/shop/files/sb_white_01_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_white_02_dark.png?v=1706&width=800"]'::jsonb,
    '["https://pwnage.com/cdn/shop/files/sb_white_01_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_white_02_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_white_03_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_white_04_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_white_06_dark.png?v=1706&width=800"]'::jsonb,
    '{"Сенсор": "PixArt PAW3950", "DPI": "до 30000", "Polling Rate": "8000 Гц", "Подключение": "2.4 ГГц / Bluetooth", "Вес": "56 г", "Аккумулятор": "80 часов", "Кнопки": "6", "RGB": "Да"}'::jsonb,
    75, true, NOW(), NOW()
),
-- 2. Pwnage StormBreaker Mint (mouse, 360° spin!)
(
    'b1b2c3d4-0006-4000-8000-000000000006',
    'Pwnage StormBreaker Mint',
    'Та же мышь StormBreaker в мятном цвете. 56 г, сенсор PAW3950, 8K polling rate.',
    12990,
    'a1b2c3d4-0004-4000-8000-000000000004',
    '["https://pwnage.com/cdn/shop/files/sb_mint_01_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_mint_02_dark.png?v=1706&width=800"]'::jsonb,
    '["https://pwnage.com/cdn/shop/files/sb_mint_01_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_mint_02_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_mint_03_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_mint_04_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_mint_05_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_mint_06_dark.png?v=1706&width=800"]'::jsonb,
    '{"Сенсор": "PixArt PAW3950", "DPI": "до 30000", "Polling Rate": "8000 Гц", "Подключение": "2.4 ГГц / Bluetooth", "Вес": "56 г", "Аккумулятор": "80 часов", "Кнопки": "6", "RGB": "Да"}'::jsonb,
    60, true, NOW(), NOW()
),
-- 3. Pwnage StormBreaker Red (mouse, 360° spin!)
(
    'b1b2c3d4-0007-4000-8000-000000000007',
    'Pwnage StormBreaker Red',
    'StormBreaker в красном цвете. Ультралёгкая беспроводная мышь для киберспорта.',
    12990,
    'a1b2c3d4-0004-4000-8000-000000000004',
    '["https://pwnage.com/cdn/shop/files/sb_red_01_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_red_02_dark.png?v=1706&width=800"]'::jsonb,
    '["https://pwnage.com/cdn/shop/files/sb_red_01_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_red_02_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_red_03_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_red_04_dark.png?v=1706&width=800", "https://pwnage.com/cdn/shop/files/sb_red_06_dark.png?v=1706&width=800"]'::jsonb,
    '{"Сенсор": "PixArt PAW3950", "DPI": "до 30000", "Polling Rate": "8000 Гц", "Подключение": "2.4 ГГц / Bluetooth", "Вес": "56 г", "Аккумулятор": "80 часов", "Кнопки": "6", "RGB": "Да"}'::jsonb,
    45, true, NOW(), NOW()
),
-- 4. Logitech MX Master 3S
(
    'b1b2c3d4-0008-4000-8000-000000000008',
    'Logitech MX Master 3S',
    'Флагманская мышь для продуктивности. Тихие клики, сенсор 8000 DPI, MagSpeed колесо, работа на любой поверхности.',
    9990,
    'a1b2c3d4-0004-4000-8000-000000000004',
    '["/images/products/mx-master-3s-1.png", "/images/products/mx-master-3s-2.png"]'::jsonb,
    '[]'::jsonb,
    '{"Сенсор": "Darkfield 8000 DPI", "Подключение": "Bluetooth / Logi Bolt", "Аккумулятор": "70 дней", "Кнопки": "7", "Колесо": "MagSpeed электромагнитное", "Вес": "141 г", "Совместимость": "Windows / macOS / Linux / iPadOS", "Зарядка": "USB-C"}'::jsonb,
    200, true, NOW(), NOW()
),
-- 5. Logitech G PRO X Superlight 2
(
    'b1b2c3d4-0009-4000-8000-000000000009',
    'Logitech G PRO X Superlight 2',
    'Сверхлёгкая беспроводная игровая мышь (60 г). Сенсор HERO 2, 32000 DPI, 95 часов работы.',
    14990,
    'a1b2c3d4-0004-4000-8000-000000000004',
    '["/images/products/g-pro-x-superlight-2-1.png", "/images/products/g-pro-x-superlight-2-2.png"]'::jsonb,
    '[]'::jsonb,
    '{"Сенсор": "HERO 2 Sensor", "DPI": "32000", "Polling Rate": "2000 Гц", "Подключение": "LIGHTSPEED 2.4 ГГц", "Вес": "60 г", "Аккумулятор": "95 часов", "Кнопки": "5", "Зарядка": "USB-C"}'::jsonb,
    85, true, NOW(), NOW()
),
-- 6. Apple AirPods Max
(
    'b1b2c3d4-0010-4000-8000-000000000010',
    'Apple AirPods Max',
    'Накладные наушники с активным шумоподавлением, пространственным аудио и чипом H2. До 20 часов работы.',
    59990,
    'a1b2c3d4-0003-4000-8000-000000000003',
    '["/images/products/airpods-max-midnight.png", "/images/products/airpods-max-starlight.png", "/images/products/airpods-max-blue.png", "/images/products/airpods-max-purple.png", "/images/products/airpods-max-orange.png"]'::jsonb,
    '[]'::jsonb,
    '{"Чип": "Apple H2", "Шумоподавление": "Активное (ANC)", "Аккумулятор": "20 часов", "Подключение": "Bluetooth 5.3", "Драйвер": "40 мм", "Вес": "384.8 г", "Пространственное аудио": "Да с отслеживанием головы", "Зарядка": "USB-C"}'::jsonb,
    70, true, NOW(), NOW()
),
-- 7. Apple AirPods Pro 2
(
    'b1b2c3d4-0011-4000-8000-000000000011',
    'Apple AirPods Pro 2',
    'Полностью переработаны с чипом H2. Адаптивная прозрачность, персонализированное пространственное аудио, USB-C зарядка.',
    24990,
    'a1b2c3d4-0003-4000-8000-000000000003',
    '["/images/products/airpods-pro2-1.png", "/images/products/airpods-pro2-2.png"]'::jsonb,
    '[]'::jsonb,
    '{"Чип": "Apple H2", "Шумоподавление": "Активное с адаптивной прозрачностью", "Аккумулятор": "6 часов (30 с кейсом)", "Подключение": "Bluetooth 5.3", "Защита": "IPX4", "Зарядка": "USB-C, MagSafe, Qi", "Пространственное аудио": "Да"}'::jsonb,
    250, true, NOW(), NOW()
),
-- 8. Logitech G PRO X TKL
(
    'b1b2c3d4-0013-4000-8000-000000000013',
    'Logitech G PRO X TKL',
    'Компактная TKL игровая клавиатура. LIGHTSPEED беспроводная, GX-переключатели, LIGHTSYNC RGB.',
    16990,
    'a1b2c3d4-0005-4000-8000-000000000005',
    '["/images/products/g-pro-x-tkl-1.png", "/images/products/g-pro-x-tkl-2.png"]'::jsonb,
    '[]'::jsonb,
    '{"Раскладка": "TKL", "Переключатели": "GX Optical", "Подключение": "LIGHTSPEED / Bluetooth / USB-C", "Аккумулятор": "65 часов", "RGB": "LIGHTSYNC per-key", "Вес": "680 г", "Материал": "Алюминиевый корпус"}'::jsonb,
    55, true, NOW(), NOW()
),
-- 9. Apple Magic Keyboard
(
    'b1b2c3d4-0014-4000-8000-000000000014',
    'Apple Magic Keyboard с Touch ID',
    'Беспроводная клавиатура Apple с Touch ID для безопасной аутентификации. USB-C зарядка.',
    14990,
    'a1b2c3d4-0005-4000-8000-000000000005',
    '["/images/products/magic-keyboard-1.png", "/images/products/magic-keyboard-2.png"]'::jsonb,
    '[]'::jsonb,
    '{"Раскладка": "Полноразмерная", "Тип клавиш": "Ножничные", "Подключение": "Bluetooth / Lightning", "Аккумулятор": "1 месяц", "Touch ID": "Да", "Вес": "390 г", "Совместимость": "Mac с Apple Silicon"}'::jsonb,
    140, true, NOW(), NOW()
),
-- 10. Apple MacBook Air 15" M3
(
    'b1b2c3d4-0015-4000-8000-000000000015',
    'Apple MacBook Air 15" M3',
    'Тонкий и лёгкий ноутбук с чипом M3, 15.3" дисплеем Liquid Retina, 18 часов работы от батареи.',
    154990,
    'a1b2c3d4-0002-4000-8000-000000000002',
    '["/images/products/macbook-air-15-midnight.png", "/images/products/macbook-air-15-starlight.png"]'::jsonb,
    '[]'::jsonb,
    '{"Дисплей": "15.3\" Liquid Retina", "Процессор": "Apple M3 (8-ядер)", "Оперативная память": "8 ГБ", "Накопитель": "256 ГБ SSD", "GPU": "10-ядерный", "Аккумулятор": "18 часов", "Вес": "1.51 кг", "Порты": "2x Thunderbolt / USB 4, MagSafe, 3.5 мм"}'::jsonb,
    65, true, NOW(), NOW()
);

-- Video Reviews (YouTube embed URLs)
INSERT INTO product_service.video_reviews (id, product_id, title, video_url, thumbnail_url, duration_seconds, created_at) VALUES
('c1b2c3d4-0003-4000-8000-000000000003', 'b1b2c3d4-0005-4000-8000-000000000005', 'Pwnage StormBreaker — лучшая мышь для CS2?', 'https://www.youtube.com/embed/BzFVQ4eP_Ks', 'https://i.ytimg.com/vi/BzFVQ4eP_Ks/hqdefault.jpg', 612, NOW()),
('c1b2c3d4-0004-4000-8000-000000000004', 'b1b2c3d4-0010-4000-8000-000000000010', 'AirPods Max 2024 — стоят ли своих денег?', 'https://www.youtube.com/embed/Kj3fFrHhJSY', 'https://i.ytimg.com/vi/Kj3fFrHhJSY/hqdefault.jpg', 780, NOW());

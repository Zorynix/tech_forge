-- iPhone 17 Pro
INSERT INTO product_service.products (id, name, description, price, category_id, image_urls, spin_images, specifications, stock_quantity, active, created_at, updated_at) VALUES
(
    'b1b2c3d4-0016-4000-8000-000000000016',
    'Apple iPhone 17 Pro',
    'Новейший iPhone с чипом A19 Pro, титановым корпусом нового поколения, камерой 48 МП с улучшенным вычислительным фото и поддержкой Apple Intelligence.',
    139990,
    'a1b2c3d4-0001-4000-8000-000000000001',
    '["/images/products/iphone-17-pro-colors.png", "/images/products/iphone-17-pro-orange.png"]'::jsonb,
    '[]'::jsonb,
    '{"Дисплей": "6.3\" Super Retina XDR OLED ProMotion", "Процессор": "A19 Pro", "Оперативная память": "12 ГБ", "Накопитель": "256 ГБ", "Камера": "48 МП + 48 МП + 12 МП", "Аккумулятор": "4200 мАч", "ОС": "iOS 19", "5G": "Да", "Apple Intelligence": "Да"}'::jsonb,
    100, true, NOW(), NOW()
),
-- iPhone 17 Pro Max
(
    'b1b2c3d4-0017-4000-8000-000000000017',
    'Apple iPhone 17 Pro Max',
    'Максимальный iPhone 17 с чипом A19 Pro, 6.9" дисплеем, камерой 48 МП и до 33 часов воспроизведения видео. Apple Intelligence встроен.',
    159990,
    'a1b2c3d4-0001-4000-8000-000000000001',
    '["/images/products/iphone-17-pro-orange.png", "/images/products/iphone-17-pro-colors.png"]'::jsonb,
    '[]'::jsonb,
    '{"Дисплей": "6.9\" Super Retina XDR OLED ProMotion", "Процессор": "A19 Pro", "Оперативная память": "12 ГБ", "Накопитель": "256 ГБ", "Камера": "48 МП + 48 МП + 12 МП", "Аккумулятор": "4800 мАч", "ОС": "iOS 19", "5G": "Да", "Apple Intelligence": "Да"}'::jsonb,
    80, true, NOW(), NOW()
),
-- MacBook Pro
(
    'b1b2c3d4-0018-4000-8000-000000000018',
    'Apple MacBook Pro 16" M4 Pro',
    'Профессиональный ноутбук с чипом M4 Pro, 16" дисплеем Liquid Retina XDR, 24 ГБ единой памяти и до 22 часов автономной работы.',
    299990,
    'a1b2c3d4-0002-4000-8000-000000000002',
    '["/images/products/macbook-air-15-midnight.png"]'::jsonb,
    '[]'::jsonb,
    '{"Дисплей": "16.2\" Liquid Retina XDR", "Процессор": "Apple M4 Pro (14-ядер)", "Оперативная память": "24 ГБ", "Накопитель": "512 ГБ SSD", "GPU": "20-ядерный", "Аккумулятор": "22 часа", "Вес": "2.14 кг", "Порты": "3x Thunderbolt 5, HDMI, SDXC, MagSafe, 3.5 мм"}'::jsonb,
    50, true, NOW(), NOW()
);

-- Set 3D model URLs
UPDATE product_service.products SET model_url = '/models/iphone-17-pro.glb'
WHERE id = 'b1b2c3d4-0016-4000-8000-000000000016';

UPDATE product_service.products SET model_url = '/models/iphone-17-pro-max.glb'
WHERE id = 'b1b2c3d4-0017-4000-8000-000000000017';

UPDATE product_service.products SET model_url = '/models/macbook-pro.glb'
WHERE id = 'b1b2c3d4-0018-4000-8000-000000000018';

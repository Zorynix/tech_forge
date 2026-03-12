"use client";

import Image from "next/image";
import { motion } from "framer-motion";
import { Shield, Truck, HeadphonesIcon, Zap, Users, Award } from "lucide-react";

const advantages = [
  {
    icon: <Zap className="h-6 w-6" />,
    title: "Только оригинал",
    description: "Работаем напрямую с официальными дистрибьюторами. Никаких подделок — каждый товар сертифицирован.",
  },
  {
    icon: <Truck className="h-6 w-6" />,
    title: "Быстрая доставка",
    description: "Доставляем по всей России. Уведомления о статусе заказа приходят в Telegram в реальном времени.",
  },
  {
    icon: <Shield className="h-6 w-6" />,
    title: "Гарантия качества",
    description: "Официальная гарантия на всю технику. Если что-то пошло не так — решим вопрос.",
  },
  {
    icon: <HeadphonesIcon className="h-6 w-6" />,
    title: "Поддержка 24/7",
    description: "Наша команда всегда на связи. Пишите в Telegram — ответим в течение нескольких минут.",
  },
  {
    icon: <Users className="h-6 w-6" />,
    title: "Сообщество",
    description: "Более 10 000 довольных клиентов. Реальные отзывы и рейтинги на каждый товар.",
  },
  {
    icon: <Award className="h-6 w-6" />,
    title: "Лучшие цены",
    description: "Мониторим рынок и предлагаем конкурентные цены. Регулярные акции и скидки для постоянных клиентов.",
  },
];

export default function AboutPage() {
  return (
    <div className="min-h-screen">
      {/* Hero */}
      <section className="relative overflow-hidden border-b border-border/40">
        <div className="absolute inset-0 seigaiha opacity-50" />
        <div className="absolute inset-0 bg-gradient-to-b from-background/60 to-background" />
        <div className="absolute top-0 right-1/4 w-[400px] h-[400px] bg-primary/5 rounded-full blur-3xl" />

        <div className="container mx-auto px-4 py-16 md:py-20 relative">
          <div className="flex flex-col md:flex-row items-center gap-10">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5 }}
              className="flex-1 max-w-2xl"
            >
              <div className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full border border-gold/30 bg-gold/5 text-gold text-sm font-medium mb-6">
                <span className="tracking-[0.15em]">私たちについて</span>
              </div>
              <h1 className="text-4xl md:text-5xl font-bold tracking-tight leading-[1.1] mb-4">
                О нас
              </h1>
              <p className="text-lg text-muted-foreground leading-relaxed">
                Tech Forge — это магазин цифровой техники нового поколения.
                Мы помогаем людям находить лучшие устройства для работы, учёбы и развлечений.
              </p>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.5, delay: 0.2 }}
              className="hidden md:block"
            >
              <div className="relative">
                <div className="absolute inset-0 bg-primary/10 rounded-full blur-2xl scale-110" />
                <Image
                  src="/logo.png"
                  alt="Tech Forge"
                  width={200}
                  height={200}
                  className="relative rounded-2xl"
                />
              </div>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Story */}
      <section className="container mx-auto px-4 py-12">
        <motion.div
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: 0.1 }}
          className="max-w-3xl mx-auto"
        >
          <h2 className="text-2xl font-bold mb-4">Наша история</h2>
          <div className="space-y-4 text-muted-foreground leading-relaxed">
            <p>
              Tech Forge был основан в 2024 году командой энтузиастов, увлечённых технологиями.
              Мы заметили, что покупка техники онлайн часто превращается в стресс: непонятные
              характеристики, фейковые отзывы, долгая доставка без обратной связи.
            </p>
            <p>
              Мы решили это изменить. Наш магазин — это про честность, удобство и скорость.
              Каждый товар проходит проверку. Каждый отзыв — от реального покупателя. А статус
              доставки приходит прямо в Telegram, чтобы вы всегда были в курсе.
            </p>
            <p>
              Сегодня в нашем каталоге сотни товаров от ведущих мировых брендов: Apple, Samsung,
              Sony, Logitech, и многие другие. Мы постоянно расширяем ассортимент и работаем
              над тем, чтобы шоппинг техники был таким же приятным, как использование самой техники.
            </p>
          </div>
        </motion.div>
      </section>

      {/* Advantages */}
      <section className="container mx-auto px-4 py-12">
        <motion.div
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: 0.2 }}
        >
          <h2 className="text-2xl font-bold mb-8 text-center">Почему выбирают нас</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
            {advantages.map((item, i) => (
              <motion.div
                key={item.title}
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.3, delay: 0.1 + i * 0.05 }}
                className="group p-6 rounded-xl border border-border/40 bg-card hover:border-primary/20 hover:shadow-lg hover:shadow-primary/5 transition-all duration-300"
              >
                <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center text-primary mb-4 group-hover:bg-primary/15 transition-colors">
                  {item.icon}
                </div>
                <h3 className="font-semibold mb-2">{item.title}</h3>
                <p className="text-sm text-muted-foreground leading-relaxed">{item.description}</p>
              </motion.div>
            ))}
          </div>
        </motion.div>
      </section>

      {/* Contact CTA */}
      <section className="container mx-auto px-4 py-12 pb-16">
        <motion.div
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: 0.3 }}
          className="text-center max-w-xl mx-auto"
        >
          <h2 className="text-2xl font-bold mb-3">Остались вопросы?</h2>
          <p className="text-muted-foreground mb-1">
            Напишите нам в Telegram — мы всегда рады помочь.
          </p>
          <p className="text-sm text-gold font-medium">@tmarketZBot</p>
        </motion.div>
      </section>
    </div>
  );
}

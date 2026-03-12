"use client";

import { motion } from "framer-motion";
import {
  Rocket,
  Globe,
  Bot,
  Warehouse,
  CreditCard,
  Gamepad2,
  CheckCircle2,
  Circle,
  Clock,
} from "lucide-react";

type RoadmapStatus = "done" | "in-progress" | "planned";

interface RoadmapItem {
  quarter: string;
  title: string;
  description: string;
  status: RoadmapStatus;
  icon: React.ReactNode;
  highlights: string[];
}

const roadmap: RoadmapItem[] = [
  {
    quarter: "Q1 2026",
    title: "Запуск платформы",
    description:
      "Старт Tech Forge — интернет-магазин цифровой техники с каталогом, корзиной и оформлением заказов.",
    status: "done",
    icon: <Rocket className="h-6 w-6" />,
    highlights: [
      "Каталог с категориями и поиском",
      "Авторизация по телефону и email",
      "Корзина и оформление заказов",
      "Telegram-уведомления о статусе",
    ],
  },

  {
    quarter: "Q2 2026",
    title: "AI-ассистент покупателя",
    description:
      "Умный чат-бот на основе ИИ, который поможет подобрать технику под задачи и бюджет. Интеграция прямо в Telegram.",
    status: "in-progress",
    icon: <Bot className="h-6 w-6" />,
    highlights: [
      "Рекомендации по запросу",
      "Сравнение товаров через диалог",
      "Интеграция с Telegram-ботом",
      "Персонализация на основе истории",
    ],
  },
  {
    quarter: "Q3 2026",
    title: "Маркетплейс и продавцы",
    description:
      "Открытие площадки для сторонних продавцов. Верификация, рейтинг продавцов, защита покупателя.",
    status: "planned",
    icon: <Warehouse className="h-6 w-6" />,
    highlights: [
      "Личный кабинет продавца",
      "Система верификации",
      "Рейтинг и отзывы о продавцах",
      "Гарант-сделки и защита покупателя",
    ],
  },
  {
    quarter: "Q4 2026",
    title: "Международная экспансия",
    description:
      "Выход на рынки СНГ и Азии. Мультиязычность, местные валюты и логистические партнёры.",
    status: "planned",
    icon: <Globe className="h-6 w-6" />,
    highlights: [
      "Поддержка 5+ языков",
      "Мультивалютность",
      "Локальные склады в Казахстане и Китае",
      "Интеграция с международными службами доставки",
    ],
  },
  {
    quarter: "Q1 2027",
    title: "Подписка Tech Forge+",
    description:
      "Премиум-подписка с кэшбэком, ранним доступом к новинкам, бесплатной экспресс-доставкой и эксклюзивными предложениями.",
    status: "planned",
    icon: <CreditCard className="h-6 w-6" />,
    highlights: [
      "До 10% кэшбэк на все покупки",
      "Бесплатная экспресс-доставка",
      "Ранний доступ к лимитированным товарам",
      "Приоритетная поддержка",
    ],
  },
  {
    quarter: "2027+",
    title: "Геймификация и сообщество",
    description:
      "Программа лояльности с уровнями, достижениями и наградами. Форум и обзоры от комьюнити.",
    status: "planned",
    icon: <Gamepad2 className="h-6 w-6" />,
    highlights: [
      "Уровни и достижения",
      "Баллы за покупки и отзывы",
      "Форум и обсуждения товаров",
      "Конкурсы и розыгрыши",
    ],
  },
];

const statusConfig: Record<RoadmapStatus, { label: string; color: string; icon: React.ReactNode }> = {
  done: {
    label: "Готово",
    color: "text-emerald-500",
    icon: <CheckCircle2 className="h-4 w-4 text-emerald-500" />,
  },
  "in-progress": {
    label: "В разработке",
    color: "text-amber-500",
    icon: <Clock className="h-4 w-4 text-amber-500" />,
  },
  planned: {
    label: "Запланировано",
    color: "text-muted-foreground",
    icon: <Circle className="h-4 w-4 text-muted-foreground" />,
  },
};

export default function RoadmapPage() {
  return (
    <div className="min-h-screen">
      {/* Hero */}
      <section className="relative overflow-hidden border-b border-border/40">
        <div className="absolute inset-0 seigaiha opacity-50" />
        <div className="absolute inset-0 bg-gradient-to-b from-background/60 to-background" />
        <div className="absolute top-20 left-1/4 w-[500px] h-[500px] bg-primary/5 rounded-full blur-3xl" />
        <div className="absolute -bottom-10 right-1/4 w-[300px] h-[300px] bg-gold/5 rounded-full blur-3xl" />

        <div className="container mx-auto px-4 py-16 md:py-20 relative">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            className="max-w-2xl"
          >
            <div className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full border border-gold/30 bg-gold/5 text-gold text-sm font-medium mb-6">
              <span className="tracking-[0.15em]">ロードマップ</span>
            </div>
            <h1 className="text-4xl md:text-5xl font-bold tracking-tight leading-[1.1] mb-4">
              Roadmap
            </h1>
            <p className="text-lg text-muted-foreground leading-relaxed">
              Наш план развития — от идеи до глобальной платформы.
              Следите за прогрессом и узнавайте, что будет дальше.
            </p>
          </motion.div>
        </div>
      </section>

      {/* Timeline */}
      <section className="container mx-auto px-4 py-12 pb-20">
        <div className="max-w-4xl mx-auto relative">
          {/* Vertical line */}
          <div className="absolute left-6 md:left-1/2 top-0 bottom-0 w-px bg-gradient-to-b from-primary/30 via-primary/10 to-transparent md:-translate-x-px" />

          <div className="space-y-10">
            {roadmap.map((item, i) => {
              const isLeft = i % 2 === 0;
              const status = statusConfig[item.status];

              return (
                <motion.div
                  key={item.quarter}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ duration: 0.4, delay: 0.08 * i }}
                  className="relative"
                >
                  {/* Timeline dot */}
                  <div
                    className={`absolute left-6 md:left-1/2 -translate-x-1/2 z-10 h-4 w-4 rounded-full border-2 ${
                      item.status === "done"
                        ? "bg-emerald-500 border-emerald-500 shadow-lg shadow-emerald-500/30"
                        : item.status === "in-progress"
                        ? "bg-amber-500 border-amber-500 shadow-lg shadow-amber-500/30 animate-pulse"
                        : "bg-background border-border"
                    }`}
                  />

                  {/* Card */}
                  <div
                    className={`ml-14 md:ml-0 ${
                      isLeft
                        ? "md:mr-[calc(50%+2rem)] md:pr-0"
                        : "md:ml-[calc(50%+2rem)] md:pl-0"
                    }`}
                  >
                    <div className="glass-panel glass-panel-hover glass-highlight relative rounded-2xl p-6 overflow-hidden">
                      {/* Subtle glow for active item */}
                      {item.status === "in-progress" && (
                        <div className="absolute -top-20 -right-20 w-40 h-40 bg-amber-500/10 rounded-full blur-3xl" />
                      )}
                      {item.status === "done" && (
                        <div className="absolute -top-20 -right-20 w-40 h-40 bg-emerald-500/5 rounded-full blur-3xl" />
                      )}

                      <div className="relative">
                        {/* Header */}
                        <div className="flex items-start justify-between gap-3 mb-4">
                          <div className="flex items-center gap-3">
                            <div
                              className={`h-12 w-12 rounded-xl flex items-center justify-center shrink-0 ${
                                item.status === "done"
                                  ? "bg-emerald-500/10 text-emerald-500"
                                  : item.status === "in-progress"
                                  ? "bg-amber-500/10 text-amber-500"
                                  : "bg-primary/10 text-primary"
                              }`}
                            >
                              {item.icon}
                            </div>
                            <div>
                              <span className="text-xs font-medium text-gold tracking-wider uppercase">
                                {item.quarter}
                              </span>
                              <h3 className="text-lg font-bold">{item.title}</h3>
                            </div>
                          </div>
                          <div className="flex items-center gap-1.5 shrink-0 mt-1">
                            {status.icon}
                            <span className={`text-xs font-medium ${status.color}`}>
                              {status.label}
                            </span>
                          </div>
                        </div>

                        {/* Description */}
                        <p className="text-sm text-muted-foreground leading-relaxed mb-4">
                          {item.description}
                        </p>

                        {/* Highlights */}
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                          {item.highlights.map((hl) => (
                            <div
                              key={hl}
                              className="flex items-start gap-2 text-sm"
                            >
                              <div
                                className={`h-1.5 w-1.5 rounded-full mt-1.5 shrink-0 ${
                                  item.status === "done"
                                    ? "bg-emerald-500"
                                    : item.status === "in-progress"
                                    ? "bg-amber-500"
                                    : "bg-primary/40"
                                }`}
                              />
                              <span className="text-muted-foreground">{hl}</span>
                            </div>
                          ))}
                        </div>
                      </div>
                    </div>
                  </div>
                </motion.div>
              );
            })}
          </div>
        </div>
      </section>
    </div>
  );
}

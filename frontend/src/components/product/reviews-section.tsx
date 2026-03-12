"use client";

import { useEffect, useState } from "react";
import { getReviews, getReviewSummary, createReview } from "@/lib/api";
import type { Review, ReviewSummary, CreateReviewRequest } from "@/types";
import { useAuthStore } from "@/stores/auth-store";
import { RatingStars, InteractiveRating } from "./rating-stars";
import { RatingBreakdown } from "./rating-breakdown";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { MessageSquarePlus, User, Send, Star, ChevronDown, ChevronUp } from "lucide-react";
import { toast } from "sonner";
import { motion, AnimatePresence } from "framer-motion";

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString("ru-RU", {
    day: "numeric",
    month: "long",
    year: "numeric",
  });
}

function getInitials(name: string) {
  return name
    .split(" ")
    .map((w) => w[0])
    .join("")
    .toUpperCase()
    .slice(0, 2);
}

export function ReviewsSection({ productId }: { productId: string }) {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [summary, setSummary] = useState<ReviewSummary | null>(null);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [showAll, setShowAll] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const { isAuthenticated, user, setShowAuthModal } = useAuthStore();

  const [form, setForm] = useState<CreateReviewRequest>({
    authorName: "",
    title: "",
    content: "",
    ratingQuality: 7,
    ratingPrice: 7,
    ratingDesign: 7,
    ratingFeatures: 7,
    ratingUsability: 7,
  });

  useEffect(() => {
    Promise.all([getReviews(productId), getReviewSummary(productId)])
      .then(([r, s]) => {
        setReviews(r);
        setSummary(s);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [productId]);

  useEffect(() => {
    if (user?.name) {
      setForm((f) => ({ ...f, authorName: user.name || "" }));
    }
  }, [user]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.content.trim()) {
      toast.error("Напишите текст отзыва");
      return;
    }
    if (!form.authorName.trim()) {
      toast.error("Укажите ваше имя");
      return;
    }
    setSubmitting(true);
    try {
      const newReview = await createReview(productId, form);
      setReviews((prev) => [newReview, ...prev]);
      const newSummary = await getReviewSummary(productId);
      setSummary(newSummary);
      setShowForm(false);
      setForm({
        authorName: user?.name || "",
        title: "",
        content: "",
        ratingQuality: 7,
        ratingPrice: 7,
        ratingDesign: 7,
        ratingFeatures: 7,
        ratingUsability: 7,
      });
      toast.success("Отзыв опубликован!");
    } catch {
      toast.error("Не удалось отправить отзыв");
    } finally {
      setSubmitting(false);
    }
  };

  const displayedReviews = showAll ? reviews : reviews.slice(0, 4);

  if (loading) {
    return (
      <div className="space-y-4">
        <Skeleton className="h-8 w-48" />
        <div className="grid md:grid-cols-3 gap-4">
          <Skeleton className="h-48" />
          <Skeleton className="h-48 md:col-span-2" />
        </div>
      </div>
    );
  }

  return (
    <section className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h2 className="text-xl font-bold flex items-center gap-2">
          <MessageSquarePlus className="h-5 w-5 text-gold" />
          Отзывы и оценки
          {summary && summary.totalReviews > 0 && (
            <span className="text-sm font-normal text-muted-foreground">
              ({summary.totalReviews})
            </span>
          )}
        </h2>
        {isAuthenticated ? (
          <Button
            variant="outline"
            size="sm"
            onClick={() => setShowForm(!showForm)}
            className="gap-1.5 border-gold/30 text-gold hover:bg-gold/5 hover:text-gold"
          >
            <Send className="h-3.5 w-3.5" />
            Написать отзыв
          </Button>
        ) : (
          <Button
            variant="outline"
            size="sm"
            onClick={() => setShowAuthModal(true)}
            className="gap-1.5 border-border/50 text-muted-foreground"
          >
            <User className="h-3.5 w-3.5" />
            Войдите для отзыва
          </Button>
        )}
      </div>

      {/* Summary + Breakdown */}
      {summary && summary.totalReviews > 0 && (
        <div className="glass-panel rounded-2xl p-6">
          <div className="grid md:grid-cols-[1fr_2fr] gap-8">
            {/* Overall score */}
            <div className="flex flex-col items-center justify-center text-center space-y-2">
              <div className="text-5xl font-bold bg-gradient-to-b from-gold to-gold/70 bg-clip-text text-transparent">
                {summary.averageOverall.toFixed(1)}
              </div>
              <RatingStars rating={summary.averageOverall} size="lg" showValue={false} />
              <p className="text-sm text-muted-foreground">
                На основе {summary.totalReviews}{" "}
                {summary.totalReviews === 1
                  ? "отзыва"
                  : summary.totalReviews < 5
                  ? "отзывов"
                  : "отзывов"}
              </p>
            </div>
            {/* Criteria breakdown */}
            <RatingBreakdown summary={summary} />
          </div>
        </div>
      )}

      {/* Write Review Form */}
      <AnimatePresence>
        {showForm && isAuthenticated && (
          <motion.form
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: "auto" }}
            exit={{ opacity: 0, height: 0 }}
            transition={{ duration: 0.3 }}
            onSubmit={handleSubmit}
            className="glass-panel rounded-2xl p-6 space-y-5 overflow-hidden"
          >
            <h3 className="font-semibold text-sm uppercase tracking-[0.1em]">Новый отзыв</h3>

            <div className="grid sm:grid-cols-2 gap-4">
              <div className="space-y-1.5">
                <label className="text-sm text-muted-foreground">Ваше имя</label>
                <input
                  type="text"
                  value={form.authorName}
                  onChange={(e) => setForm({ ...form, authorName: e.target.value })}
                  className="w-full rounded-lg border border-border/50 bg-background/50 px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-gold/50"
                  placeholder="Имя"
                  maxLength={255}
                  required
                />
              </div>
              <div className="space-y-1.5">
                <label className="text-sm text-muted-foreground">Заголовок (необязательно)</label>
                <input
                  type="text"
                  value={form.title}
                  onChange={(e) => setForm({ ...form, title: e.target.value })}
                  className="w-full rounded-lg border border-border/50 bg-background/50 px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-gold/50"
                  placeholder="Краткий заголовок отзыва"
                  maxLength={500}
                />
              </div>
            </div>

            <div className="space-y-1.5">
              <label className="text-sm text-muted-foreground">Текст отзыва</label>
              <textarea
                value={form.content}
                onChange={(e) => setForm({ ...form, content: e.target.value })}
                className="w-full rounded-lg border border-border/50 bg-background/50 px-3 py-2 text-sm min-h-[100px] resize-y focus:outline-none focus:ring-1 focus:ring-gold/50"
                placeholder="Поделитесь вашим опытом использования..."
                maxLength={5000}
                required
              />
            </div>

            <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-x-6 gap-y-3">
              <InteractiveRating
                label="Качество"
                value={form.ratingQuality}
                onChange={(v) => setForm({ ...form, ratingQuality: v })}
              />
              <InteractiveRating
                label="Цена / качество"
                value={form.ratingPrice}
                onChange={(v) => setForm({ ...form, ratingPrice: v })}
              />
              <InteractiveRating
                label="Дизайн"
                value={form.ratingDesign}
                onChange={(v) => setForm({ ...form, ratingDesign: v })}
              />
              <InteractiveRating
                label="Функциональность"
                value={form.ratingFeatures}
                onChange={(v) => setForm({ ...form, ratingFeatures: v })}
              />
              <InteractiveRating
                label="Удобство"
                value={form.ratingUsability}
                onChange={(v) => setForm({ ...form, ratingUsability: v })}
              />
            </div>

            <div className="flex justify-end gap-2">
              <Button
                type="button"
                variant="ghost"
                size="sm"
                onClick={() => setShowForm(false)}
              >
                Отмена
              </Button>
              <Button
                type="submit"
                size="sm"
                disabled={submitting}
                className="gap-1.5 bg-gold text-gold-foreground hover:bg-gold/90"
              >
                <Send className="h-3.5 w-3.5" />
                {submitting ? "Отправка..." : "Опубликовать"}
              </Button>
            </div>
          </motion.form>
        )}
      </AnimatePresence>

      {/* Reviews List */}
      {reviews.length === 0 ? (
        <div className="glass-panel rounded-2xl p-10 text-center">
          <Star className="h-10 w-10 text-muted-foreground/30 mx-auto mb-3" />
          <p className="text-muted-foreground">Отзывов пока нет</p>
          <p className="text-sm text-muted-foreground/70 mt-1">Будьте первым, кто оставит отзыв!</p>
        </div>
      ) : (
        <div className="space-y-3">
          {displayedReviews.map((review, i) => (
            <motion.div
              key={review.id}
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.3, delay: i * 0.05 }}
              className="glass-panel rounded-xl p-5 space-y-3"
            >
              {/* Review header */}
              <div className="flex items-start justify-between gap-3">
                <div className="flex items-center gap-3">
                  <div className="h-9 w-9 rounded-full bg-gradient-to-br from-gold/20 to-primary/20 flex items-center justify-center text-xs font-bold text-gold shrink-0">
                    {getInitials(review.authorName)}
                  </div>
                  <div>
                    <p className="font-medium text-sm">{review.authorName}</p>
                    <p className="text-xs text-muted-foreground">{formatDate(review.createdAt)}</p>
                  </div>
                </div>
                <RatingStars rating={review.overallRating} size="sm" />
              </div>

              {/* Title */}
              {review.title && (
                <h4 className="font-semibold text-sm">{review.title}</h4>
              )}

              {/* Content */}
              <p className="text-sm text-muted-foreground leading-relaxed">{review.content}</p>

              {/* Mini criteria */}
              <div className="flex flex-wrap gap-2 pt-1">
                {[
                  { label: "Качество", value: review.ratingQuality },
                  { label: "Цена", value: review.ratingPrice },
                  { label: "Дизайн", value: review.ratingDesign },
                  { label: "Функции", value: review.ratingFeatures },
                  { label: "Удобство", value: review.ratingUsability },
                ].map(({ label, value }) => (
                  <span
                    key={label}
                    className="inline-flex items-center gap-1 text-xs px-2 py-0.5 rounded-full bg-secondary/50 text-muted-foreground"
                  >
                    {label}
                    <span className="font-semibold text-foreground">{value}</span>
                  </span>
                ))}
              </div>
            </motion.div>
          ))}

          {/* Show more/less */}
          {reviews.length > 4 && (
            <div className="flex justify-center pt-2">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setShowAll(!showAll)}
                className="gap-1.5 text-muted-foreground hover:text-foreground"
              >
                {showAll ? (
                  <>
                    <ChevronUp className="h-4 w-4" />
                    Свернуть
                  </>
                ) : (
                  <>
                    <ChevronDown className="h-4 w-4" />
                    Показать все {reviews.length} отзывов
                  </>
                )}
              </Button>
            </div>
          )}
        </div>
      )}
    </section>
  );
}

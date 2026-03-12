"use client";

import { useEffect, useState, lazy, Suspense } from "react";
import { useParams } from "next/navigation";
import { getProduct, getReviewSummary } from "@/lib/api";
import type { Product, ReviewSummary } from "@/types";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import { ShoppingCart, ArrowLeft, Check, RotateCw, Box, ImageIcon, Star } from "lucide-react";
import { VideoReviews } from "@/components/product/video-reviews";
import { SpinViewer } from "@/components/product/spin-viewer";
import { ReviewsSection } from "@/components/product/reviews-section";
import { RatingStars } from "@/components/product/rating-stars";
import { useCartStore } from "@/stores/cart-store";
import { toast } from "sonner";
import Link from "next/link";
import Image from "next/image";
import { motion } from "framer-motion";

const ModelViewer = lazy(() =>
  import("@/components/product/model-viewer").then((m) => ({ default: m.ModelViewer }))
);

function formatPrice(price: number) {
  return new Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB",
    maximumFractionDigits: 0,
  }).format(price);
}

type ViewMode = "3d" | "360" | "gallery";

export default function ProductPage() {
  const { id } = useParams<{ id: string }>();
  const [product, setProduct] = useState<Product | null>(null);
  const [summary, setSummary] = useState<ReviewSummary | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedImage, setSelectedImage] = useState(0);
  const [viewMode, setViewMode] = useState<ViewMode>("gallery");
  const { addItem } = useCartStore();

  useEffect(() => {
    if (id) {
      Promise.all([getProduct(id), getReviewSummary(id)])
        .then(([p, s]) => {
          setProduct(p);
          setSummary(s);
          if (p.modelUrl) {
            setViewMode("3d");
          } else if (p.spinImages?.length > 0) {
            setViewMode("360");
          } else {
            setViewMode("gallery");
          }
        })
        .catch(console.error)
        .finally(() => setLoading(false));
    }
  }, [id]);

  const handleAddToCart = async () => {
    if (!product) return;
    await addItem({
      productId: product.id,
      productName: product.name,
      price: product.price,
      imageUrl: product.imageUrls?.[0] || "",
    });
    toast.success(`${product.name} добавлен в корзину`);
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8 space-y-6">
        <Skeleton className="h-6 w-24" />
        <div className="grid md:grid-cols-2 gap-8">
          <Skeleton className="aspect-square rounded-2xl" />
          <div className="space-y-4">
            <Skeleton className="h-8 w-3/4" />
            <Skeleton className="h-6 w-1/4" />
            <Skeleton className="h-20 w-full" />
            <Skeleton className="h-12 w-40" />
          </div>
        </div>
      </div>
    );
  }

  if (!product) {
    return (
      <div className="container mx-auto px-4 py-24 text-center">
        <p className="text-xl text-muted-foreground">Товар не найден</p>
        <Link href="/">
          <Button variant="outline" className="mt-4">На главную</Button>
        </Link>
      </div>
    );
  }

  const has3D = !!product.modelUrl;
  const has360 = product.spinImages?.length > 0;
  const hasMultipleViews = [has3D, has360, true].filter(Boolean).length > 1;

  return (
    <div className="container mx-auto px-4 py-8 space-y-8">
      <Link href="/" className="inline-flex items-center gap-1 text-sm text-muted-foreground hover:text-primary transition-colors">
        <ArrowLeft className="h-4 w-4" /> Назад в каталог
      </Link>

      <div className="grid md:grid-cols-2 gap-8 lg:gap-12">
        {/* Images / 360 / 3D Viewer */}
        <motion.div
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.4 }}
          className="space-y-3"
        >
          {/* View mode toggle */}
          {hasMultipleViews && (
            <div className="flex gap-1 p-1 glass-panel rounded-lg w-fit">
              {has3D && (
                <button
                  onClick={() => setViewMode("3d")}
                  className={`flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm font-medium transition-colors ${
                    viewMode === "3d"
                      ? "bg-card shadow-sm text-foreground border border-border/50"
                      : "text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <Box className="h-3.5 w-3.5" /> 3D
                </button>
              )}
              {has360 && (
                <button
                  onClick={() => setViewMode("360")}
                  className={`flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm font-medium transition-colors ${
                    viewMode === "360"
                      ? "bg-card shadow-sm text-foreground border border-border/50"
                      : "text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <RotateCw className="h-3.5 w-3.5" /> 360°
                </button>
              )}
              <button
                onClick={() => setViewMode("gallery")}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-md text-sm font-medium transition-colors ${
                  viewMode === "gallery"
                    ? "bg-card shadow-sm text-foreground border border-border/50"
                    : "text-muted-foreground hover:text-foreground"
                }`}
              >
                <ImageIcon className="h-3.5 w-3.5" /> Фото
              </button>
            </div>
          )}

          {/* 3D Model Viewer */}
          {viewMode === "3d" && product.modelUrl ? (
            <Suspense
              fallback={
                <div className="aspect-square rounded-2xl glass-panel flex items-center justify-center">
                  <div className="h-10 w-10 border-3 border-primary border-t-transparent rounded-full animate-spin" />
                </div>
              }
            >
              <ModelViewer modelUrl={product.modelUrl} alt={product.name} />
            </Suspense>
          ) : viewMode === "360" && product.spinImages?.length > 0 ? (
            <SpinViewer images={product.spinImages} alt={product.name} />
          ) : (
            <>
              <div className="product-image relative aspect-square rounded-2xl glass-panel overflow-hidden">
                {product.imageUrls?.[selectedImage] ? (
                  <Image
                    src={product.imageUrls[selectedImage]}
                    alt={product.name}
                    fill
                    className="object-contain p-6"
                    sizes="(max-width: 768px) 100vw, 50vw"
                    priority
                  />
                ) : (
                  <div className="flex items-center justify-center h-full text-muted-foreground">
                    Нет фото
                  </div>
                )}
              </div>
              {product.imageUrls?.length > 1 && (
                <div className="flex gap-2 overflow-x-auto pb-1">
                  {product.imageUrls.map((url, i) => (
                    <button
                      key={i}
                      onClick={() => setSelectedImage(i)}
                      className={`relative h-16 w-16 shrink-0 rounded-lg border-2 overflow-hidden transition-colors ${
                        i === selectedImage ? "border-primary" : "border-border/30 hover:border-border"
                      }`}
                    >
                      <Image src={url} alt="" fill className="object-contain p-1" sizes="64px" />
                    </button>
                  ))}
                </div>
              )}
            </>
          )}
        </motion.div>

        {/* Info */}
        <motion.div
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.4, delay: 0.1 }}
          className="space-y-5"
        >
          <div>
            <p className="text-xs text-gold font-medium uppercase tracking-[0.15em] mb-2">{product.categoryName}</p>
            <h1 className="text-2xl md:text-3xl font-bold">{product.name}</h1>
          </div>

          {/* Rating summary inline */}
          {summary && summary.totalReviews > 0 && (
            <div className="flex items-center gap-3">
              <RatingStars rating={summary.averageOverall} size="md" />
              <span className="text-sm text-muted-foreground">
                {summary.totalReviews}{" "}
                {summary.totalReviews === 1 ? "отзыв" : summary.totalReviews < 5 ? "отзыва" : "отзывов"}
              </span>
            </div>
          )}

          <div className="flex items-center gap-3">
            <span className="text-3xl font-bold text-primary">{formatPrice(product.price)}</span>
            {product.stockQuantity > 0 ? (
              <Badge variant="secondary" className="gap-1">
                <Check className="h-3 w-3" /> В наличии
              </Badge>
            ) : (
              <Badge variant="destructive">Нет в наличии</Badge>
            )}
          </div>

          <p className="text-muted-foreground leading-relaxed">{product.description}</p>

          <Button
            size="lg"
            className="w-full md:w-auto gap-2 shadow-md shadow-primary/20"
            onClick={handleAddToCart}
            disabled={product.stockQuantity === 0}
          >
            <ShoppingCart className="h-5 w-5" />
            В корзину
          </Button>

          {/* Specifications in glass panel */}
          {product.specifications && Object.keys(product.specifications).length > 0 && (
            <div className="glass-panel rounded-xl p-5 space-y-3">
              <h2 className="font-semibold text-sm uppercase tracking-[0.1em]">Характеристики</h2>
              <dl className="space-y-0">
                {Object.entries(product.specifications).map(([key, value]) => (
                  <div key={key} className="flex justify-between text-sm py-2.5 border-b border-border/20 last:border-0">
                    <dt className="text-muted-foreground">{key}</dt>
                    <dd className="font-medium text-right">{value}</dd>
                  </div>
                ))}
              </dl>
            </div>
          )}
        </motion.div>
      </div>

      {/* Reviews Section */}
      <div className="gold-line" />
      <ReviewsSection productId={product.id} />

      {/* Video Reviews */}
      <div className="gold-line" />
      <VideoReviews productId={product.id} />
    </div>
  );
}

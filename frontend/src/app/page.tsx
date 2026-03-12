"use client";

import { useEffect, useState } from "react";
import Image from "next/image";
import Link from "next/link";
import { getProducts, getCategories } from "@/lib/api";
import { ProductCard } from "@/components/product/product-card";
import { Button } from "@/components/ui/button";
import type { Product, Category } from "@/types";
import { motion } from "framer-motion";
import { ArrowRight, Sparkles, Smartphone, Laptop, Headphones, Mouse, Keyboard, LayoutGrid } from "lucide-react";

const CATEGORY_ICONS: Record<string, React.ReactNode> = {
  smartphones: <Smartphone className="h-5 w-5 text-primary" />,
  laptops: <Laptop className="h-5 w-5 text-primary" />,
  headphones: <Headphones className="h-5 w-5 text-primary" />,
  mice: <Mouse className="h-5 w-5 text-primary" />,
  keyboards: <Keyboard className="h-5 w-5 text-primary" />,
};

export default function HomePage() {
  const [newArrivals, setNewArrivals] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getProducts(0, 6)
      .then((data) => {
        const filtered = data.content
          .filter((p) => !p.name.toLowerCase().includes("superlight"))
          .slice(0, 4);
        setNewArrivals(filtered);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
    getCategories().then(setCategories).catch(console.error);
  }, []);

  const parentCategories = categories.filter((c) => !c.parentCategoryId);

  return (
    <div className="space-y-12">
      {/* Hero */}
      <section className="relative overflow-hidden">
        <div className="absolute inset-0 seigaiha" />
        <div className="absolute inset-0 bg-gradient-to-b from-background/40 via-background/80 to-background" />

        <div className="absolute top-0 right-1/4 w-[400px] h-[400px] bg-primary/5 rounded-full blur-3xl" />
        <div className="absolute -bottom-20 left-0 w-[300px] h-[300px] bg-gold/5 rounded-full blur-3xl" />

        <div className="container mx-auto px-4 py-16 md:py-24 relative">
          <div className="flex flex-col md:flex-row items-center gap-10">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, ease: "easeOut" }}
              className="flex-1 max-w-2xl"
            >
              <div className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full border border-gold/30 bg-gold/5 text-gold text-sm font-medium mb-6">
                <span className="tracking-[0.15em]">テックフォージ</span>
              </div>
              <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold tracking-tight leading-[1.1]">
                Цифровая техника{" "}
                <span className="bg-gradient-to-r from-primary via-primary to-primary/50 bg-clip-text text-transparent">
                  нового поколения
                </span>
              </h1>
              <p className="mt-4 text-lg text-muted-foreground max-w-xl leading-relaxed">
                Смартфоны, наушники, аксессуары от ведущих брендов.
                Оформляйте заказ и отслеживайте статус через Telegram.
              </p>

              <div className="mt-8 flex flex-wrap gap-3">
                <Link href="/catalog">
                  <Button size="lg" className="rounded-full px-8 gap-2 shadow-lg shadow-primary/20">
                    <LayoutGrid className="h-4 w-4" />
                    Перейти в каталог
                  </Button>
                </Link>
              </div>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.6, delay: 0.2, ease: "easeOut" }}
              className="hidden lg:block"
            >
              <div className="relative">
                <div className="absolute inset-0 bg-primary/10 rounded-full blur-2xl scale-110" />
                <Image
                  src="/logo.png"
                  alt="Tech Forge"
                  width={260}
                  height={260}
                  className="relative rounded-2xl"
                  priority
                />
              </div>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Categories quick links */}
      {parentCategories.length > 0 && (
        <section className="container mx-auto px-4">
          <motion.div
            initial={{ opacity: 0, y: 15 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.4, delay: 0.2 }}
          >
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-3">
              {parentCategories.map((cat, i) => (
                <motion.div
                  key={cat.id}
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ duration: 0.3, delay: 0.1 + i * 0.05 }}
                >
                  <Link
                    href={`/catalog?category=${cat.id}`}
                    className="group flex items-center gap-3 p-4 rounded-xl border border-border/40 bg-card hover:border-primary/30 hover:shadow-lg hover:shadow-primary/5 transition-all duration-300 hover:-translate-y-0.5"
                  >
                    <div className="h-10 w-10 rounded-lg bg-primary/10 flex items-center justify-center shrink-0 group-hover:bg-primary/15 transition-colors">
                      {CATEGORY_ICONS[cat.slug] || <LayoutGrid className="h-5 w-5 text-primary" />}
                    </div>
                    <span className="text-sm font-medium truncate">{cat.name}</span>
                  </Link>
                </motion.div>
              ))}
            </div>
          </motion.div>
        </section>
      )}

      {/* New Arrivals */}
      <section className="container mx-auto px-4 pb-12">
        <motion.div
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: 0.3 }}
        >
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center gap-3">
              <div className="h-10 w-10 rounded-xl bg-gold/10 flex items-center justify-center">
                <Sparkles className="h-5 w-5 text-gold" />
              </div>
              <div>
                <h2 className="text-2xl font-bold">Новинки</h2>
                <p className="text-sm text-muted-foreground">Недавно добавленные товары</p>
              </div>
            </div>
            <Link href="/catalog">
              <Button
                variant="outline"
                className="rounded-full gap-2 border-gold/30 text-gold hover:bg-gold/5 hover:text-gold"
              >
                Все товары
                <ArrowRight className="h-4 w-4" />
              </Button>
            </Link>
          </div>

          {loading ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
              {Array.from({ length: 4 }).map((_, i) => (
                <div key={i} className="rounded-xl bg-card border border-border/40 overflow-hidden animate-pulse">
                  <div className="aspect-square bg-secondary/30" />
                  <div className="p-4 space-y-3">
                    <div className="h-3 bg-secondary/50 rounded w-1/3" />
                    <div className="h-4 bg-secondary/50 rounded w-2/3" />
                    <div className="h-5 bg-secondary/50 rounded w-1/4" />
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
              {newArrivals.map((product, idx) => (
                <ProductCard key={product.id} product={product} index={idx} />
              ))}
            </div>
          )}
        </motion.div>
      </section>
    </div>
  );
}

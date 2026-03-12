"use client";

import Image from "next/image";
import Link from "next/link";
import { ShoppingCart, Star } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import type { Product } from "@/types";
import { useCartStore } from "@/stores/cart-store";
import { toast } from "sonner";
import { motion } from "framer-motion";

function formatPrice(price: number) {
  return new Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB",
    maximumFractionDigits: 0,
  }).format(price);
}

export function ProductCard({ product, index = 0 }: { product: Product; index?: number }) {
  const { addItem } = useCartStore();

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    await addItem({
      productId: product.id,
      productName: product.name,
      price: product.price,
      imageUrl: product.imageUrls?.[0] || "",
    });
    toast.success(`${product.name} добавлен в корзину`);
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.35, delay: index * 0.05, ease: "easeOut" }}
    >
      <Link href={`/product/${product.id}`}>
        <div className="group relative rounded-xl bg-card border border-border/40 overflow-hidden transition-all duration-300 hover:shadow-xl hover:shadow-primary/5 hover:border-primary/20 hover:-translate-y-1">
          {/* Top accent line */}
          <div className="h-[1.5px] bg-gradient-to-r from-transparent via-primary/40 to-transparent" />

          <div className="product-image relative aspect-square bg-gradient-to-br from-secondary/30 to-secondary/10 overflow-hidden">
            {product.imageUrls?.[0] ? (
              <Image
                src={product.imageUrls[0]}
                alt={product.name}
                fill
                className="object-contain p-6 transition-transform duration-500 ease-out group-hover:scale-110"
                sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 25vw"
              />
            ) : (
              <div className="flex items-center justify-center h-full text-muted-foreground text-sm">
                Нет фото
              </div>
            )}
            {product.stockQuantity < 5 && product.stockQuantity > 0 && (
              <Badge variant="destructive" className="absolute top-3 right-3 text-xs shadow-sm">
                Осталось мало
              </Badge>
            )}
            {product.stockQuantity === 0 && (
              <Badge variant="secondary" className="absolute top-3 right-3 text-xs">
                Нет в наличии
              </Badge>
            )}
            {/* Cart button overlay */}
            <div className="absolute bottom-3 right-3 opacity-0 translate-y-2 group-hover:opacity-100 group-hover:translate-y-0 transition-all duration-300">
              <Button
                size="icon"
                className="h-10 w-10 rounded-full shadow-lg shadow-primary/30 bg-primary hover:bg-primary/90"
                onClick={handleAddToCart}
                disabled={product.stockQuantity === 0}
              >
                <ShoppingCart className="h-4 w-4" />
              </Button>
            </div>
          </div>
          <div className="p-4 space-y-1.5">
            <p className="text-[11px] text-gold font-medium uppercase tracking-[0.15em]">
              {product.categoryName}
            </p>
            <h3 className="font-medium text-sm line-clamp-2 leading-snug min-h-[2.5rem]">
              {product.name}
            </h3>
            <div className="flex items-center justify-between pt-1">
              <p className="font-bold text-lg text-primary">{formatPrice(product.price)}</p>
              {product.averageRating != null && product.averageRating > 0 && (
                <div className="rating-badge flex items-center gap-1 px-1.5 py-0.5 rounded-md text-xs font-semibold">
                  <Star className="h-3 w-3 fill-current" />
                  {product.averageRating.toFixed(1)}
                </div>
              )}
            </div>
          </div>
        </div>
      </Link>
    </motion.div>
  );
}

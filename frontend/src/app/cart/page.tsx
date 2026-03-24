"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { Trash2, ShoppingBag, ArrowLeft, Loader2 } from "lucide-react";
import { useCartStore } from "@/stores/cart-store";
import { useAuthStore } from "@/stores/auth-store";
import { createOrder } from "@/lib/api";
import { toast } from "sonner";
import Link from "next/link";
import Image from "next/image";
import { motion, AnimatePresence } from "framer-motion";

function formatPrice(price: number) {
  return new Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB",
    maximumFractionDigits: 0,
  }).format(price);
}

export default function CartPage() {
  const { cart, fetchCart, removeItem, addItem, clear } = useCartStore();
  const { isAuthenticated, setShowAuthModal } = useAuthStore();
  const [ordering, setOrdering] = useState(false);
  const router = useRouter();

  useEffect(() => {
    fetchCart();
  }, [fetchCart]);

  const handleOrder = async () => {
    const authState = useAuthStore.getState();
    if (!authState.isAuthenticated) {
      setShowAuthModal(true, () => handleOrder());
      return;
    }

    const { phone, email } = authState.user || {};
    if (!phone && !email) {
      toast.error("Не удалось определить контактные данные");
      return;
    }

    setOrdering(true);
    try {
 
      await fetchCart();
      await createOrder({ phone: phone || undefined, email: email || undefined });
      await clear();
      toast.success("Заказ оформлен!");
      router.push(`/orders`);
    } catch {
      toast.error("Не удалось оформить заказ");
    } finally {
      setOrdering(false);
    }
  };

  const items = cart?.items || [];
  const total = cart?.totalPrice || 0;

  if (items.length === 0) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="container mx-auto px-4 py-24 text-center space-y-4"
      >
        <div className="h-20 w-20 mx-auto rounded-full bg-secondary/50 flex items-center justify-center">
          <ShoppingBag className="h-10 w-10 text-muted-foreground/50" />
        </div>
        <h1 className="text-2xl font-bold">Корзина пуста</h1>
        <p className="text-muted-foreground">Добавьте товары из каталога</p>
        <Link href="/">
          <Button variant="outline" className="gap-2 rounded-full">
            <ArrowLeft className="h-4 w-4" /> В каталог
          </Button>
        </Link>
      </motion.div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8 space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Корзина</h1>
        <Button variant="ghost" size="sm" className="text-muted-foreground hover:text-destructive" onClick={clear}>
          Очистить
        </Button>
      </div>

      <div className="grid lg:grid-cols-3 gap-6">
        {/* Items */}
        <div className="lg:col-span-2 space-y-3">
          <AnimatePresence mode="popLayout">
            {items.map((item) => (
              <motion.div
                key={item.productId}
                layout
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                exit={{ opacity: 0, x: 20, transition: { duration: 0.2 } }}
              >
                <Card className="border-border/40">
                  <CardContent className="flex items-center gap-4 p-4">
                    <div className="product-image relative h-20 w-20 shrink-0 rounded-xl bg-secondary/30 overflow-hidden">
                      {item.imageUrl ? (
                        <Image
                          src={item.imageUrl}
                          alt={item.productName}
                          fill
                          className="object-contain p-2"
                          sizes="80px"
                        />
                      ) : (
                        <div className="flex items-center justify-center h-full text-xs text-muted-foreground">
                          Нет фото
                        </div>
                      )}
                    </div>
                    <div className="flex-1 min-w-0">
                      <Link
                        href={`/product/${item.productId}`}
                        className="font-medium text-sm hover:text-primary transition-colors line-clamp-2"
                      >
                        {item.productName}
                      </Link>
                      <p className="font-bold mt-1">{formatPrice(item.price)}</p>
                    </div>
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-medium w-8 text-center text-muted-foreground">
                        x{item.quantity}
                      </span>
                    </div>
                    <Button
                      variant="ghost"
                      size="icon"
                      className="shrink-0 text-muted-foreground hover:text-destructive hover:bg-destructive/10 transition-colors"
                      onClick={() => removeItem(item.productId)}
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </CardContent>
                </Card>
              </motion.div>
            ))}
          </AnimatePresence>
        </div>

        {/* Summary */}
        <div>
          <Card className="sticky top-24 border-border/40">
            <CardContent className="p-6 space-y-4">
              <h2 className="font-semibold text-lg">Итого</h2>
              <div className="space-y-3 text-sm">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Товаров</span>
                  <span>{items.length}</span>
                </div>
                <Separator />
                <div className="flex justify-between text-xl font-bold">
                  <span>Сумма</span>
                  <span className="text-primary">{formatPrice(total)}</span>
                </div>
              </div>
              <Button
                className="w-full rounded-xl shadow-md shadow-primary/20"
                size="lg"
                onClick={handleOrder}
                disabled={ordering}
              >
                {ordering ? (
                  <Loader2 className="h-4 w-4 animate-spin" />
                ) : (
                  "Оформить заказ"
                )}
              </Button>
              <p className="text-xs text-muted-foreground text-center">
                Оплата при получении в ПВЗ
              </p>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}

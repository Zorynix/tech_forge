"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { getOrders } from "@/lib/api";
import { useAuthStore } from "@/stores/auth-store";
import type { Order, PageResponse } from "@/types";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { Package, Clock, CheckCircle, XCircle, Truck, ArrowLeft } from "lucide-react";
import Link from "next/link";
import { motion } from "framer-motion";

const STATUS_CONFIG: Record<string, { label: string; variant: "default" | "secondary" | "destructive" | "outline"; icon: typeof Package; color: string }> = {
  CREATED: { label: "Создан", variant: "secondary", icon: Clock, color: "text-muted-foreground" },
  CONFIRMED: { label: "Подтверждён", variant: "outline", icon: CheckCircle, color: "text-blue-500" },
  PROCESSING: { label: "Собирается", variant: "default", icon: Package, color: "text-primary" },
  READY_FOR_PICKUP: { label: "Готов к выдаче", variant: "default", icon: Truck, color: "text-green-500" },
  COMPLETED: { label: "Выдан", variant: "secondary", icon: CheckCircle, color: "text-green-500" },
  CANCELLED: { label: "Отменён", variant: "destructive", icon: XCircle, color: "text-destructive" },
};

function formatPrice(price: number) {
  return new Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB",
    maximumFractionDigits: 0,
  }).format(price);
}

function formatDate(dateStr: string) {
  return new Intl.DateTimeFormat("ru-RU", {
    day: "numeric",
    month: "long",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date(dateStr));
}

export default function OrdersPage() {
  const { isAuthenticated, isLoading: authLoading, setShowAuthModal } = useAuthStore();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    if (authLoading) return;
    if (!isAuthenticated) {
      setShowAuthModal(true);
      setLoading(false);
      return;
    }
    getOrders()
      .then((data) => setOrders(data.content))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [isAuthenticated, authLoading, setShowAuthModal]);

  if (loading || authLoading) {
    return (
      <div className="container mx-auto px-4 py-8 space-y-4">
        <Skeleton className="h-8 w-48" />
        {Array.from({ length: 3 }).map((_, i) => (
          <Skeleton key={i} className="h-36 rounded-2xl" />
        ))}
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="container mx-auto px-4 py-24 text-center space-y-4">
        <p className="text-lg text-muted-foreground">Войдите, чтобы увидеть заказы</p>
        <Button onClick={() => setShowAuthModal(true)}>Войти</Button>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8 space-y-6">
      <h1 className="text-2xl font-bold">Мои заказы</h1>

      {orders.length === 0 ? (
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center py-24 space-y-4"
        >
          <div className="h-20 w-20 mx-auto rounded-full bg-secondary/50 flex items-center justify-center">
            <Package className="h-10 w-10 text-muted-foreground/50" />
          </div>
          <p className="text-lg text-muted-foreground">У вас пока нет заказов</p>
          <Link href="/">
            <Button variant="outline" className="gap-2 rounded-full">
              <ArrowLeft className="h-4 w-4" /> В каталог
            </Button>
          </Link>
        </motion.div>
      ) : (
        <div className="space-y-4">
          {orders.map((order, i) => {
            const config = STATUS_CONFIG[order.status] || STATUS_CONFIG.CREATED;
            const StatusIcon = config.icon;
            return (
              <motion.div
                key={order.id}
                initial={{ opacity: 0, y: 15 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: i * 0.05 }}
              >
                <Card className="overflow-hidden border-border/40 hover:border-border/70 transition-colors">
                  <CardContent className="p-5">
                    <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 mb-4">
                      <div>
                        <p className="text-sm text-muted-foreground">
                          Заказ от {formatDate(order.createdAt)}
                        </p>
                        <p className="text-xs text-muted-foreground font-mono mt-0.5">
                          #{order.id.slice(0, 8)}
                        </p>
                      </div>
                      <Badge variant={config.variant} className="gap-1.5 w-fit">
                        <StatusIcon className={`h-3 w-3 ${config.color}`} />
                        {config.label}
                      </Badge>
                    </div>
                    <div className="space-y-2">
                      {order.items.map((item, j) => (
                        <div key={j} className="flex justify-between text-sm py-1">
                          <span className="text-muted-foreground">
                            {item.productName} <span className="text-foreground/40">x{item.quantity}</span>
                          </span>
                          <span className="font-medium">{formatPrice(item.price * item.quantity)}</span>
                        </div>
                      ))}
                    </div>
                    <div className="flex justify-between items-center mt-4 pt-3 border-t border-border/50">
                      <span className="font-semibold text-muted-foreground">Итого</span>
                      <span className="font-bold text-lg">{formatPrice(order.totalPrice)}</span>
                    </div>
                  </CardContent>
                </Card>
              </motion.div>
            );
          })}
        </div>
      )}
    </div>
  );
}

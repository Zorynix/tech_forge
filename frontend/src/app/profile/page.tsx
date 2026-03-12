"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/stores/auth-store";
import { updateMe, generateTelegramLink } from "@/lib/api";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { User, MessageCircle, ExternalLink, Loader2 } from "lucide-react";
import { toast } from "sonner";
import { motion } from "framer-motion";

export default function ProfilePage() {
  const { user, isAuthenticated, isLoading, fetchUser } = useAuthStore();
  const router = useRouter();
  const [name, setName] = useState("");
  const [saving, setSaving] = useState(false);
  const [generatingLink, setGeneratingLink] = useState(false);

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.push("/");
    }
  }, [isLoading, isAuthenticated, router]);

  useEffect(() => {
    if (user?.name) {
      setName(user.name);
    }
  }, [user]);

  const handleSaveName = async () => {
    if (!name.trim()) return;
    setSaving(true);
    try {
      await updateMe(name.trim());
      await fetchUser();
      toast.success("Имя обновлено");
    } catch {
      toast.error("Не удалось обновить имя");
    } finally {
      setSaving(false);
    }
  };

  const handleLinkTelegram = async () => {
    setGeneratingLink(true);
    try {
      const data = await generateTelegramLink();
      window.open(data.botLink, "_blank");
    } catch {
      toast.error("Не удалось сгенерировать ссылку");
    } finally {
      setGeneratingLink(false);
    }
  };

  if (isLoading || !user) {
    return (
      <div className="container mx-auto px-4 py-8 flex justify-center">
        <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-2xl space-y-6">
      <motion.h1
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="text-2xl font-bold"
      >
        Профиль
      </motion.h1>

      <motion.div
        initial={{ opacity: 0, y: 15 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.05 }}
      >
        <Card className="border-border/40">
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-lg">
              <div className="h-8 w-8 rounded-full bg-primary/10 flex items-center justify-center">
                <User className="h-4 w-4 text-primary" />
              </div>
              Личные данные
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <label className="text-sm text-muted-foreground">Телефон</label>
              <p className="font-medium">{user.phone}</p>
            </div>
            {user.email && (
              <div>
                <label className="text-sm text-muted-foreground">Почта</label>
                <p className="font-medium">{user.email}</p>
              </div>
            )}
            <div className="space-y-2">
              <label className="text-sm text-muted-foreground">Имя</label>
              <div className="flex gap-2">
                <Input
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Введите ваше имя"
                />
                <Button onClick={handleSaveName} disabled={saving || !name.trim()}>
                  {saving ? <Loader2 className="h-4 w-4 animate-spin" /> : "Сохранить"}
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.div>

      <motion.div
        initial={{ opacity: 0, y: 15 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.1 }}
      >
        <Card className="border-border/40">
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-lg">
              <div className="h-8 w-8 rounded-full bg-primary/10 flex items-center justify-center">
                <MessageCircle className="h-4 w-4 text-primary" />
              </div>
              Telegram-уведомления
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {user.telegramChatId ? (
              <div className="flex items-center gap-2 text-sm px-3 py-2 rounded-lg bg-green-500/10">
                <div className="h-2 w-2 rounded-full bg-green-500" />
                <span className="text-green-700 dark:text-green-400 font-medium">
                  Telegram привязан
                </span>
              </div>
            ) : (
              <>
                <p className="text-sm text-muted-foreground">
                  Привяжите Telegram чтобы получать уведомления о статусе заказов.
                </p>
                <Button onClick={handleLinkTelegram} disabled={generatingLink} className="rounded-xl">
                  {generatingLink ? (
                    <Loader2 className="h-4 w-4 animate-spin mr-2" />
                  ) : (
                    <MessageCircle className="h-4 w-4 mr-2" />
                  )}
                  Привязать Telegram
                  <ExternalLink className="ml-2 h-4 w-4" />
                </Button>
              </>
            )}
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}

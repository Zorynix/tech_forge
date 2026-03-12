"use client";

import { useState } from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useAuthStore } from "@/stores/auth-store";
import { Phone, Mail, ArrowRight, ArrowLeft, Loader2, Sparkles } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";

type AuthMethod = "phone" | "email";
type Step = "choose" | "input" | "code";

const slideVariants = {
  enter: (direction: number) => ({ x: direction > 0 ? 80 : -80, opacity: 0 }),
  center: { x: 0, opacity: 1 },
  exit: (direction: number) => ({ x: direction > 0 ? -80 : 80, opacity: 0 }),
};

export function AuthModal() {
  const { showAuthModal, setShowAuthModal, requestCode, verify } = useAuthStore();
  const [step, setStep] = useState<Step>("choose");
  const [method, setMethod] = useState<AuthMethod>("phone");
  const [identifier, setIdentifier] = useState("");
  const [code, setCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [direction, setDirection] = useState(1);

  const goTo = (newStep: Step, dir: number = 1) => {
    setDirection(dir);
    setStep(newStep);
  };

  const handleRequestCode = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!identifier.trim()) return;
    setLoading(true);
    setError("");
    try {
      await requestCode(identifier, method);
      goTo("code", 1);
    } catch {
      setError(
        method === "phone"
          ? "Не удалось отправить код. Проверьте номер."
          : "Не удалось отправить код. Проверьте адрес."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!code.trim()) return;
    setLoading(true);
    setError("");
    try {
      await verify(identifier, code, method);
      handleClose();
    } catch {
      setError("Неверный код. Попробуйте ещё раз.");
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setShowAuthModal(false);
    setStep("choose");
    setMethod("phone");
    setIdentifier("");
    setCode("");
    setError("");
    setDirection(1);
  };

  const selectMethod = (m: AuthMethod) => {
    setMethod(m);
    setIdentifier("");
    setError("");
    goTo("input", 1);
  };

  return (
    <Dialog open={showAuthModal} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-md overflow-hidden">
        <DialogHeader>
          <DialogTitle className="text-center text-xl">
            {step === "choose" && "Вход в Tech Forge"}
            {step === "input" && (method === "phone" ? "Вход по телефону" : "Вход по почте")}
            {step === "code" && "Введите код"}
          </DialogTitle>
        </DialogHeader>

        <AnimatePresence mode="wait" custom={direction}>
          {step === "choose" && (
            <motion.div
              key="choose"
              custom={direction}
              variants={slideVariants}
              initial="enter"
              animate="center"
              exit="exit"
              transition={{ duration: 0.2, ease: "easeInOut" }}
              className="space-y-3"
            >
              <p className="text-sm text-muted-foreground text-center">
                Выберите способ авторизации
              </p>
              <button
                className="w-full flex items-center gap-4 p-4 rounded-xl border border-border/50 hover:border-primary/30 hover:bg-primary/5 transition-all duration-200 text-left"
                onClick={() => selectMethod("phone")}
              >
                <div className="h-10 w-10 rounded-full bg-primary/10 flex items-center justify-center shrink-0">
                  <Phone className="h-5 w-5 text-primary" />
                </div>
                <div>
                  <p className="font-medium text-sm">По номеру телефона</p>
                  <p className="text-xs text-muted-foreground">SMS с кодом подтверждения</p>
                </div>
              </button>
              <button
                className="w-full flex items-center gap-4 p-4 rounded-xl border border-border/50 hover:border-primary/30 hover:bg-primary/5 transition-all duration-200 text-left"
                onClick={() => selectMethod("email")}
              >
                <div className="h-10 w-10 rounded-full bg-primary/10 flex items-center justify-center shrink-0">
                  <Mail className="h-5 w-5 text-primary" />
                </div>
                <div>
                  <p className="font-medium text-sm">По электронной почте</p>
                  <p className="text-xs text-muted-foreground">Код на вашу почту</p>
                </div>
              </button>
            </motion.div>
          )}

          {step === "input" && (
            <motion.div
              key="input"
              custom={direction}
              variants={slideVariants}
              initial="enter"
              animate="center"
              exit="exit"
              transition={{ duration: 0.2, ease: "easeInOut" }}
            >
              <form onSubmit={handleRequestCode} className="space-y-4">
                <p className="text-sm text-muted-foreground text-center">
                  {method === "phone"
                    ? "Введите номер телефона для получения кода"
                    : "Введите email для получения кода"}
                </p>
                <div className="relative">
                  {method === "phone" ? (
                    <Phone className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  ) : (
                    <Mail className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  )}
                  <Input
                    type={method === "phone" ? "tel" : "email"}
                    placeholder={method === "phone" ? "+7 (999) 123-45-67" : "user@example.com"}
                    className="pl-10 h-12 rounded-xl"
                    value={identifier}
                    onChange={(e) => setIdentifier(e.target.value)}
                    autoFocus
                  />
                </div>
                {error && <p className="text-sm text-destructive text-center">{error}</p>}
                <Button type="submit" className="w-full h-11 rounded-xl" disabled={loading || !identifier.trim()}>
                  {loading ? (
                    <Loader2 className="h-4 w-4 animate-spin" />
                  ) : (
                    <>
                      Получить код <ArrowRight className="ml-2 h-4 w-4" />
                    </>
                  )}
                </Button>
                <Button
                  type="button"
                  variant="ghost"
                  className="w-full"
                  onClick={() => goTo("choose", -1)}
                >
                  <ArrowLeft className="mr-2 h-4 w-4" />
                  Другой способ
                </Button>
              </form>
            </motion.div>
          )}

          {step === "code" && (
            <motion.div
              key="code"
              custom={direction}
              variants={slideVariants}
              initial="enter"
              animate="center"
              exit="exit"
              transition={{ duration: 0.2, ease: "easeInOut" }}
            >
              <form onSubmit={handleVerify} className="space-y-4">
                <p className="text-sm text-muted-foreground text-center">
                  Код отправлен на{" "}
                  <span className="font-medium text-foreground">{identifier}</span>
                </p>
                <Input
                  type="text"
                  inputMode="numeric"
                  placeholder="000000"
                  maxLength={6}
                  className="text-center text-2xl tracking-[0.5em] font-mono h-14 rounded-xl"
                  value={code}
                  onChange={(e) => setCode(e.target.value.replace(/\D/g, "").slice(0, 6))}
                  autoFocus
                />
                {error && <p className="text-sm text-destructive text-center">{error}</p>}
                <Button type="submit" className="w-full h-11 rounded-xl" disabled={loading || code.length !== 6}>
                  {loading ? (
                    <Loader2 className="h-4 w-4 animate-spin" />
                  ) : (
                    <>
                      <Sparkles className="mr-2 h-4 w-4" />
                      Подтвердить
                    </>
                  )}
                </Button>
                <Button
                  type="button"
                  variant="ghost"
                  className="w-full"
                  onClick={() => {
                    goTo("input", -1);
                    setCode("");
                    setError("");
                  }}
                >
                  <ArrowLeft className="mr-2 h-4 w-4" />
                  {method === "phone" ? "Изменить номер" : "Изменить email"}
                </Button>
              </form>
            </motion.div>
          )}
        </AnimatePresence>
      </DialogContent>
    </Dialog>
  );
}

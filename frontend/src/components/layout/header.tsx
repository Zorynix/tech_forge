"use client";

import Link from "next/link";
import Image from "next/image";
import { ShoppingCart, User, Search, Menu, Package, LogOut, Sun, Moon, LayoutGrid } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Sheet, SheetContent, SheetTrigger, SheetTitle } from "@/components/ui/sheet";
import { useAuthStore } from "@/stores/auth-store";
import { useCartStore } from "@/stores/cart-store";
import { useRouter, useSearchParams } from "next/navigation";
import { useState, useEffect, useCallback, Suspense } from "react";
import { useTheme } from "next-themes";
import { motion, AnimatePresence } from "framer-motion";

function ThemeToggle() {
  const { theme, setTheme } = useTheme();
  const [mounted, setMounted] = useState(false);

  useEffect(() => setMounted(true), []);

  if (!mounted) {
    return <Button variant="ghost" size="icon" className="h-9 w-9" />;
  }

  const isDark = theme === "dark";

  const toggle = () => {
    document.documentElement.classList.add("transitioning");
    setTheme(isDark ? "light" : "dark");
    setTimeout(() => document.documentElement.classList.remove("transitioning"), 350);
  };

  return (
    <Button
      variant="ghost"
      size="icon"
      className="h-9 w-9 relative overflow-hidden"
      onClick={toggle}
    >
      <AnimatePresence mode="wait" initial={false}>
        {isDark ? (
          <motion.div
            key="sun"
            initial={{ rotate: -90, opacity: 0, scale: 0 }}
            animate={{ rotate: 0, opacity: 1, scale: 1 }}
            exit={{ rotate: 90, opacity: 0, scale: 0 }}
            transition={{ duration: 0.2 }}
          >
            <Sun className="h-4 w-4" />
          </motion.div>
        ) : (
          <motion.div
            key="moon"
            initial={{ rotate: 90, opacity: 0, scale: 0 }}
            animate={{ rotate: 0, opacity: 1, scale: 1 }}
            exit={{ rotate: -90, opacity: 0, scale: 0 }}
            transition={{ duration: 0.2 }}
          >
            <Moon className="h-4 w-4" />
          </motion.div>
        )}
      </AnimatePresence>
    </Button>
  );
}

function SearchInput() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [query, setQuery] = useState(searchParams.get("q") || "");
  const [focused, setFocused] = useState(false);

  const handleSearch = useCallback(
    (e: React.FormEvent) => {
      e.preventDefault();
      if (query.trim()) {
        router.push(`/search?q=${encodeURIComponent(query.trim())}`);
      }
    },
    [query, router]
  );

  return (
    <form onSubmit={handleSearch} className="relative flex-1 max-w-md">
      <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground pointer-events-none" />
      <Input
        type="search"
        placeholder="Поиск товаров..."
        className={`pl-10 bg-secondary/50 border border-border/30 focus-visible:ring-1 focus-visible:ring-primary/40 focus-visible:border-primary/30 transition-all duration-200 rounded-lg ${
          focused ? "bg-secondary/80 shadow-sm border-primary/20" : ""
        }`}
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        onFocus={() => setFocused(true)}
        onBlur={() => setFocused(false)}
      />
    </form>
  );
}

export function Header() {
  const { user, isAuthenticated, setShowAuthModal, logout } = useAuthStore();
  const { itemCount, fetchCart } = useCartStore();
  const [mobileOpen, setMobileOpen] = useState(false);
  const router = useRouter();

  useEffect(() => {
    fetchCart();
  }, [fetchCart, isAuthenticated]);

  const handleCartClick = () => {
    router.push("/cart");
  };

  return (
    <header className="sticky top-0 z-50 w-full bg-background/80 backdrop-blur-xl border-b border-border/40">
      {/* Thin accent line at top */}
      <div className="h-[2px] bg-gradient-to-r from-transparent via-primary/60 to-transparent" />

      <div className="container mx-auto flex h-16 items-center gap-4 px-4">
        {/* Mobile menu */}
        <Sheet open={mobileOpen} onOpenChange={setMobileOpen}>
          <SheetTrigger
            className="md:hidden"
            render={<Button variant="ghost" size="icon" />}
          >
            <Menu className="h-5 w-5" />
          </SheetTrigger>
          <SheetContent side="left" className="w-72">
            <SheetTitle className="sr-only">Меню навигации</SheetTitle>
            <div className="flex items-center gap-3 mb-8 mt-4">
              <Image src="/logo.png" alt="Tech Forge" width={40} height={40} className="rounded-lg" />
              <span className="font-bold text-lg">Tech Forge</span>
            </div>
            <nav className="flex flex-col gap-4">
              <Link
                href="/"
                onClick={() => setMobileOpen(false)}
                className="text-lg font-medium hover:text-primary transition-colors"
              >
                Главная
              </Link>
              <Link
                href="/catalog"
                onClick={() => setMobileOpen(false)}
                className="text-lg font-medium hover:text-primary transition-colors"
              >
                Каталог
              </Link>
              {isAuthenticated && (
                <Link
                  href="/orders"
                  onClick={() => setMobileOpen(false)}
                  className="text-lg font-medium hover:text-primary transition-colors"
                >
                  Мои заказы
                </Link>
              )}
            </nav>
          </SheetContent>
        </Sheet>

        {/* Logo */}
        <Link href="/" className="flex items-center gap-2.5 shrink-0 group">
          <Image
            src="/logo.png"
            alt="Tech Forge"
            width={36}
            height={36}
            className="rounded-lg transition-transform duration-300 group-hover:scale-105"
          />
          <div className="hidden sm:flex flex-col leading-none">
            <span className="font-bold text-base tracking-wide">Tech Forge</span>
            <span className="text-[10px] text-muted-foreground tracking-[0.2em]">テックフォージ</span>
          </div>
        </Link>

        {/* Nav links */}
        <nav className="hidden md:flex items-center gap-1 ml-2">
          <Link href="/catalog">
            <Button variant="ghost" size="sm" className="gap-1.5 text-muted-foreground hover:text-foreground">
              <LayoutGrid className="h-4 w-4" />
              Каталог
            </Button>
          </Link>
          {isAuthenticated && (
            <Link href="/orders">
              <Button variant="ghost" size="sm" className="gap-1.5 text-muted-foreground hover:text-foreground">
                <Package className="h-4 w-4" />
                Заказы
              </Button>
            </Link>
          )}
        </nav>

        {/* Search */}
        <div className="hidden md:flex flex-1 justify-center">
          <Suspense>
            <SearchInput />
          </Suspense>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-1 ml-auto">
          <ThemeToggle />

          {/* Cart */}
          <Button variant="ghost" size="icon" className="relative h-9 w-9" onClick={handleCartClick}>
            <ShoppingCart className="h-4 w-4" />
            <AnimatePresence>
              {itemCount > 0 && (
                <motion.div
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  exit={{ scale: 0 }}
                  transition={{ type: "spring", stiffness: 500, damping: 25 }}
                >
                  <Badge className="absolute -top-1 -right-1 h-5 w-5 rounded-full p-0 flex items-center justify-center text-[10px] bg-primary text-primary-foreground shadow-sm shadow-primary/30">
                    {itemCount}
                  </Badge>
                </motion.div>
              )}
            </AnimatePresence>
          </Button>

          {/* User */}
          {isAuthenticated ? (
            <DropdownMenu>
              <DropdownMenuTrigger render={<Button variant="ghost" size="icon" className="h-9 w-9" />}>
                <User className="h-4 w-4" />
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-48">
                <div className="px-2 py-1.5 text-sm font-medium">
                  {user?.name || user?.phone || user?.email}
                </div>
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={() => router.push("/profile")}>
                  <User className="mr-2 h-4 w-4" />
                  Профиль
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => router.push("/orders")}>
                  <Package className="mr-2 h-4 w-4" />
                  Мои заказы
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={logout}>
                  <LogOut className="mr-2 h-4 w-4" />
                  Выйти
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <Button
              size="sm"
              className="ml-1 shadow-sm shadow-primary/20"
              onClick={() => setShowAuthModal(true)}
            >
              Войти
            </Button>
          )}
        </div>
      </div>

      {/* Mobile search */}
      <div className="md:hidden px-4 pb-3">
        <Suspense>
          <SearchInput />
        </Suspense>
      </div>
    </header>
  );
}

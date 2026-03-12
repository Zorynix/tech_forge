import Link from "next/link";
import Image from "next/image";

export function Footer() {
  return (
    <footer className="border-t border-border/40 mt-auto">
      {/* Gold accent line */}
      <div className="gold-line" />

      <div className="container mx-auto px-4 py-10">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div>
            <div className="flex items-center gap-3 mb-4">
              <Image src="/logo.png" alt="Tech Forge" width={36} height={36} className="rounded-lg" />
              <div className="leading-none">
                <span className="font-bold text-lg">Tech Forge</span>
                <p className="text-[10px] text-muted-foreground tracking-[0.2em] mt-0.5">テックフォージ</p>
              </div>
            </div>
            <p className="text-sm text-muted-foreground leading-relaxed">
              Магазин цифровой техники нового поколения. Доставка и уведомления через Telegram.
            </p>
          </div>
          <div>
            <h3 className="font-semibold mb-4 text-xs uppercase tracking-[0.2em] text-gold">
              Навигация
            </h3>
            <nav className="flex flex-col gap-2.5 text-sm">
              <Link href="/" className="text-muted-foreground hover:text-primary transition-colors w-fit">
                Главная
              </Link>
              <Link href="/catalog" className="text-muted-foreground hover:text-primary transition-colors w-fit">
                Каталог
              </Link>
              <Link href="/cart" className="text-muted-foreground hover:text-primary transition-colors w-fit">
                Корзина
              </Link>
              <Link href="/orders" className="text-muted-foreground hover:text-primary transition-colors w-fit">
                Мои заказы
              </Link>
              <Link href="/about" className="text-muted-foreground hover:text-primary transition-colors w-fit">
                О нас
              </Link>
              <Link href="/roadmap" className="text-muted-foreground hover:text-primary transition-colors w-fit">
                Roadmap
              </Link>
            </nav>
          </div>
          <div>
            <h3 className="font-semibold mb-4 text-xs uppercase tracking-[0.2em] text-gold">
              Контакты
            </h3>
            <div className="flex flex-col gap-2.5 text-sm text-muted-foreground">
              <span>Telegram: @tmarketZBot</span>
              <span>Пн-Вс: 9:00 — 21:00</span>
            </div>
          </div>
        </div>
        <div className="gold-line mt-8 mb-4" />
        <div className="flex flex-col sm:flex-row items-center justify-between gap-2 text-xs text-muted-foreground">
          <span>&copy; {new Date().getFullYear()} Tech Forge. Все права защищены.</span>
          <div className="flex items-center gap-3">
            <Link href="/privacy" className="hover:text-primary transition-colors">
              Политика конфиденциальности
            </Link>
            <span className="tracking-[0.15em]">品質と信頼</span>
          </div>
        </div>
      </div>
    </footer>
  );
}

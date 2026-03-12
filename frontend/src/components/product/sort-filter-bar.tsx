"use client";

import { ArrowUpDown, ArrowDown, ArrowUp, Star, MessageSquare, TrendingUp, Clock, DollarSign, Filter } from "lucide-react";
import { motion, AnimatePresence } from "framer-motion";
import { useState } from "react";
import { Button } from "@/components/ui/button";

export interface SortOption {
  label: string;
  value: string;
  icon: React.ReactNode;
}

const SORT_OPTIONS: SortOption[] = [
  { label: "По умолчанию", value: "", icon: <ArrowUpDown className="h-3.5 w-3.5" /> },
  { label: "Сначала высокий рейтинг", value: "averageRating,desc", icon: <Star className="h-3.5 w-3.5" /> },
  { label: "Сначала низкий рейтинг", value: "averageRating,asc", icon: <Star className="h-3.5 w-3.5" /> },
  { label: "Больше отзывов", value: "reviewCount,desc", icon: <MessageSquare className="h-3.5 w-3.5" /> },
  { label: "Меньше отзывов", value: "reviewCount,asc", icon: <MessageSquare className="h-3.5 w-3.5" /> },
  { label: "Сначала дешёвые", value: "price,asc", icon: <DollarSign className="h-3.5 w-3.5" /> },
  { label: "Сначала дорогие", value: "price,desc", icon: <DollarSign className="h-3.5 w-3.5" /> },
  { label: "Сначала новые", value: "createdAt,desc", icon: <Clock className="h-3.5 w-3.5" /> },
  { label: "Популярные", value: "reviewCount,desc", icon: <TrendingUp className="h-3.5 w-3.5" /> },
];

// Remove duplicate "Больше отзывов" / "Популярные" — keep unique values
const uniqueOptions = SORT_OPTIONS.filter(
  (opt, i, arr) => arr.findIndex((o) => o.value === opt.value && o.label === opt.label) === i
);

interface PriceRange {
  min: number | null;
  max: number | null;
}

interface SortFilterBarProps {
  sort: string;
  onSortChange: (sort: string) => void;
  priceRange?: PriceRange;
  onPriceRangeChange?: (range: PriceRange) => void;
  inStockOnly?: boolean;
  onInStockOnlyChange?: (val: boolean) => void;
  totalResults?: number;
}

export function SortFilterBar({
  sort,
  onSortChange,
  priceRange,
  onPriceRangeChange,
  inStockOnly,
  onInStockOnlyChange,
  totalResults,
}: SortFilterBarProps) {
  const [showFilters, setShowFilters] = useState(false);

  const currentOption = uniqueOptions.find((o) => o.value === sort) || uniqueOptions[0];

  return (
    <div className="space-y-3">
      {/* Sort bar */}
      <div className="flex flex-wrap items-center gap-2">
        {/* Sort pills */}
        <div className="flex flex-wrap gap-1.5">
          {uniqueOptions.map((option) => {
            const isActive = sort === option.value;
            return (
              <button
                key={option.label}
                onClick={() => onSortChange(option.value)}
                className={`relative inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-medium transition-all duration-200 ${
                  isActive
                    ? "text-primary-foreground"
                    : "text-muted-foreground hover:text-foreground hover:bg-secondary/60"
                }`}
              >
                {isActive && (
                  <motion.div
                    layoutId="sort-pill"
                    className="absolute inset-0 rounded-lg bg-primary shadow-md shadow-primary/20"
                    transition={{ type: "spring", stiffness: 400, damping: 30 }}
                  />
                )}
                <span className="relative z-10 flex items-center gap-1.5">
                  {option.icon}
                  {option.label}
                </span>
              </button>
            );
          })}
        </div>

        {/* Filter button */}
        {(onPriceRangeChange || onInStockOnlyChange) && (
          <Button
            variant="outline"
            size="sm"
            onClick={() => setShowFilters(!showFilters)}
            className={`gap-1.5 rounded-lg ml-auto ${showFilters ? "border-primary/30 text-primary" : ""}`}
          >
            <Filter className="h-3.5 w-3.5" />
            Фильтры
            {((priceRange?.min != null && priceRange.min > 0) ||
              (priceRange?.max != null) ||
              inStockOnly) && (
              <span className="h-1.5 w-1.5 rounded-full bg-primary" />
            )}
          </Button>
        )}
      </div>

      {/* Filter panel */}
      <AnimatePresence>
        {showFilters && (onPriceRangeChange || onInStockOnlyChange) && (
          <motion.div
            initial={{ height: 0, opacity: 0 }}
            animate={{ height: "auto", opacity: 1 }}
            exit={{ height: 0, opacity: 0 }}
            transition={{ duration: 0.2 }}
            className="overflow-hidden"
          >
            <div className="flex flex-wrap items-end gap-4 p-4 rounded-xl border border-border/40 bg-card">
              {/* Price filter */}
              {onPriceRangeChange && (
                <div className="flex items-end gap-2">
                  <div>
                    <label className="text-xs text-muted-foreground mb-1 block">Цена от</label>
                    <input
                      type="number"
                      placeholder="0"
                      value={priceRange?.min ?? ""}
                      onChange={(e) =>
                        onPriceRangeChange({
                          min: e.target.value ? Number(e.target.value) : null,
                          max: priceRange?.max ?? null,
                        })
                      }
                      className="w-28 h-8 px-3 rounded-lg border border-border/40 bg-secondary/30 text-sm focus:outline-none focus:ring-1 focus:ring-primary/40 focus:border-primary/30"
                    />
                  </div>
                  <span className="text-muted-foreground text-sm pb-1">—</span>
                  <div>
                    <label className="text-xs text-muted-foreground mb-1 block">до</label>
                    <input
                      type="number"
                      placeholder="∞"
                      value={priceRange?.max ?? ""}
                      onChange={(e) =>
                        onPriceRangeChange({
                          min: priceRange?.min ?? null,
                          max: e.target.value ? Number(e.target.value) : null,
                        })
                      }
                      className="w-28 h-8 px-3 rounded-lg border border-border/40 bg-secondary/30 text-sm focus:outline-none focus:ring-1 focus:ring-primary/40 focus:border-primary/30"
                    />
                  </div>
                  <span className="text-xs text-muted-foreground pb-1.5">₽</span>
                </div>
              )}

              {/* In stock filter */}
              {onInStockOnlyChange && (
                <label className="flex items-center gap-2 cursor-pointer pb-0.5">
                  <input
                    type="checkbox"
                    checked={inStockOnly ?? false}
                    onChange={(e) => onInStockOnlyChange(e.target.checked)}
                    className="h-4 w-4 rounded border-border accent-primary"
                  />
                  <span className="text-sm">Только в наличии</span>
                </label>
              )}

              {/* Reset */}
              {((priceRange?.min != null && priceRange.min > 0) ||
                priceRange?.max != null ||
                inStockOnly) && (
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => {
                    onPriceRangeChange?.({ min: null, max: null });
                    onInStockOnlyChange?.(false);
                  }}
                  className="text-xs text-muted-foreground"
                >
                  Сбросить
                </Button>
              )}
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}

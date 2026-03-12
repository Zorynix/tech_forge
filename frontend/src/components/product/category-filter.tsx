"use client";

import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import type { Category } from "@/types";
import { motion } from "framer-motion";

interface CategoryFilterProps {
  categories: Category[];
  selectedId: string | null;
  onSelect: (id: string | null) => void;
}

function Pill({
  active,
  onClick,
  children,
}: {
  active: boolean;
  onClick: () => void;
  children: React.ReactNode;
}) {
  return (
    <button
      onClick={onClick}
      className={`relative shrink-0 px-4 py-2 rounded-lg text-sm font-medium transition-colors duration-200 ${
        active
          ? "text-primary-foreground"
          : "text-muted-foreground hover:text-foreground hover:bg-secondary/60"
      }`}
    >
      {active && (
        <motion.div
          layoutId="category-pill"
          className="absolute inset-0 rounded-lg bg-primary shadow-md shadow-primary/20"
          transition={{ type: "spring", stiffness: 400, damping: 30 }}
        />
      )}
      <span className="relative z-10">{children}</span>
    </button>
  );
}

export function CategoryFilter({ categories, selectedId, onSelect }: CategoryFilterProps) {
  const parentCategories = categories.filter((c) => !c.parentCategoryId);

  return (
    <ScrollArea className="w-full whitespace-nowrap">
      <div className="flex gap-1 pb-2 p-1 bg-secondary/30 rounded-lg border border-border/30 w-fit">
        <Pill active={selectedId === null} onClick={() => onSelect(null)}>
          Все
        </Pill>
        {parentCategories.map((category) => (
          <Pill
            key={category.id}
            active={selectedId === category.id}
            onClick={() => onSelect(category.id)}
          >
            {category.name}
          </Pill>
        ))}
      </div>
      <ScrollBar orientation="horizontal" />
    </ScrollArea>
  );
}

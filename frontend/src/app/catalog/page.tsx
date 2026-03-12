"use client";

import { useEffect, useState, useCallback } from "react";
import { useSearchParams } from "next/navigation";
import { getProducts, getCategories, getProductsByCategory } from "@/lib/api";
import { ProductGrid } from "@/components/product/product-grid";
import { CategoryFilter } from "@/components/product/category-filter";
import { SortFilterBar } from "@/components/product/sort-filter-bar";
import { Button } from "@/components/ui/button";
import type { Product, Category, PageResponse } from "@/types";
import { motion } from "framer-motion";
import { ArrowDown, LayoutGrid } from "lucide-react";
import { Suspense } from "react";

interface PriceRange { min: number | null; max: number | null }

function CatalogContent() {
  const searchParams = useSearchParams();
  const initialCategory = searchParams.get("category");

  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<string | null>(initialCategory);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [pageData, setPageData] = useState<PageResponse<Product> | null>(null);
  const [sort, setSort] = useState("");
  const [priceRange, setPriceRange] = useState<PriceRange>({ min: null, max: null });
  const [inStockOnly, setInStockOnly] = useState(false);

  const applyClientFilters = useCallback((items: Product[]) => {
    return items.filter((p) => {
      if (inStockOnly && p.stockQuantity === 0) return false;
      if (priceRange.min != null && p.price < priceRange.min) return false;
      if (priceRange.max != null && p.price > priceRange.max) return false;
      return true;
    });
  }, [inStockOnly, priceRange]);

  const fetchProducts = useCallback(async (categoryId: string | null, pageNum: number, sortParam: string) => {
    setLoading(true);
    try {
      const data = categoryId
        ? await getProductsByCategory(categoryId, pageNum, 12, sortParam || undefined)
        : await getProducts(pageNum, 12, sortParam || undefined);
      setProducts(pageNum === 0 ? data.content : (prev) => [...prev, ...data.content]);
      setPageData(data);
    } catch (e) {
      console.error("Failed to fetch products", e);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    getCategories().then(setCategories).catch(console.error);
    fetchProducts(initialCategory, 0, sort);
  }, [fetchProducts, initialCategory, sort]);

  const handleCategoryChange = (id: string | null) => {
    setSelectedCategory(id);
    setPage(0);
    setProducts([]);
    fetchProducts(id, 0, sort);
  };

  const handleSortChange = (newSort: string) => {
    setSort(newSort);
    setPage(0);
    setProducts([]);
    fetchProducts(selectedCategory, 0, newSort);
  };

  const handleLoadMore = () => {
    const next = page + 1;
    setPage(next);
    fetchProducts(selectedCategory, next, sort);
  };

  const displayProducts = applyClientFilters(products);

  return (
    <div className="min-h-screen">
      {/* Header section */}
      <section className="relative overflow-hidden border-b border-border/40">
        <div className="absolute inset-0 seigaiha opacity-50" />
        <div className="absolute inset-0 bg-gradient-to-b from-background/60 to-background" />
        <div className="container mx-auto px-4 py-10 relative">
          <motion.div
            initial={{ opacity: 0, y: 15 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.4 }}
          >
            <div className="flex items-center gap-3 mb-2">
              <div className="h-10 w-10 rounded-xl bg-primary/10 flex items-center justify-center">
                <LayoutGrid className="h-5 w-5 text-primary" />
              </div>
              <h1 className="text-3xl font-bold">Каталог</h1>
            </div>
            {pageData && (
              <p className="text-sm text-muted-foreground ml-[52px]">
                {pageData.totalElements} товаров
              </p>
            )}
          </motion.div>
        </div>
      </section>

      <div className="container mx-auto px-4 py-6 space-y-6 pb-12">
        {/* Categories */}
        {categories.length > 0 && (
          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.4, delay: 0.1 }}
          >
            <CategoryFilter
              categories={categories}
              selectedId={selectedCategory}
              onSelect={handleCategoryChange}
            />
          </motion.div>
        )}

        {/* Sort & Filter */}
        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: 0.15 }}
        >
          <SortFilterBar
            sort={sort}
            onSortChange={handleSortChange}
            priceRange={priceRange}
            onPriceRangeChange={setPriceRange}
            inStockOnly={inStockOnly}
            onInStockOnlyChange={setInStockOnly}
          />
        </motion.div>

        {/* Products */}
        <ProductGrid products={displayProducts} loading={loading && page === 0} />

        {/* Load more */}
        {pageData && !pageData.last && !loading && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="flex justify-center pt-4 pb-8"
          >
            <Button
              variant="outline"
              size="lg"
              onClick={handleLoadMore}
              className="gap-2 rounded-full px-8 border-gold/30 text-gold hover:bg-gold/5 hover:text-gold"
            >
              <ArrowDown className="h-4 w-4" />
              Показать ещё
            </Button>
          </motion.div>
        )}
      </div>
    </div>
  );
}

export default function CatalogPage() {
  return (
    <Suspense>
      <CatalogContent />
    </Suspense>
  );
}

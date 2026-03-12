"use client";

import { useEffect, useState, useCallback, Suspense } from "react";
import { useSearchParams } from "next/navigation";
import { searchProducts } from "@/lib/api";
import { ProductGrid } from "@/components/product/product-grid";
import { SortFilterBar } from "@/components/product/sort-filter-bar";
import { Button } from "@/components/ui/button";
import type { Product, PageResponse } from "@/types";
import { motion } from "framer-motion";
import { ArrowDown, Search } from "lucide-react";

interface PriceRange { min: number | null; max: number | null }

function SearchResults() {
  const searchParams = useSearchParams();
  const query = searchParams.get("q") || "";
  const [products, setProducts] = useState<Product[]>([]);
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

  useEffect(() => {
    if (!query) {
      setProducts([]);
      setLoading(false);
      return;
    }
    setLoading(true);
    setPage(0);
    searchProducts(query, 0, 12, sort || undefined)
      .then((data) => {
        setProducts(data.content);
        setPageData(data);
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [query, sort]);

  const handleSortChange = (newSort: string) => {
    setSort(newSort);
  };

  const handleLoadMore = () => {
    const next = page + 1;
    setPage(next);
    searchProducts(query, next, 12, sort || undefined).then((data) => {
      setProducts((prev) => [...prev, ...data.content]);
      setPageData(data);
    });
  };

  const displayProducts = applyClientFilters(products);

  return (
    <div className="container mx-auto px-4 py-8 space-y-6">
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="flex items-center gap-3 mb-1">
          <div className="h-8 w-8 rounded-full bg-primary/10 flex items-center justify-center">
            <Search className="h-4 w-4 text-primary" />
          </div>
          <h1 className="text-2xl font-bold">
            {query ? `Результаты: "${query}"` : "Поиск"}
          </h1>
        </div>
        {pageData && (
          <p className="text-sm text-muted-foreground ml-11">
            Найдено: {pageData.totalElements}
          </p>
        )}
      </motion.div>

      {/* Sort & Filter */}
      {query && (
        <SortFilterBar
          sort={sort}
          onSortChange={handleSortChange}
          priceRange={priceRange}
          onPriceRangeChange={setPriceRange}
          inStockOnly={inStockOnly}
          onInStockOnlyChange={setInStockOnly}
        />
      )}

      <ProductGrid products={displayProducts} loading={loading} />

      {pageData && !pageData.last && !loading && (
        <div className="flex justify-center pt-4">
          <Button variant="outline" size="lg" onClick={handleLoadMore} className="gap-2 rounded-full px-8">
            <ArrowDown className="h-4 w-4" />
            Показать ещё
          </Button>
        </div>
      )}
    </div>
  );
}

export default function SearchPage() {
  return (
    <Suspense>
      <SearchResults />
    </Suspense>
  );
}

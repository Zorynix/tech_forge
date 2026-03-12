"use client";

import type { ReviewSummary } from "@/types";

const CRITERIA_LABELS: Record<string, string> = {
  averageQuality: "Качество",
  averagePrice: "Цена / качество",
  averageDesign: "Дизайн",
  averageFeatures: "Функциональность",
  averageUsability: "Удобство",
};

export function RatingBreakdown({ summary }: { summary: ReviewSummary }) {
  const criteria = [
    { key: "averageQuality", value: summary.averageQuality },
    { key: "averagePrice", value: summary.averagePrice },
    { key: "averageDesign", value: summary.averageDesign },
    { key: "averageFeatures", value: summary.averageFeatures },
    { key: "averageUsability", value: summary.averageUsability },
  ];

  return (
    <div className="space-y-3">
      {criteria.map(({ key, value }) => (
        <div key={key} className="space-y-1">
          <div className="flex items-center justify-between text-sm">
            <span className="text-muted-foreground">{CRITERIA_LABELS[key]}</span>
            <span className="font-medium tabular-nums">{value.toFixed(1)}</span>
          </div>
          <div className="h-2 rounded-full bg-border/20 overflow-hidden">
            <div
              className="h-full rounded-full transition-all duration-700 ease-out"
              style={{
                width: `${(value / 10) * 100}%`,
                background: `linear-gradient(90deg, oklch(0.73 0.12 75 / 0.8), oklch(0.73 0.12 75))`,
              }}
            />
          </div>
        </div>
      ))}
    </div>
  );
}

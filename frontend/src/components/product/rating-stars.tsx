"use client";

import { Star } from "lucide-react";

interface RatingStarsProps {
  rating: number;
  maxRating?: number;
  size?: "sm" | "md" | "lg";
  showValue?: boolean;
}

export function RatingStars({ rating, maxRating = 10, size = "md", showValue = true }: RatingStarsProps) {
  const sizeClasses = {
    sm: "h-3 w-3",
    md: "h-4 w-4",
    lg: "h-5 w-5",
  };
  const textClasses = {
    sm: "text-xs",
    md: "text-sm",
    lg: "text-base",
  };
  const starSize = sizeClasses[size];
  const textSize = textClasses[size];

  // Convert 10-scale to 5-star display
  const starCount = 5;
  const normalizedRating = (rating / maxRating) * starCount;
  const fullStars = Math.floor(normalizedRating);
  const partialFill = normalizedRating - fullStars;

  return (
    <div className="flex items-center gap-1.5">
      <div className="flex items-center gap-0.5">
        {Array.from({ length: starCount }).map((_, i) => {
          let fillPercent = 0;
          if (i < fullStars) fillPercent = 100;
          else if (i === fullStars) fillPercent = Math.round(partialFill * 100);

          return (
            <div key={i} className="relative">
              <Star className={`${starSize} text-border/50`} />
              {fillPercent > 0 && (
                <div
                  className="absolute inset-0 overflow-hidden"
                  style={{ width: `${fillPercent}%` }}
                >
                  <Star className={`${starSize} text-gold fill-gold`} />
                </div>
              )}
            </div>
          );
        })}
      </div>
      {showValue && (
        <span className={`${textSize} font-semibold text-foreground`}>
          {rating.toFixed(1)}
        </span>
      )}
    </div>
  );
}

interface InteractiveRatingProps {
  value: number;
  onChange: (value: number) => void;
  maxRating?: number;
  label?: string;
}

export function InteractiveRating({ value, onChange, maxRating = 10, label }: InteractiveRatingProps) {
  return (
    <div className="space-y-1.5">
      {label && (
        <div className="flex items-center justify-between">
          <span className="text-sm text-muted-foreground">{label}</span>
          <span className="text-sm font-medium text-foreground">{value}/{maxRating}</span>
        </div>
      )}
      <div className="flex items-center gap-1">
        {Array.from({ length: maxRating }).map((_, i) => {
          const starValue = i + 1;
          const isActive = starValue <= value;
          return (
            <button
              key={i}
              type="button"
              onClick={() => onChange(starValue)}
              className="transition-all duration-150 hover:scale-125 focus:outline-none"
            >
              <Star
                className={`h-5 w-5 transition-colors ${
                  isActive
                    ? "text-gold fill-gold drop-shadow-[0_0_3px_oklch(0.73_0.12_75/0.5)]"
                    : "text-border/40 hover:text-gold/50"
                }`}
              />
            </button>
          );
        })}
      </div>
    </div>
  );
}

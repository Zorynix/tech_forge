"use client";

import { useEffect, useState } from "react";
import { getVideoReviews } from "@/lib/api";
import type { VideoReview } from "@/types";
import { Play } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

function formatDuration(seconds: number) {
  const m = Math.floor(seconds / 60);
  const s = seconds % 60;
  return `${m}:${s.toString().padStart(2, "0")}`;
}

export function VideoReviews({ productId }: { productId: string }) {
  const [videos, setVideos] = useState<VideoReview[]>([]);
  const [loading, setLoading] = useState(true);
  const [activeVideo, setActiveVideo] = useState<string | null>(null);

  useEffect(() => {
    getVideoReviews(productId)
      .then(setVideos)
      .catch(() => setVideos([]))
      .finally(() => setLoading(false));
  }, [productId]);

  if (loading) {
    return (
      <div className="space-y-3">
        <Skeleton className="h-6 w-40" />
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Skeleton className="aspect-video rounded-lg" />
          <Skeleton className="aspect-video rounded-lg" />
        </div>
      </div>
    );
  }

  if (videos.length === 0) return null;

  return (
    <div className="space-y-4">
      <h2 className="text-xl font-semibold">Видеообзоры</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {videos.map((video) => (
          <Card key={video.id} className="overflow-hidden">
            <div className="relative aspect-video bg-secondary/50">
              {activeVideo === video.id ? (
                <iframe
                  src={video.videoUrl}
                  title={video.title}
                  className="w-full h-full"
                  allowFullScreen
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                />
              ) : (
                <button
                  className="w-full h-full relative group"
                  onClick={() => setActiveVideo(video.id)}
                >
                  {video.thumbnailUrl && (
                    <img
                      src={video.thumbnailUrl}
                      alt={video.title}
                      className="w-full h-full object-cover"
                    />
                  )}
                  <div className="absolute inset-0 flex items-center justify-center bg-black/20 group-hover:bg-black/40 transition-colors">
                    <div className="h-14 w-14 rounded-full bg-white/90 flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform">
                      <Play className="h-6 w-6 text-primary ml-1" />
                    </div>
                  </div>
                  <span className="absolute bottom-2 right-2 bg-black/70 text-white text-xs px-2 py-1 rounded">
                    {formatDuration(video.durationSeconds)}
                  </span>
                </button>
              )}
            </div>
            <CardContent className="p-3">
              <p className="font-medium text-sm">{video.title}</p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}

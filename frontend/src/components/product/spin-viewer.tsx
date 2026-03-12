"use client";

import { useCallback, useEffect, useRef, useState } from "react";
import Image from "next/image";
import { RotateCw, ZoomIn, ZoomOut, Pause, Play, Hand } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface SpinViewerProps {
  images: string[];
  alt?: string;
  autoRotate?: boolean;
  autoRotateSpeed?: number; // ms per frame
}

export function SpinViewer({
  images,
  alt = "Product 360°",
  autoRotate: initialAutoRotate = false,
  autoRotateSpeed = 800,
}: SpinViewerProps) {
  const [currentFrame, setCurrentFrame] = useState(0);
  const [isDragging, setIsDragging] = useState(false);
  const [autoRotate, setAutoRotate] = useState(initialAutoRotate);
  const [zoom, setZoom] = useState(1);
  const [showHint, setShowHint] = useState(true);
  const [loadedCount, setLoadedCount] = useState(0);
  const [panOffset, setPanOffset] = useState({ x: 0, y: 0 });

  const containerRef = useRef<HTMLDivElement>(null);
  const dragStartX = useRef(0);
  const dragStartFrame = useRef(0);
  const lastX = useRef(0);
  const velocity = useRef(0);
  const animFrameRef = useRef<number>(0);
  const autoRotateRef = useRef<ReturnType<typeof setInterval>>(undefined);
  const panStart = useRef({ x: 0, y: 0 });
  const panOffsetStart = useRef({ x: 0, y: 0 });

  const totalFrames = images.length;
  const sensitivity = 0.4; // pixels per frame

  // Preload all images
  useEffect(() => {
    setLoadedCount(0);
    images.forEach((src) => {
      const img = new window.Image();
      img.src = src;
      img.onload = () => setLoadedCount((c) => c + 1);
    });
  }, [images]);

  const isLoaded = loadedCount >= totalFrames;
  const loadProgress = totalFrames > 0 ? (loadedCount / totalFrames) * 100 : 0;

  // Auto-rotate
  useEffect(() => {
    if (autoRotate && isLoaded && !isDragging) {
      autoRotateRef.current = setInterval(() => {
        setCurrentFrame((f) => (f + 1) % totalFrames);
      }, autoRotateSpeed);
    }
    return () => {
      if (autoRotateRef.current) clearInterval(autoRotateRef.current);
    };
  }, [autoRotate, isLoaded, isDragging, totalFrames, autoRotateSpeed]);

  // Momentum / inertia after drag
  const applyMomentum = useCallback(() => {
    if (Math.abs(velocity.current) < 0.3) {
      velocity.current = 0;
      return;
    }
    velocity.current *= 0.92;
    setCurrentFrame((f) => {
      const delta = Math.round(velocity.current);
      return ((f + delta) % totalFrames + totalFrames) % totalFrames;
    });
    animFrameRef.current = requestAnimationFrame(applyMomentum);
  }, [totalFrames]);

  // Mouse handlers
  const handlePointerDown = useCallback(
    (e: React.PointerEvent) => {
      if (!isLoaded) return;
      setIsDragging(true);
      setShowHint(false);
      dragStartX.current = e.clientX;
      dragStartFrame.current = currentFrame;
      lastX.current = e.clientX;
      velocity.current = 0;
      if (animFrameRef.current) cancelAnimationFrame(animFrameRef.current);

      if (zoom > 1) {
        panStart.current = { x: e.clientX, y: e.clientY };
        panOffsetStart.current = { ...panOffset };
      }

      (e.target as HTMLElement).setPointerCapture(e.pointerId);
    },
    [isLoaded, currentFrame, zoom, panOffset]
  );

  const handlePointerMove = useCallback(
    (e: React.PointerEvent) => {
      if (!isDragging) return;

      if (zoom > 1) {
        const dx = e.clientX - panStart.current.x;
        const dy = e.clientY - panOffsetStart.current.y;
        setPanOffset({
          x: panOffsetStart.current.x + dx,
          y: panOffsetStart.current.y + dy,
        });
        return;
      }

      const deltaX = e.clientX - dragStartX.current;
      velocity.current = e.clientX - lastX.current;
      lastX.current = e.clientX;

      const frameDelta = Math.round(deltaX * sensitivity);
      const newFrame =
        ((dragStartFrame.current + frameDelta) % totalFrames + totalFrames) % totalFrames;
      setCurrentFrame(newFrame);
    },
    [isDragging, totalFrames, zoom]
  );

  const handlePointerUp = useCallback(() => {
    if (!isDragging) return;
    setIsDragging(false);
    if (zoom <= 1 && Math.abs(velocity.current) > 1) {
      animFrameRef.current = requestAnimationFrame(applyMomentum);
    }
  }, [isDragging, applyMomentum, zoom]);

  // Zoom
  const handleZoomIn = () => {
    setZoom((z) => Math.min(z + 0.5, 3));
  };

  const handleZoomOut = () => {
    setZoom((z) => {
      const next = Math.max(z - 0.5, 1);
      if (next === 1) setPanOffset({ x: 0, y: 0 });
      return next;
    });
  };

  const handleWheel = useCallback(
    (e: React.WheelEvent) => {
      if (!isLoaded) return;
      e.preventDefault();
      if (e.ctrlKey || e.metaKey) {
        // Zoom with ctrl+scroll
        const delta = e.deltaY > 0 ? -0.2 : 0.2;
        setZoom((z) => {
          const next = Math.max(1, Math.min(3, z + delta));
          if (next === 1) setPanOffset({ x: 0, y: 0 });
          return next;
        });
      } else {
        // Rotate with scroll
        setShowHint(false);
        const dir = e.deltaY > 0 ? 1 : -1;
        setCurrentFrame((f) => ((f + dir) % totalFrames + totalFrames) % totalFrames);
      }
    },
    [isLoaded, totalFrames]
  );

  // Cleanup
  useEffect(() => {
    return () => {
      if (animFrameRef.current) cancelAnimationFrame(animFrameRef.current);
    };
  }, []);

  if (images.length === 0) return null;

  return (
    <div className="relative select-none">
      {/* Main viewer */}
      <div
        ref={containerRef}
        className={cn(
          "relative aspect-square rounded-xl bg-secondary/30 overflow-hidden",
          isDragging ? "cursor-grabbing" : "cursor-grab",
          !isLoaded && "cursor-wait"
        )}
        onPointerDown={handlePointerDown}
        onPointerMove={handlePointerMove}
        onPointerUp={handlePointerUp}
        onPointerCancel={handlePointerUp}
        onWheel={handleWheel}
        style={{ touchAction: "none" }}
      >
        {/* Loading overlay */}
        {!isLoaded && (
          <div className="absolute inset-0 z-20 flex flex-col items-center justify-center bg-background/80 backdrop-blur-sm">
            <div className="relative h-16 w-16 mb-3">
              <svg className="h-16 w-16 -rotate-90" viewBox="0 0 64 64">
                <circle
                  cx="32"
                  cy="32"
                  r="28"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="4"
                  className="text-secondary"
                />
                <circle
                  cx="32"
                  cy="32"
                  r="28"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="4"
                  strokeDasharray={175.9}
                  strokeDashoffset={175.9 - (175.9 * loadProgress) / 100}
                  strokeLinecap="round"
                  className="text-primary transition-all duration-200"
                />
              </svg>
              <span className="absolute inset-0 flex items-center justify-center text-xs font-medium">
                {Math.round(loadProgress)}%
              </span>
            </div>
            <p className="text-sm text-muted-foreground">Загрузка 360°...</p>
          </div>
        )}

        {/* Image */}
        <div
          className="relative w-full h-full transition-transform duration-75"
          style={{
            transform: `scale(${zoom}) translate(${panOffset.x / zoom}px, ${panOffset.y / zoom}px)`,
          }}
        >
          {images.map((src, i) => (
            <Image
              key={i}
              src={src}
              alt={`${alt} - angle ${i + 1}`}
              fill
              className={cn(
                "object-contain p-4 transition-opacity duration-0",
                i === currentFrame ? "opacity-100" : "opacity-0 pointer-events-none"
              )}
              sizes="(max-width: 768px) 100vw, 50vw"
              priority={i === 0}
              draggable={false}
            />
          ))}
        </div>

        {/* Hint overlay */}
        {showHint && isLoaded && (
          <div className="absolute inset-0 z-10 flex items-center justify-center pointer-events-none animate-pulse">
            <div className="flex flex-col items-center gap-2 bg-black/60 text-white px-5 py-3 rounded-xl backdrop-blur-sm">
              <Hand className="h-6 w-6" />
              <span className="text-sm font-medium">Перетащите для вращения</span>
            </div>
          </div>
        )}

        {/* 360 badge */}
        <div className="absolute top-3 left-3 z-10 flex items-center gap-1.5 bg-black/60 text-white px-2.5 py-1 rounded-full text-xs font-medium backdrop-blur-sm">
          <RotateCw className="h-3 w-3" />
          360°
        </div>

        {/* Frame indicator */}
        {isLoaded && (
          <div className="absolute bottom-3 left-1/2 -translate-x-1/2 z-10">
            <div className="h-1 w-24 bg-white/20 rounded-full overflow-hidden backdrop-blur-sm">
              <div
                className="h-full bg-white/80 rounded-full transition-all duration-75"
                style={{ width: `${((currentFrame + 1) / totalFrames) * 100}%` }}
              />
            </div>
          </div>
        )}
      </div>

      {/* Controls */}
      <div className="flex items-center justify-center gap-1 mt-3">
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8 rounded-full"
          onClick={() => setAutoRotate(!autoRotate)}
          title={autoRotate ? "Остановить вращение" : "Авто-вращение"}
        >
          {autoRotate ? <Pause className="h-3.5 w-3.5" /> : <Play className="h-3.5 w-3.5" />}
        </Button>
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8 rounded-full"
          onClick={handleZoomIn}
          disabled={zoom >= 3}
          title="Приблизить"
        >
          <ZoomIn className="h-3.5 w-3.5" />
        </Button>
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8 rounded-full"
          onClick={handleZoomOut}
          disabled={zoom <= 1}
          title="Отдалить"
        >
          <ZoomOut className="h-3.5 w-3.5" />
        </Button>
      </div>
    </div>
  );
}

"use client";

import { Suspense, useRef, useState } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import { OrbitControls, useGLTF, Environment, ContactShadows, Html } from "@react-three/drei";
import { RotateCw, ZoomIn, ZoomOut, RotateCcw } from "lucide-react";
import { Button } from "@/components/ui/button";
import * as THREE from "three";

interface ModelViewerProps {
  modelUrl: string;
  alt?: string;
  autoRotate?: boolean;
}

function Model({ url, autoRotate }: { url: string; autoRotate: boolean }) {
  const { scene } = useGLTF(url);
  const ref = useRef<THREE.Group>(null);

  // Auto-fit model to view
  const box = new THREE.Box3().setFromObject(scene);
  const size = box.getSize(new THREE.Vector3());
  const maxDim = Math.max(size.x, size.y, size.z);
  const scale = 2 / maxDim;
  const center = box.getCenter(new THREE.Vector3());

  useFrame((_, delta) => {
    if (autoRotate && ref.current) {
      ref.current.rotation.y += delta * 0.3;
    }
  });

  return (
    <group ref={ref}>
      <primitive
        object={scene}
        scale={scale}
        position={[-center.x * scale, -center.y * scale, -center.z * scale]}
      />
    </group>
  );
}

function LoadingIndicator() {
  return (
    <Html center>
      <div className="flex flex-col items-center gap-2">
        <div className="h-10 w-10 border-3 border-primary border-t-transparent rounded-full animate-spin" />
        <p className="text-sm text-muted-foreground whitespace-nowrap">Загрузка 3D модели...</p>
      </div>
    </Html>
  );
}

export function ModelViewer({
  modelUrl,
  alt = "3D Model",
  autoRotate: initialAutoRotate = false,
}: ModelViewerProps) {
  const [autoRotate, setAutoRotate] = useState(initialAutoRotate);
  const controlsRef = useRef<any>(null);

  const handleReset = () => {
    if (controlsRef.current) {
      controlsRef.current.reset();
    }
  };

  return (
    <div className="relative select-none">
      <div className="relative aspect-square rounded-xl bg-secondary/30 overflow-hidden">
        {/* 3D badge */}
        <div className="absolute top-3 left-3 z-10 flex items-center gap-1.5 bg-black/60 text-white px-2.5 py-1 rounded-full text-xs font-medium backdrop-blur-sm">
          <RotateCw className="h-3 w-3" />
          3D
        </div>

        <Canvas
          camera={{ position: [0, 0, 4], fov: 45 }}
          gl={{ antialias: true, alpha: true }}
          dpr={[1, 2]}
        >
          <ambientLight intensity={0.6} />
          <directionalLight position={[5, 5, 5]} intensity={1} />
          <directionalLight position={[-5, -5, -5]} intensity={0.3} />

          <Suspense fallback={<LoadingIndicator />}>
            <Model url={modelUrl} autoRotate={autoRotate} />
            <Environment preset="studio" />
            <ContactShadows
              position={[0, -1.2, 0]}
              opacity={0.4}
              scale={5}
              blur={2}
            />
          </Suspense>

          <OrbitControls
            ref={controlsRef}
            enablePan={false}
            enableZoom={true}
            minDistance={2}
            maxDistance={8}
            minPolarAngle={Math.PI / 6}
            maxPolarAngle={Math.PI / 1.5}
            autoRotate={false}
            makeDefault
          />
        </Canvas>
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
          <RotateCw className={`h-3.5 w-3.5 ${autoRotate ? "animate-spin" : ""}`} />
        </Button>
        <Button
          variant="outline"
          size="icon"
          className="h-8 w-8 rounded-full"
          onClick={handleReset}
          title="Сбросить вид"
        >
          <RotateCcw className="h-3.5 w-3.5" />
        </Button>
      </div>
    </div>
  );
}

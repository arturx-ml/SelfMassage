import { useEffect, useRef } from 'react';

interface AnimationPanelProps {
  isSession?: boolean;
  activeZone?: string;
}

export function AnimationPanel({ isSession = false, activeZone = 'neck' }: AnimationPanelProps) {
  const canvasRef = useRef<HTMLCanvasElement>(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    let animationFrame: number;
    let pulse = 0;

    const draw = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      // Draw simple body silhouette
      ctx.fillStyle = 'rgba(45, 106, 79, 0.1)';
      ctx.beginPath();
      // Head
      ctx.arc(canvas.width / 2, 40, 20, 0, Math.PI * 2);
      // Body
      ctx.rect(canvas.width / 2 - 15, 60, 30, 50);
      ctx.fill();

      // Pulsing dot at zone
      const dotY = activeZone === 'neck' ? 70 : activeZone === 'back' ? 90 : 80;
      const pulseSize = 5 + Math.sin(pulse) * 2;

      ctx.fillStyle = `rgba(45, 106, 79, ${0.4 + Math.sin(pulse) * 0.3})`;
      ctx.beginPath();
      ctx.arc(canvas.width / 2, dotY, pulseSize + 8, 0, Math.PI * 2);
      ctx.fill();

      ctx.fillStyle = '#2D6A4F';
      ctx.beginPath();
      ctx.arc(canvas.width / 2, dotY, pulseSize, 0, Math.PI * 2);
      ctx.fill();

      pulse += 0.05;
      animationFrame = requestAnimationFrame(draw);
    };

    draw();

    return () => {
      cancelAnimationFrame(animationFrame);
    };
  }, [activeZone]);

  const height = isSession ? 200 : 220;

  return (
    <div
      className="w-full rounded-[20px] overflow-hidden relative"
      style={{
        height: `${height}px`,
        background: 'linear-gradient(135deg, #EAF4EC 0%, #F8F9F4 100%)',
      }}
    >
      <canvas
        ref={canvasRef}
        width={300}
        height={height}
        className="w-full h-full"
      />
    </div>
  );
}

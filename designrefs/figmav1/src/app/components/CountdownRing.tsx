interface CountdownRingProps {
  timeRemaining: number;
  totalTime: number;
  isPreparing: boolean;
}

export function CountdownRing({ timeRemaining, totalTime, isPreparing }: CountdownRingProps) {
  const radius = 60;
  const strokeWidth = 8;
  const normalizedRadius = radius - strokeWidth / 2;
  const circumference = normalizedRadius * 2 * Math.PI;
  const progress = timeRemaining / totalTime;
  const strokeDashoffset = circumference - progress * circumference;

  const color = isPreparing ? '#1B4332' : '#2D6A4F';

  return (
    <div className="relative inline-flex items-center justify-center">
      {/* Outer glow ring */}
      <div
        className="absolute rounded-full"
        style={{
          width: `${radius * 2 + 32}px`,
          height: `${radius * 2 + 32}px`,
          background: `radial-gradient(circle, ${color}08 0%, transparent 70%)`,
        }}
      />

      {/* SVG countdown ring */}
      <svg height={radius * 2} width={radius * 2} className="relative">
        {/* Background track */}
        <circle
          stroke="#E5E7EB"
          fill="transparent"
          strokeWidth={strokeWidth}
          r={normalizedRadius}
          cx={radius}
          cy={radius}
        />
        {/* Progress arc */}
        <circle
          stroke={color}
          fill="transparent"
          strokeWidth={strokeWidth}
          strokeDasharray={circumference + ' ' + circumference}
          style={{ strokeDashoffset, transform: 'rotate(-90deg)', transformOrigin: '50% 50%' }}
          strokeLinecap="round"
          r={normalizedRadius}
          cx={radius}
          cy={radius}
        />
      </svg>

      {/* Time display */}
      <div className="absolute inset-0 flex items-center justify-center">
        <span
          className="text-[#1F2937]"
          style={{
            fontSize: '48px',
            fontFamily: 'Inter, sans-serif',
            fontWeight: 600,
          }}
        >
          {timeRemaining}
        </span>
      </div>
    </div>
  );
}

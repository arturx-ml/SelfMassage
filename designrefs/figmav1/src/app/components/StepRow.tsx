import { TechniqueStep } from '../data/mockData';

interface StepRowProps {
  step: TechniqueStep;
  isLast: boolean;
}

function formatDuration(seconds: number): string {
  return `${seconds}s`;
}

export function StepRow({ step, isLast }: StepRowProps) {
  return (
    <div className="relative">
      <div className="flex gap-4 py-4">
        <div className="w-8 h-8 rounded-full bg-[#2D6A4F] flex items-center justify-center flex-shrink-0">
          <span className="text-white font-semibold text-sm" style={{ fontFamily: 'Inter, sans-serif' }}>
            {step.number}
          </span>
        </div>
        <div className="flex-1">
          <p className="text-[#1F2937] mb-2">{step.instruction}</p>
          <span className="inline-block px-3 py-1 bg-[#D4A373] bg-opacity-15 text-[#D4A373] text-xs font-medium rounded-full">
            {formatDuration(step.duration)}
          </span>
        </div>
      </div>
      {!isLast && (
        <div className="absolute left-4 top-12 bottom-0 w-px border-l-2 border-dashed border-[#E5E7EB]" />
      )}
    </div>
  );
}

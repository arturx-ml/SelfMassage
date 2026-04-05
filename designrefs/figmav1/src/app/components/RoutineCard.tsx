import { ArrowRight, Leaf } from 'lucide-react';
import { Routine } from '../data/mockData';

interface RoutineCardProps {
  routine: Routine;
  onClick: () => void;
}

function formatDuration(seconds: number): string {
  const mins = Math.floor(seconds / 60);
  return `${mins} min`;
}

export function RoutineCard({ routine, onClick }: RoutineCardProps) {
  return (
    <button
      onClick={onClick}
      className="w-full min-h-[108px] bg-white rounded-[20px] shadow-[0_4px_12px_rgba(0,0,0,0.10)] p-5 hover:shadow-[0_6px_16px_rgba(0,0,0,0.12)] transition-shadow relative overflow-hidden text-left"
    >
      <Leaf className="absolute right-4 bottom-4 w-20 h-20 text-[#2D6A4F] opacity-[0.08]" />
      <h3 className="font-semibold text-[#1F2937] mb-2">{routine.name}</h3>
      <p className="text-sm text-[#6B7280] mb-4 leading-relaxed pr-12">{routine.description}</p>
      <div className="flex items-center justify-between">
        <div className="flex gap-2">
          <span className="px-3 py-1 bg-[#D4A373] bg-opacity-15 text-[#D4A373] text-xs font-medium rounded-full">
            {formatDuration(routine.duration)}
          </span>
          <span className="px-3 py-1 bg-[#D4A373] bg-opacity-15 text-[#D4A373] text-xs font-medium rounded-full">
            {routine.techniqueIds.length} techniques
          </span>
        </div>
        <div className="w-8 h-8 rounded-full bg-[#2D6A4F] flex items-center justify-center">
          <ArrowRight className="w-4 h-4 text-white" />
        </div>
      </div>
    </button>
  );
}

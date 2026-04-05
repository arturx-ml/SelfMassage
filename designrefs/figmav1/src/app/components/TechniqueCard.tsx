import { Activity } from 'lucide-react';
import { Technique } from '../data/mockData';

interface TechniqueCardProps {
  technique: Technique;
  onClick: () => void;
}

function formatDuration(seconds: number): string {
  const mins = Math.floor(seconds / 60);
  return `${mins} min`;
}

export function TechniqueCard({ technique, onClick }: TechniqueCardProps) {
  return (
    <button
      onClick={onClick}
      className="w-full h-[72px] bg-white rounded-[20px] shadow-[0_4px_12px_rgba(0,0,0,0.10)] px-4 py-3 flex items-center gap-4 hover:shadow-[0_6px_16px_rgba(0,0,0,0.12)] transition-shadow"
    >
      <div className="w-10 h-10 rounded-full bg-[#52B788] bg-opacity-20 flex items-center justify-center flex-shrink-0">
        <Activity className="w-5 h-5 text-[#2D6A4F]" />
      </div>
      <div className="flex-1 text-left min-w-0">
        <h4 className="font-semibold text-[#1F2937] truncate">{technique.name}</h4>
        <p className="text-sm text-[#6B7280] truncate">{technique.summary}</p>
      </div>
      <span className="px-3 py-1 bg-[#D4A373] bg-opacity-15 text-[#D4A373] text-xs font-medium rounded-full flex-shrink-0">
        {formatDuration(technique.duration)}
      </span>
    </button>
  );
}

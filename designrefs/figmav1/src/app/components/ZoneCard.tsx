import { ChevronRight, Leaf, Activity, Hand, Footprints, User } from 'lucide-react';
import { Zone } from '../data/mockData';

interface ZoneCardProps {
  zone: Zone;
  onClick: () => void;
}

const iconMap: Record<string, any> = {
  neck: Activity,
  back: Activity,
  hands: Hand,
  legs: Footprints,
  head: User,
};

export function ZoneCard({ zone, onClick }: ZoneCardProps) {
  const Icon = iconMap[zone.icon] || Leaf;

  return (
    <button
      onClick={onClick}
      className="w-full h-[88px] bg-white rounded-[20px] shadow-[0_4px_12px_rgba(0,0,0,0.10)] p-4 flex items-center gap-4 hover:shadow-[0_6px_16px_rgba(0,0,0,0.12)] transition-shadow relative overflow-hidden"
    >
      <div className="absolute left-0 top-0 bottom-0 w-1 bg-[#2D6A4F]" />
      <div className="w-[52px] h-[52px] rounded-[14px] bg-[#52B788] bg-opacity-20 flex items-center justify-center flex-shrink-0">
        <Icon className="w-6 h-6 text-[#2D6A4F]" />
      </div>
      <div className="flex-1 text-left">
        <h3 className="font-semibold text-[#1F2937]">{zone.name}</h3>
        <p className="text-sm text-[#6B7280]">{zone.count} techniques</p>
      </div>
      <ChevronRight className="w-5 h-5 text-[#6B7280]" />
    </button>
  );
}

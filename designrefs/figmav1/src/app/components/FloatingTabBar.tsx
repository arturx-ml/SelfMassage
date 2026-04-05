import { Leaf, PlayCircle } from 'lucide-react';

interface FloatingTabBarProps {
  activeTab: 'zones' | 'routines';
  onTabChange: (tab: 'zones' | 'routines') => void;
}

export function FloatingTabBar({ activeTab, onTabChange }: FloatingTabBarProps) {
  return (
    <div className="fixed bottom-4 left-4 right-4 z-50">
      <div className="mx-auto max-w-[390px] bg-white rounded-[20px] shadow-[0_4px_12px_rgba(0,0,0,0.10)] px-4 py-3 flex items-center justify-around">
        <button
          onClick={() => onTabChange('zones')}
          className={`flex flex-col items-center gap-1 px-6 py-2 rounded-full transition-colors ${
            activeTab === 'zones' ? 'text-[#2D6A4F]' : 'text-[#6B7280]'
          }`}
        >
          <Leaf className="w-6 h-6" fill={activeTab === 'zones' ? 'currentColor' : 'none'} />
          <span className="text-xs font-medium">Zones</span>
        </button>
        <button
          onClick={() => onTabChange('routines')}
          className={`flex flex-col items-center gap-1 px-6 py-2 rounded-full transition-colors ${
            activeTab === 'routines' ? 'text-[#2D6A4F]' : 'text-[#6B7280]'
          }`}
        >
          <PlayCircle className="w-6 h-6" fill={activeTab === 'routines' ? 'currentColor' : 'none'} />
          <span className="text-xs font-medium">Routines</span>
        </button>
      </div>
    </div>
  );
}

import { ZoneCard } from '../components/ZoneCard';
import { zones } from '../data/mockData';

interface ZoneListScreenProps {
  onZoneClick: (zoneId: string) => void;
}

export function ZoneListScreen({ onZoneClick }: ZoneListScreenProps) {
  const currentHour = new Date().getHours();
  const greeting =
    currentHour < 12 ? 'Good morning' : currentHour < 18 ? 'Good afternoon' : 'Good evening';

  return (
    <div className="min-h-screen pb-24">
      <div
        className="px-4 pt-12 pb-8"
        style={{
          background: 'linear-gradient(180deg, #EAF4EC 0%, #FFFFFF 100%)',
        }}
      >
        <h1 className="text-2xl font-semibold text-[#1F2937] mb-1">{greeting}</h1>
        <p className="text-[#6B7280]">Choose a zone to begin your session</p>
      </div>

      <div className="px-4 space-y-3">
        {zones.map((zone) => (
          <ZoneCard key={zone.id} zone={zone} onClick={() => onZoneClick(zone.id)} />
        ))}
      </div>
    </div>
  );
}

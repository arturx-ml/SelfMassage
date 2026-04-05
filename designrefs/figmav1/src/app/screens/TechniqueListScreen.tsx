import { ArrowLeft } from 'lucide-react';
import { TechniqueCard } from '../components/TechniqueCard';
import { techniques, zones } from '../data/mockData';

interface TechniqueListScreenProps {
  zoneId: string;
  onBack: () => void;
  onTechniqueClick: (techniqueId: string) => void;
}

export function TechniqueListScreen({ zoneId, onBack, onTechniqueClick }: TechniqueListScreenProps) {
  const zone = zones.find((z) => z.id === zoneId);
  const zoneTechniques = techniques.filter((t) => t.zoneId === zoneId);

  if (!zone) return null;

  return (
    <div className="min-h-screen pb-24">
      <div
        className="px-4 pt-12 pb-8"
        style={{
          background: 'linear-gradient(180deg, #EAF4EC 0%, #FFFFFF 100%)',
        }}
      >
        <button
          onClick={onBack}
          className="flex items-center gap-2 text-[#2D6A4F] mb-4 -ml-2 px-2 py-1 hover:bg-[#2D6A4F] hover:bg-opacity-10 rounded-lg transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
          <span className="font-medium">Back</span>
        </button>
        <h1 className="text-2xl font-semibold text-[#1F2937] mb-1">{zone.name}</h1>
        <p className="text-[#6B7280]">{zoneTechniques.length} techniques available</p>
      </div>

      <div className="px-4 space-y-3">
        {zoneTechniques.map((technique) => (
          <TechniqueCard
            key={technique.id}
            technique={technique}
            onClick={() => onTechniqueClick(technique.id)}
          />
        ))}
      </div>
    </div>
  );
}

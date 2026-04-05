import { ArrowLeft } from 'lucide-react';
import { routines, techniques } from '../data/mockData';

interface RoutineDetailScreenProps {
  routineId: string;
  onBack: () => void;
  onStartRoutine: () => void;
}

function formatDuration(seconds: number): string {
  const mins = Math.floor(seconds / 60);
  return `${mins} min`;
}

export function RoutineDetailScreen({ routineId, onBack, onStartRoutine }: RoutineDetailScreenProps) {
  const routine = routines.find((r) => r.id === routineId);

  if (!routine) return null;

  const routineTechniques = routine.techniqueIds
    .map((id) => techniques.find((t) => t.id === id))
    .filter(Boolean);

  return (
    <div className="min-h-screen pb-24">
      <div
        className="px-4 pt-8 pb-8"
        style={{
          background: 'linear-gradient(135deg, rgba(45, 106, 79, 0.08) 0%, rgba(45, 106, 79, 0.02) 100%)',
        }}
      >
        <button
          onClick={onBack}
          className="flex items-center gap-2 text-[#2D6A4F] mb-6 -ml-2 px-2 py-1 hover:bg-[#2D6A4F] hover:bg-opacity-10 rounded-lg transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
          <span className="font-medium">Back</span>
        </button>
        <h1 className="text-2xl font-semibold text-white mb-4" style={{ color: '#1F2937' }}>
          {routine.name}
        </h1>
        <div className="flex gap-2">
          <span className="px-3 py-1 bg-[#D4A373] bg-opacity-15 text-[#D4A373] text-xs font-medium rounded-full">
            {formatDuration(routine.duration)}
          </span>
          <span className="px-3 py-1 bg-[#D4A373] bg-opacity-15 text-[#D4A373] text-xs font-medium rounded-full">
            {routine.techniqueIds.length} techniques
          </span>
        </div>
      </div>

      <div className="px-4 py-6">
        <p className="text-[#6B7280] mb-6 leading-relaxed">{routine.description}</p>

        <h3 className="font-semibold text-[#1F2937] mb-4">Techniques</h3>

        <div className="space-y-3 mb-8">
          {routineTechniques.map((technique) => (
            <div key={technique?.id} className="flex items-start gap-3">
              <div className="w-2 h-2 rounded-full bg-[#2D6A4F] mt-2 flex-shrink-0" />
              <div className="flex-1">
                <h4 className="font-medium text-[#1F2937]">{technique?.name}</h4>
                <p className="text-sm text-[#6B7280]">{technique?.summary}</p>
              </div>
            </div>
          ))}
        </div>

        <button
          onClick={onStartRoutine}
          className="w-full h-14 bg-[#2D6A4F] hover:bg-[#1B4332] text-white font-semibold rounded-[14px] transition-colors"
        >
          Start Routine
        </button>
      </div>
    </div>
  );
}

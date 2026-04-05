import { RoutineCard } from '../components/RoutineCard';
import { routines } from '../data/mockData';

interface RoutineListScreenProps {
  onRoutineClick: (routineId: string) => void;
}

export function RoutineListScreen({ onRoutineClick }: RoutineListScreenProps) {
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
        <p className="text-[#6B7280]">Start a guided routine</p>
      </div>

      <div className="px-4 space-y-3">
        {routines.map((routine) => (
          <RoutineCard key={routine.id} routine={routine} onClick={() => onRoutineClick(routine.id)} />
        ))}
      </div>
    </div>
  );
}

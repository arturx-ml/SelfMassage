import { ArrowLeft, PlayCircle } from 'lucide-react';
import { AnimationPanel } from '../components/AnimationPanel';
import { StepRow } from '../components/StepRow';
import { techniques } from '../data/mockData';

interface TechniqueDetailScreenProps {
  techniqueId: string;
  onBack: () => void;
  onStartSession: () => void;
}

function formatDuration(seconds: number): string {
  const mins = Math.floor(seconds / 60);
  return `${mins} min`;
}

export function TechniqueDetailScreen({ techniqueId, onBack, onStartSession }: TechniqueDetailScreenProps) {
  const technique = techniques.find((t) => t.id === techniqueId);

  if (!technique) return null;

  return (
    <div className="min-h-screen pb-24">
      <div className="px-4 pt-8 pb-6 bg-white">
        <div className="flex items-center justify-between mb-6">
          <button
            onClick={onBack}
            className="flex items-center gap-2 text-[#2D6A4F] -ml-2 px-2 py-1 hover:bg-[#2D6A4F] hover:bg-opacity-10 rounded-lg transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <h2 className="text-lg font-semibold text-[#1F2937] flex-1 text-center -ml-12">
            {technique.name}
          </h2>
          <button
            onClick={onStartSession}
            className="w-9 h-9 rounded-full bg-[#2D6A4F] hover:bg-[#1B4332] transition-colors flex items-center justify-center"
          >
            <PlayCircle className="w-5 h-5 text-white" fill="white" />
          </button>
        </div>

        <AnimationPanel />
      </div>

      <div className="px-4 py-6">
        <p className="text-[#6B7280] mb-6 leading-relaxed">{technique.description}</p>

        <div className="flex items-center gap-3 mb-4">
          <h3 className="font-semibold text-[#1F2937]">Steps</h3>
          <span className="px-3 py-1 bg-[#2D6A4F] bg-opacity-10 text-[#2D6A4F] text-xs font-medium rounded-full">
            {technique.steps.length}
          </span>
        </div>

        <div className="space-y-0">
          {technique.steps.map((step, index) => (
            <StepRow key={step.id} step={step} isLast={index === technique.steps.length - 1} />
          ))}
        </div>

        <button
          onClick={onStartSession}
          className="w-full h-14 bg-[#2D6A4F] hover:bg-[#1B4332] text-white font-semibold rounded-[14px] transition-colors mt-8"
        >
          Start Session
        </button>
      </div>
    </div>
  );
}

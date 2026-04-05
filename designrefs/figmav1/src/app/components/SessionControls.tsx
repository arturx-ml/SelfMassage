import { Play, Pause, SkipBack, SkipForward } from 'lucide-react';

interface SessionControlsProps {
  isPlaying: boolean;
  onPlayPause: () => void;
  onPrevious: () => void;
  onNext: () => void;
  canGoPrevious: boolean;
  canGoNext: boolean;
}

export function SessionControls({
  isPlaying,
  onPlayPause,
  onPrevious,
  onNext,
  canGoPrevious,
  canGoNext,
}: SessionControlsProps) {
  return (
    <div className="flex items-center justify-center gap-6">
      <button
        onClick={onPrevious}
        disabled={!canGoPrevious}
        className={`w-11 h-11 rounded-full flex items-center justify-center transition-colors ${
          canGoPrevious
            ? 'text-[#2D6A4F] hover:bg-[#2D6A4F] hover:bg-opacity-10'
            : 'text-[#E5E7EB]'
        }`}
      >
        <SkipBack className="w-6 h-6" />
      </button>

      <button
        onClick={onPlayPause}
        className="w-16 h-16 rounded-full bg-[#2D6A4F] hover:bg-[#1B4332] transition-colors flex items-center justify-center shadow-lg"
      >
        {isPlaying ? (
          <Pause className="w-7 h-7 text-white" fill="white" />
        ) : (
          <Play className="w-7 h-7 text-white ml-1" fill="white" />
        )}
      </button>

      <button
        onClick={onNext}
        disabled={!canGoNext}
        className={`w-11 h-11 rounded-full flex items-center justify-center transition-colors ${
          canGoNext
            ? 'text-[#2D6A4F] hover:bg-[#2D6A4F] hover:bg-opacity-10'
            : 'text-[#E5E7EB]'
        }`}
      >
        <SkipForward className="w-6 h-6" />
      </button>
    </div>
  );
}

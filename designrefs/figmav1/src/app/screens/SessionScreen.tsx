import { useState, useEffect } from 'react';
import { ArrowLeft } from 'lucide-react';
import { AnimationPanel } from '../components/AnimationPanel';
import { CountdownRing } from '../components/CountdownRing';
import { SessionControls } from '../components/SessionControls';
import { SessionCompleteOverlay } from '../components/SessionCompleteOverlay';
import { techniques } from '../data/mockData';

interface SessionScreenProps {
  techniqueId: string;
  onExit: () => void;
}

const PREP_TIME = 5;

export function SessionScreen({ techniqueId, onExit }: SessionScreenProps) {
  const technique = techniques.find((t) => t.id === techniqueId);
  const [currentStepIndex, setCurrentStepIndex] = useState(0);
  const [timeRemaining, setTimeRemaining] = useState(PREP_TIME);
  const [isPlaying, setIsPlaying] = useState(true);
  const [isPreparing, setIsPreparing] = useState(true);
  const [isComplete, setIsComplete] = useState(false);

  const currentStep = technique?.steps[currentStepIndex];

  useEffect(() => {
    if (!isPlaying || !currentStep) return;

    const timer = setInterval(() => {
      setTimeRemaining((prev) => {
        if (prev <= 1) {
          if (isPreparing) {
            // Prep done, start actual step
            setIsPreparing(false);
            return currentStep.duration;
          } else {
            // Step done, move to next or complete
            if (currentStepIndex < (technique?.steps.length || 0) - 1) {
              setCurrentStepIndex((i) => i + 1);
              setIsPreparing(true);
              return PREP_TIME;
            } else {
              setIsComplete(true);
              setIsPlaying(false);
              return 0;
            }
          }
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [isPlaying, isPreparing, currentStepIndex, currentStep, technique]);

  if (!technique || !currentStep) return null;

  const handlePlayPause = () => {
    setIsPlaying(!isPlaying);
  };

  const handlePrevious = () => {
    if (currentStepIndex > 0) {
      setCurrentStepIndex((i) => i - 1);
      setIsPreparing(true);
      setTimeRemaining(PREP_TIME);
    }
  };

  const handleNext = () => {
    if (currentStepIndex < technique.steps.length - 1) {
      setCurrentStepIndex((i) => i + 1);
      setIsPreparing(true);
      setTimeRemaining(PREP_TIME);
    }
  };

  return (
    <div className="min-h-screen bg-white">
      {/* Top bar */}
      <div className="px-4 pt-8 pb-4 flex items-center justify-between">
        <button
          onClick={onExit}
          className="flex items-center gap-2 text-[#2D6A4F] -ml-2 px-2 py-1 hover:bg-[#2D6A4F] hover:bg-opacity-10 rounded-lg transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>
        <h2 className="text-base font-semibold text-[#1F2937] flex-1 text-center -ml-12">
          {technique.name}
        </h2>
        <div className="w-10" />
      </div>

      {/* Animation panel */}
      <div className="px-4 mb-6">
        <AnimationPanel isSession={true} />
      </div>

      {/* Step indicator */}
      <div className="px-6 mb-4 flex items-center justify-center gap-2 text-sm">
        <span className="text-[#6B7280]">
          Step {currentStepIndex + 1} of {technique.steps.length}
        </span>
        <span className="text-[#6B7280]">•</span>
        <span className="text-[#2D6A4F] font-medium">{technique.name}</span>
      </div>

      {/* Get Ready badge */}
      {isPreparing && (
        <div className="flex justify-center mb-6">
          <div className="px-4 py-2 bg-[#1B4332] rounded-full animate-pulse">
            <span className="text-white text-xs font-semibold tracking-wider uppercase">Get Ready</span>
          </div>
        </div>
      )}

      {/* Instruction */}
      <div className="px-6 mb-8">
        <p className="text-center text-lg text-[#1F2937] leading-relaxed">
          {currentStep.instruction}
        </p>
      </div>

      {/* Countdown ring */}
      <div className="flex justify-center mb-12">
        <CountdownRing
          timeRemaining={timeRemaining}
          totalTime={isPreparing ? PREP_TIME : currentStep.duration}
          isPreparing={isPreparing}
        />
      </div>

      {/* Controls */}
      <div className="px-6">
        <SessionControls
          isPlaying={isPlaying}
          onPlayPause={handlePlayPause}
          onPrevious={handlePrevious}
          onNext={handleNext}
          canGoPrevious={currentStepIndex > 0}
          canGoNext={currentStepIndex < technique.steps.length - 1}
        />
      </div>

      {/* Complete overlay */}
      {isComplete && <SessionCompleteOverlay onDone={onExit} />}
    </div>
  );
}

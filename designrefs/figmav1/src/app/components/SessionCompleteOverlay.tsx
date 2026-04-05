import { Check } from 'lucide-react';

interface SessionCompleteOverlayProps {
  onDone: () => void;
}

export function SessionCompleteOverlay({ onDone }: SessionCompleteOverlayProps) {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50 px-6">
      <div className="bg-white rounded-[28px] p-8 max-w-sm w-full text-center shadow-2xl">
        <div className="w-20 h-20 rounded-full bg-[#2D6A4F] flex items-center justify-center mx-auto mb-6">
          <Check className="w-10 h-10 text-white" strokeWidth={3} />
        </div>
        <h2 className="text-2xl font-semibold text-[#1F2937] mb-3">Well done!</h2>
        <p className="text-[#6B7280] mb-8 leading-relaxed">
          You've completed your session. Keep up the great work on your wellness journey.
        </p>
        <button
          onClick={onDone}
          className="w-full h-14 bg-[#2D6A4F] hover:bg-[#1B4332] text-white font-semibold rounded-[14px] transition-colors"
        >
          Done
        </button>
      </div>
    </div>
  );
}

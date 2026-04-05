import { useState } from 'react';
import { FloatingTabBar } from './components/FloatingTabBar';
import { ZoneListScreen } from './screens/ZoneListScreen';
import { RoutineListScreen } from './screens/RoutineListScreen';
import { TechniqueListScreen } from './screens/TechniqueListScreen';
import { TechniqueDetailScreen } from './screens/TechniqueDetailScreen';
import { RoutineDetailScreen } from './screens/RoutineDetailScreen';
import { SessionScreen } from './screens/SessionScreen';

type Screen =
  | { type: 'zone-list' }
  | { type: 'routine-list' }
  | { type: 'technique-list'; zoneId: string }
  | { type: 'technique-detail'; techniqueId: string }
  | { type: 'routine-detail'; routineId: string }
  | { type: 'session'; techniqueId: string };

export default function App() {
  const [activeTab, setActiveTab] = useState<'zones' | 'routines'>('zones');
  const [screen, setScreen] = useState<Screen>({ type: 'zone-list' });

  const handleTabChange = (tab: 'zones' | 'routines') => {
    setActiveTab(tab);
    setScreen(tab === 'zones' ? { type: 'zone-list' } : { type: 'routine-list' });
  };

  const showTabBar = screen.type !== 'session';

  return (
    <div className="min-h-screen max-w-[390px] mx-auto bg-[#F8F9F4] relative">
      {/* Screens */}
      {screen.type === 'zone-list' && (
        <ZoneListScreen
          onZoneClick={(zoneId) => setScreen({ type: 'technique-list', zoneId })}
        />
      )}

      {screen.type === 'routine-list' && (
        <RoutineListScreen
          onRoutineClick={(routineId) => setScreen({ type: 'routine-detail', routineId })}
        />
      )}

      {screen.type === 'technique-list' && (
        <TechniqueListScreen
          zoneId={screen.zoneId}
          onBack={() => setScreen({ type: 'zone-list' })}
          onTechniqueClick={(techniqueId) => setScreen({ type: 'technique-detail', techniqueId })}
        />
      )}

      {screen.type === 'technique-detail' && (
        <TechniqueDetailScreen
          techniqueId={screen.techniqueId}
          onBack={() => setScreen({ type: 'technique-list', zoneId: '1' })}
          onStartSession={() => setScreen({ type: 'session', techniqueId: screen.techniqueId })}
        />
      )}

      {screen.type === 'routine-detail' && (
        <RoutineDetailScreen
          routineId={screen.routineId}
          onBack={() => setScreen({ type: 'routine-list' })}
          onStartRoutine={() => {
            // For demo, start first technique from the routine
            setScreen({ type: 'session', techniqueId: 't1' });
          }}
        />
      )}

      {screen.type === 'session' && (
        <SessionScreen
          techniqueId={screen.techniqueId}
          onExit={() => setScreen({ type: activeTab === 'zones' ? 'zone-list' : 'routine-list' })}
        />
      )}

      {/* Bottom navigation */}
      {showTabBar && <FloatingTabBar activeTab={activeTab} onTabChange={handleTabChange} />}
    </div>
  );
}
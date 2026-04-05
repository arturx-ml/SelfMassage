export interface Zone {
  id: string;
  name: string;
  count: number;
  icon: string;
}

export interface Technique {
  id: string;
  name: string;
  summary: string;
  duration: number;
  zoneId: string;
  description?: string;
  steps: TechniqueStep[];
}

export interface TechniqueStep {
  id: string;
  number: number;
  instruction: string;
  duration: number;
}

export interface Routine {
  id: string;
  name: string;
  description: string;
  duration: number;
  techniqueIds: string[];
}

export const zones: Zone[] = [
  { id: '1', name: 'Neck & Shoulders', count: 8, icon: 'neck' },
  { id: '2', name: 'Lower Back', count: 6, icon: 'back' },
  { id: '3', name: 'Hands & Wrists', count: 5, icon: 'hands' },
  { id: '4', name: 'Legs & Feet', count: 7, icon: 'legs' },
  { id: '5', name: 'Head & Face', count: 4, icon: 'head' },
];

export const techniques: Technique[] = [
  {
    id: 't1',
    name: 'Neck Release',
    summary: 'Gentle pressure points to relieve tension',
    duration: 180,
    zoneId: '1',
    description: 'A soothing technique that targets key pressure points along the neck to release built-up tension and improve circulation.',
    steps: [
      { id: 's1', number: 1, instruction: 'Place fingertips at the base of your skull, apply gentle pressure', duration: 45 },
      { id: 's2', number: 2, instruction: 'Slowly move fingers down the sides of your neck in circular motions', duration: 60 },
      { id: 's3', number: 3, instruction: 'Gently tilt head side to side, maintaining light pressure', duration: 45 },
      { id: 's4', number: 4, instruction: 'Finish with long strokes from base of skull to shoulders', duration: 30 },
    ],
  },
  {
    id: 't2',
    name: 'Shoulder Knead',
    summary: 'Deep tissue massage for shoulder relief',
    duration: 240,
    zoneId: '1',
    description: 'Target deep muscle knots in your shoulders with this effective kneading technique.',
    steps: [
      { id: 's5', number: 1, instruction: 'Cross arm over chest, grip opposite shoulder with hand', duration: 60 },
      { id: 's6', number: 2, instruction: 'Use firm circular motions to knead the shoulder muscle', duration: 90 },
      { id: 's7', number: 3, instruction: 'Focus on any tender areas with sustained pressure', duration: 60 },
      { id: 's8', number: 4, instruction: 'Switch to other shoulder and repeat', duration: 30 },
    ],
  },
  {
    id: 't3',
    name: 'Trapezius Press',
    summary: 'Targeted relief for upper back tension',
    duration: 150,
    zoneId: '1',
    description: 'Release tension in the trapezius muscle that runs along your upper back and neck.',
    steps: [
      { id: 's9', number: 1, instruction: 'Reach across and pinch trapezius muscle between fingers', duration: 40 },
      { id: 's10', number: 2, instruction: 'Apply firm pressure while rolling the muscle', duration: 70 },
      { id: 's11', number: 3, instruction: 'Work from neck toward shoulder tip', duration: 40 },
    ],
  },
  {
    id: 't4',
    name: 'Lower Spine Alignment',
    summary: 'Gentle stretches to ease lower back pain',
    duration: 300,
    zoneId: '2',
    description: 'A combination of massage and gentle movement to relieve lower back discomfort.',
    steps: [
      { id: 's12', number: 1, instruction: 'Place palms on lower back, fingers pointing down', duration: 60 },
      { id: 's13', number: 2, instruction: 'Apply gentle pressure and slide hands outward', duration: 90 },
      { id: 's14', number: 3, instruction: 'Use knuckles to trace along spine with circular motions', duration: 90 },
      { id: 's15', number: 4, instruction: 'Gently arch back while maintaining pressure', duration: 60 },
    ],
  },
  {
    id: 't5',
    name: 'Hand Relaxation',
    summary: 'Soothe tired hands and improve flexibility',
    duration: 200,
    zoneId: '3',
    description: 'Perfect for after long hours at the keyboard or repetitive hand work.',
    steps: [
      { id: 's16', number: 1, instruction: 'Massage palm with thumb in circular motions', duration: 50 },
      { id: 's17', number: 2, instruction: 'Gently pull and rotate each finger', duration: 70 },
      { id: 's18', number: 3, instruction: 'Press along the spaces between knuckles', duration: 50 },
      { id: 's19', number: 4, instruction: 'Finish with gentle wrist rotations', duration: 30 },
    ],
  },
];

export const routines: Routine[] = [
  {
    id: 'r1',
    name: 'Morning Energizer',
    description: 'Wake up your body with this refreshing 10-minute routine targeting key energy points.',
    duration: 570,
    techniqueIds: ['t1', 't3', 't5'],
  },
  {
    id: 'r2',
    name: 'Desk Worker Relief',
    description: 'Combat sitting fatigue with focused techniques for neck, shoulders, and lower back.',
    duration: 690,
    techniqueIds: ['t1', 't2', 't4'],
  },
  {
    id: 'r3',
    name: 'Evening Wind Down',
    description: 'Relax and release the tension of the day with gentle, calming massage techniques.',
    duration: 420,
    techniqueIds: ['t1', 't4'],
  },
];

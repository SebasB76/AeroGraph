// types.ts - Tipos compartidos para todo el proyecto

export interface Tab {
  id: number;
  title: string;
  active: boolean;
}

export interface Message {
  id: number;
  role: 'user' | 'assistant';
  content: string;
  showRouteButton?: boolean;
  isRoute?: boolean;
}

export interface HistoryItem {
  id: number;
  title: string;
  date: string;
  status: 'completed' | 'in-progress';
}

export interface Connections {
  whatsapp: boolean;
  calendar: boolean;
  gmail: boolean;
}

export interface MessagesState {
  [tabId: number]: Message[];
}
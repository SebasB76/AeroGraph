import React from 'react';
import { Settings, MessageCircle, Calendar, Mail, Check, Clock } from 'lucide-react';
import type { Connections, HistoryItem } from './types';

interface SidebarProps {
  connections: Connections;
  onToggleConnection: (service: keyof Connections) => void;
  history: HistoryItem[];
}

const Sidebar: React.FC<SidebarProps> = ({ connections, onToggleConnection, history }) => {
  return (
    <div className="w-[230px] bg-white border-r border-gray-200 flex flex-col">
      {/* Header y Config en amarillo */}
      <div className="bg-yellow-400 px-6 py-6">
        <h1 className="text-xl font-bold text-gray-900 mb-4">Govly</h1>
        
        <button className="w-full flex items-center gap-2 px-3 py-2 text-gray-900 hover:bg-yellow-500 rounded-lg transition-colors text-sm">
          <Settings className="w-4 h-4" />
          <span className="font-medium">Configuración</span>
        </button>
      </div>

      {/* Conexiones */}
      <div className="px-3 py-4 border-b border-gray-200">
        <p className="text-xs font-semibold text-gray-500 mb-3 uppercase tracking-wide px-2">
          Conectar servicios
        </p>
        
        <div className="space-y-1">
          <button
            onClick={() => onToggleConnection('whatsapp')}
            className={`w-full flex items-center gap-2 px-3 py-2 rounded-md text-sm transition-colors ${
              connections.whatsapp
                ? 'bg-green-500 text-white'
                : 'text-gray-700 hover:bg-gray-50'
            }`}
          >
            <MessageCircle className="w-4 h-4" />
            <span>WhatsApp</span>
          </button>

          <button
            onClick={() => onToggleConnection('calendar')}
            className={`w-full flex items-center gap-2 px-3 py-2 rounded-md text-sm transition-colors ${
              connections.calendar
                ? 'bg-blue-500 text-white'
                : 'text-gray-700 hover:bg-gray-50'
            }`}
          >
            <Calendar className="w-4 h-4" />
            <span>Google Calendar</span>
          </button>

          <button
            onClick={() => onToggleConnection('gmail')}
            className={`w-full flex items-center gap-2 px-3 py-2 rounded-md text-sm transition-colors ${
              connections.gmail
                ? 'bg-red-500 text-white'
                : 'text-gray-700 hover:bg-gray-50'
            }`}
          >
            <Mail className="w-4 h-4" />
            <span>Gmail</span>
          </button>
        </div>
      </div>

      {/* Historial */}
      <div className="flex-1 overflow-y-auto px-3 py-4">
        <p className="text-xs font-semibold text-gray-500 mb-3 uppercase tracking-wide px-2">
          Historial
        </p>
        
        <div className="space-y-1">
          {history.map(item => (
            <div
              key={item.id}
              className="px-3 py-2 hover:bg-gray-50 rounded-md cursor-pointer transition-colors"
            >
              <div className="flex items-start justify-between mb-1">
                <h4 className="text-sm font-medium text-gray-900 leading-tight">
                  {item.title}
                </h4>
                {item.status === 'completed' ? (
                  <Check className="w-3.5 h-3.5 text-green-600 flex-shrink-0 mt-0.5" />
                ) : (
                  <Clock className="w-3.5 h-3.5 text-yellow-600 flex-shrink-0 mt-0.5" />
                )}
              </div>
              <p className="text-xs text-gray-500">{item.date}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Sidebar;

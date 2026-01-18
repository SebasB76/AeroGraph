import React from 'react';
import { Map, Mic, Send } from 'lucide-react';

interface WelcomeScreenProps {
  inputValue: string;
  onInputChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onKeyPress: (e: React.KeyboardEvent<HTMLInputElement>) => void;
  onSend: () => void;
  onToggleRecording: () => void;
  isRecording: boolean;
  onGenerateRoute: () => void;
  onQuickAction: (text: string) => void;
}

const WelcomeScreen: React.FC<WelcomeScreenProps> = ({ 
  inputValue, 
  onInputChange, 
  onKeyPress, 
  onSend, 
  onToggleRecording, 
  isRecording,
  onGenerateRoute,
  onQuickAction 
}) => {
  return (
    <div className="h-full flex flex-col items-center justify-center px-6 pb-32">
      <h2 className="text-4xl font-bold text-gray-900 mb-3 text-center">
        ¿Qué trámite quieres solucionar hoy?
      </h2>
      <p className="text-gray-600 text-center max-w-2xl mb-8">
        Escribe el nombre del trámite, describe tu situación o pregúntame lo que necesites.
      </p>
      
      {/* Opciones de trámites comunes */}
      <div className="w-full max-w-3xl mb-6 flex gap-2 justify-center flex-wrap">
        <button
          onClick={() => onQuickAction('Necesito renovar mi cédula')}
          className="px-6 py-2.5 bg-white border border-gray-200 rounded-full text-sm text-gray-700 hover:border-gray-300 hover:bg-gray-50 transition-all shadow-sm"
        >
          Renovar cédula
        </button>
        <button
          onClick={() => onQuickAction('Quiero sacar mi pasaporte')}
          className="px-6 py-2.5 bg-white border border-gray-200 rounded-full text-sm text-gray-700 hover:border-gray-300 hover:bg-gray-50 transition-all shadow-sm"
        >
          Obtener pasaporte
        </button>
        <button
          onClick={() => onQuickAction('Necesito licencia de conducir')}
          className="px-6 py-2.5 bg-white border border-gray-200 rounded-full text-sm text-gray-700 hover:border-gray-300 hover:bg-gray-50 transition-all shadow-sm"
        >
          Licencia de conducir
        </button>
      </div>
      
      {/* Input en la pantalla de bienvenida */}
      <div className="w-full max-w-3xl flex items-center gap-3">
        <button
          onClick={onGenerateRoute}
          className="p-3 bg-white hover:bg-gray-50 text-gray-700 rounded-xl border border-gray-200 transition-colors flex-shrink-0 shadow-sm"
          title="Generar ruta óptima"
        >
          <Map className="w-5 h-5" />
        </button>
        <div className="flex-1 flex items-center gap-3 bg-white border border-gray-200 rounded-full px-6 py-3 shadow-sm">
          <input
            type="text"
            value={inputValue}
            onChange={onInputChange}
            onKeyPress={onKeyPress}
            placeholder="Escribe tu mensaje"
            className="flex-1 bg-transparent outline-none text-gray-800 placeholder-gray-400 text-sm"
          />
          <button
            onClick={onToggleRecording}
            className={`p-2 rounded-full transition-colors ${
              isRecording
                ? 'bg-red-500 text-white'
                : 'hover:bg-gray-100 text-gray-600'
            }`}
          >
            <Mic className="w-4 h-4" />
          </button>
          <button
            onClick={onSend}
            disabled={!inputValue.trim()}
            className="p-2.5 bg-yellow-400 text-gray-900 rounded-full hover:bg-yellow-500 transition-colors disabled:opacity-40 disabled:cursor-not-allowed shadow-sm"
          >
            <Send className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
};

export default WelcomeScreen;
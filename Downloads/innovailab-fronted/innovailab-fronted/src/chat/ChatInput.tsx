import React from 'react';
import { Map, Mic, Send } from 'lucide-react';

interface ChatInputProps {
  inputValue: string;
  onInputChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onKeyPress: (e: React.KeyboardEvent<HTMLInputElement>) => void;
  onSend: () => void;
  onToggleRecording: () => void;
  isRecording: boolean;
  onGenerateRoute: () => void;
}

const ChatInput: React.FC<ChatInputProps> = ({ 
  inputValue, 
  onInputChange, 
  onKeyPress, 
  onSend, 
  onToggleRecording, 
  isRecording,
  onGenerateRoute 
}) => {
  return (
    <div className="p-6 bg-white">
      <div className="max-w-4xl mx-auto flex items-center gap-3">
        <button
          onClick={onGenerateRoute}
          className="p-3 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-full transition-colors flex-shrink-0"
          title="Generar ruta óptima"
        >
          <Map className="w-5 h-5" />
        </button>
        <div className="flex-1 flex items-center gap-3 bg-white border border-gray-300 rounded-full px-5 py-3 shadow-sm">
          <input
            type="text"
            value={inputValue}
            onChange={onInputChange}
            onKeyPress={onKeyPress}
            placeholder="Escribe tu mensaje"
            className="flex-1 bg-transparent outline-none text-gray-800 placeholder-gray-500"
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
            className="p-2 bg-yellow-400 text-gray-900 rounded-full hover:bg-yellow-500 transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <Send className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatInput;
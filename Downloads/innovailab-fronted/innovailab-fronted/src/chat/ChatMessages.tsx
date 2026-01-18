import React, { useRef, useEffect } from 'react';
import MessageBubble from './MessageBubble';
import type { Message } from './types';

interface ChatMessagesProps {
  messages: Message[];
  onGenerateRoute: () => void;
}

const ChatMessages: React.FC<ChatMessagesProps> = ({ messages, onGenerateRoute }) => {
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  return (
    <div className="flex-1 overflow-y-auto bg-gray-50">
      <div className="px-6 py-6">
        <div className="max-w-4xl mx-auto space-y-4">
          {messages.map(message => (
            <MessageBubble
              key={message.id}
              message={message}
              onGenerateRoute={onGenerateRoute}
            />
          ))}
          <div ref={messagesEndRef} />
        </div>
      </div>
    </div>
  );
};

export default ChatMessages;
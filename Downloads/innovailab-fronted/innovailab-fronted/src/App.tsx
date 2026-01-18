import React, { useState } from 'react';
import Sidebar from './chat/Sidebar';
import TabBar from './chat/TabBar';
import WelcomeScreen from './chat/WelcomeScreen';
import ChatMessages from './chat/ChatMessages';
import ChatInput from './chat/ChatInput';
import type { Tab, Message, MessagesState, Connections, HistoryItem } from './chat/types';

const App: React.FC = () => {
  const [tabs, setTabs] = useState<Tab[]>([
    { id: 1, title: 'Consulta Principal', active: true }
  ]);
  const [activeTabId, setActiveTabId] = useState<number>(1);
  const [messages, setMessages] = useState<MessagesState>({
    1: []
  });
  const [inputValue, setInputValue] = useState<string>('');
  const [isRecording, setIsRecording] = useState<boolean>(false);
  const [connections, setConnections] = useState<Connections>({
    whatsapp: false,
    calendar: false,
    gmail: false
  });
  const [history] = useState<HistoryItem[]>([
    { id: 1, title: 'Renovación de cédula', date: 'Hace 2 horas', status: 'completed' },
    { id: 2, title: 'Visa americana', date: 'Ayer', status: 'in-progress' },
    { id: 3, title: 'Licencia de conducir', date: 'Hace 3 días', status: 'completed' }
  ]);

  const addNewTab = () => {
    const newId = Math.max(...tabs.map(t => t.id)) + 1;
    const newTab: Tab = { 
      id: newId, 
      title: `Consulta ${newId}`, 
      active: false 
    };
    setTabs([...tabs, newTab]);
    setMessages({ ...messages, [newId]: [] });
    setActiveTabId(newId);
  };

  const closeTab = (tabId: number) => {
    if (tabs.length === 1) return;
    
    const newTabs = tabs.filter(t => t.id !== tabId);
    setTabs(newTabs);
    
    if (activeTabId === tabId) {
      setActiveTabId(newTabs[0].id);
    }
    
    const newMessages = { ...messages };
    delete newMessages[tabId];
    setMessages(newMessages);
  };

  const switchTab = (tabId: number) => {
    setActiveTabId(tabId);
  };

  const handleSendMessage = () => {
    if (!inputValue.trim()) return;

    const userMessage: Message = {
      id: Date.now(),
      role: 'user',
      content: inputValue
    };

    const currentMessages = messages[activeTabId] || [];
    
    setTimeout(() => {
      const assistantMessage: Message = {
        id: Date.now() + 1,
        role: 'assistant',
        content: 'Entendido. Para ese trámite vas a necesitar algunos documentos específicos...',
        showRouteButton: true
      };
      
      setMessages({
        ...messages,
        [activeTabId]: [...currentMessages, userMessage, assistantMessage]
      });
    }, 800);

    setMessages({
      ...messages,
      [activeTabId]: [...currentMessages, userMessage]
    });
    
    setInputValue('');
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const toggleRecording = () => {
    setIsRecording(!isRecording);
  };

  const handleGenerateRoute = () => {
    const currentMessages = messages[activeTabId] || [];
    const routeMessage: Message = {
      id: Date.now(),
      role: 'assistant',
      content: 'Te armé la mejor ruta según tu ubicación...',
      isRoute: true
    };
    
    setMessages({
      ...messages,
      [activeTabId]: [...currentMessages, routeMessage]
    });
  };

  const toggleConnection = (service: keyof Connections) => {
    setConnections({
      ...connections,
      [service]: !connections[service]
    });
  };

  const handleQuickAction = (text: string) => {
    setInputValue(text);
  };

  const currentMessages = messages[activeTabId] || [];
  const showWelcome = currentMessages.length === 0;

  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar */}
      <Sidebar
        connections={connections}
        onToggleConnection={toggleConnection}
        history={history}
      />

      {/* Chat Area */}
      <div className="flex-1 flex flex-col bg-white">
        
        {/* Tabs */}
        {!showWelcome && (
          <TabBar
            tabs={tabs}
            activeTabId={activeTabId}
            onSwitchTab={switchTab}
            onCloseTab={closeTab}
            onAddTab={addNewTab}
          />
        )}

        {/* Messages / Welcome */}
        {showWelcome ? (
          <div className="flex-1 overflow-y-auto bg-gray-50">
            <WelcomeScreen
              inputValue={inputValue}
              onInputChange={(e) => setInputValue(e.target.value)}
              onKeyPress={handleKeyPress}
              onSend={handleSendMessage}
              onToggleRecording={toggleRecording}
              isRecording={isRecording}
              onGenerateRoute={handleGenerateRoute}
              onQuickAction={handleQuickAction}
            />
          </div>
        ) : (
          <>
            <ChatMessages
              messages={currentMessages}
              onGenerateRoute={handleGenerateRoute}
            />
            <ChatInput
              inputValue={inputValue}
              onInputChange={(e) => setInputValue(e.target.value)}
              onKeyPress={handleKeyPress}
              onSend={handleSendMessage}
              onToggleRecording={toggleRecording}
              isRecording={isRecording}
              onGenerateRoute={handleGenerateRoute}
            />
          </>
        )}
      </div>
    </div>
  );
};

export default App;
import React from 'react';
import { X, Plus } from 'lucide-react';

interface Tab {
  id: number;
  title: string;
  active: boolean;
}

interface TabBarProps {
  tabs: Tab[];
  activeTabId: number;
  onSwitchTab: (tabId: number) => void;
  onCloseTab: (tabId: number) => void;
  onAddTab: () => void;
}

const TabBar: React.FC<TabBarProps> = ({ 
  tabs, 
  activeTabId, 
  onSwitchTab, 
  onCloseTab, 
  onAddTab 
}) => {
  return (
    <div className="flex items-center gap-2 px-6 py-3 border-b border-yellow-500">
      {tabs.map(tab => (
        <div
          key={tab.id}
          className={`flex items-center gap-2 px-3 py-1.5 rounded-md cursor-pointer text-sm transition-colors ${
            activeTabId === tab.id
              ? 'bg-yellow-400 text-gray-900 font-medium'
              : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
          }`}
          onClick={() => onSwitchTab(tab.id)}
        >
          <span className="whitespace-nowrap">{tab.title}</span>
          {tabs.length > 1 && (
            <button
              onClick={(e) => {
                e.stopPropagation();
                onCloseTab(tab.id);
              }}
              className="hover:bg-black/10 rounded p-0.5"
            >
              <X className="w-3 h-3" />
            </button>
          )}
        </div>
      ))}
      <button
        onClick={onAddTab}
        className="p-1.5 hover:bg-gray-100 rounded-md"
      >
        <Plus className="w-4 h-4 text-gray-600" />
      </button>
    </div>
  );
};

export default TabBar;
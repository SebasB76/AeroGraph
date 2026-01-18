# Chatbot con Gemini AI

Un chatbot inteligente alimentado por Google Generative AI (Gemini Pro) construido con React y TypeScript.

## Características

- 🤖 Respuestas inteligentes con Gemini AI
- 💬 Interfaz moderna y responsiva
- ⏰ Marcas de tiempo en cada mensaje
- ✨ Animaciones suaves
- 📱 Diseño adaptable para móvil y escritorio
- 🌐 Soporte para español e inglés

## Requisitos Previos

1. Node.js v18+ instalado
2. Una cuenta de Google
3. Clave API de Google Generative AI

## Configuración

### 1. Obtener la Clave API de Google

1. Ve a [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Haz clic en "Create API Key"
3. Copia la clave generada

### 2. Configurar Variables de Entorno

1. Copia el archivo `.env.example` a `.env.local`:
   ```bash
   cp .env.example .env.local
   ```

2. Abre `.env.local` y reemplaza `your_api_key_here` con tu clave API:
   ```
   VITE_GOOGLE_API_KEY=tu_clave_api_aqui
   ```

### 3. Instalar Dependencias

```bash
npm install
```

### 4. Ejecutar el Desarrollo

```bash
npm run dev
```

El chatbot estará disponible en `http://localhost:5174`

## Uso

1. Escribe tu mensaje en el campo de entrada
2. Presiona Enter o haz clic en el botón "Enviar"
3. El chatbot responderá usando Gemini AI

## Construcción para Producción

```bash
npm run build
```

Los archivos compilados estarán en la carpeta `dist/`

## Estructura del Proyecto

```
src/
├── components/
│   └── Chatbot.tsx          # Componente principal del chatbot
├── styles/
│   └── Chatbot.css          # Estilos del chatbot
├── App.tsx                  # Componente raíz
├── main.tsx                 # Punto de entrada
└── index.css                # Estilos globales
```

## Tecnologías Utilizadas

- **React 19** - Librería de UI
- **TypeScript** - Tipado estático
- **Vite** - Herramienta de construcción
- **Google Generative AI** - API de IA
- **CSS3** - Estilos con animaciones

## Personalización

### Cambiar el modelo de IA

En `src/components/Chatbot.tsx`, puedes cambiar el modelo:

```typescript
const model = genAIRef.current.getGenerativeModel({ 
  model: 'gemini-pro'  // Cambia a otro modelo disponible
})
```

### Ajustar configuración de generación

```typescript
generationConfig: {
  maxOutputTokens: 1000,  // Aumenta para respuestas más largas
  temperature: 0.5,       // Ajusta la creatividad (0-1)
  topP: 0.9,             // Diversidad de respuestas
}
```

## Troubleshooting

### Error: "No se encontró la clave API"

- Verifica que el archivo `.env.local` existe
- Confirma que `VITE_GOOGLE_API_KEY` está correctamente configurada
- Reinicia el servidor de desarrollo

### Error: "Error al obtener respuesta"

- Verifica que tu clave API es válida
- Comprueba tu conexión a internet
- Revisa los límites de uso de tu API key

## Licencia

MIT

## Soporte

Para preguntas o problemas, abre un issue en el repositorio.

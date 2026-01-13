# Arquitectura

## Objetivo
Arquitectura simple, mantenible y preparada para:
- flujo “voice-first” (STT + TTS)
- “agente” orquestador de intents
- Firebase como backend (usuarios, actividades, registros, etc.)

## Capas
- **ui/**: Activities, Adapters, ViewModels (MVVM)
- **domain/**: modelos + contratos (repositorios) (sin dependencias Android)
- **data/**: implementaciones y fuentes de datos (Firebase stubs por ahora)
- **agent/**: orquestador: interpreta texto y devuelve una respuesta tipada
- **voice/**: managers para Speech-to-Text y Text-to-Speech
- **common/**: Result/UiState y utilidades compartidas

## MVVM (mínimo viable)
- `MainActivity`:
  - solo UI + binding + listeners
  - observa un `StateFlow` del ViewModel
- `MainViewModel`:
  - recibe eventos (enviar texto, hablar, etc.)
  - llama al `AgentOrchestrator`
  - expone estado para la UI

## Agente (concepto)
`AgentOrchestrator`:
- Entrada: texto del usuario
- Salida: `AgentResponse` (sealed class):
  - ShowActivities(list)
  - ShowMessage(text)
  - ShowError(text)
  - AskForFields(fieldsNeeded)

Hoy: parser por palabras clave (placeholder).  
Futuro: NLU/LLM, reglas, validaciones, contexto, etc.

## Voz
- `SpeechToTextManager`: encapsula reconocimiento de voz, permisos y callbacks
- `TextToSpeechManager`: encapsula TTS, colas y lifecycle

Hoy: stubs para no bloquear compilación.  
Futuro: implementación real con Android SpeechRecognizer y TextToSpeech.

## Evolución recomendada (sin prisas)
- Añadir repositorios y casos de uso por feature
- Introducir inyección de dependencias cuando haga falta (Hilt/Koin) **solo si compensa**
- Tests de AgentOrchestrator y lógica de ViewModels

# Accesibilidad (enfoque mayores)

## Principios
- Texto grande y legible (mínimo ~16sp, preferible 18–20sp en contenido)
- Contraste adecuado
- Botones grandes y con etiquetas claras
- Feedback inmediato (visual y, a futuro, TTS)
- Evitar flujos complejos: confirmaciones explícitas

## En esta base del proyecto
- Título grande
- Botón “Hablar” grande
- `contentDescription` en botones
- Área de estado visible
- Layout simple y estable (RecyclerView + tarjetas)

## Checklist por PR
- ¿Se puede usar sin precisión táctil?
- ¿Los textos y botones tienen tamaños adecuados?
- ¿El foco se mueve de forma lógica?
- ¿Hay mensajes claros ante error?
- ¿Se evita “solo color” para comunicar estados?

# Contribuir

Este repo está pensado para un equipo de **5 personas** trabajando en paralelo, con PRs pequeños y revisiones consistentes.

## Principios
- PRs pequeños (idealmente < 300 líneas netas)
- Un PR = un objetivo
- Accesibilidad como requisito (no “nice to have”)
- Evitar sobre-ingeniería: base sólida y extensible

## Ramas
- `main`: producción/estable
- `develop`: integración
- `feature/<breve-descripcion>`: nuevas funcionalidades
- `fix/<breve-descripcion>`: correcciones
- `docs/<breve-descripcion>`: documentación

## Flujo de trabajo recomendado
1. Crea issue (bug/feature)
2. Crea rama desde `develop`
3. Implementa y prueba en local:
   - `./gradlew ktlintCheck`
   - `./gradlew test`
   - `./gradlew lint`
   - `./gradlew assembleDebug`
4. Abre PR a `develop`
5. Revisión obligatoria (mínimo 1 reviewer; ideal 2 en cambios delicados)
6. Squash merge (preferido) o rebase merge

## Definición de “Done”
- Compila y ejecuta
- CI en verde
- Sin warnings críticos nuevos
- Accesibilidad básica revisada:
  - textos legibles
  - foco navegable
  - contentDescription en acciones
  - feedback claro (y futuro: TTS)
- Documentación actualizada si aplica

## Convenciones de commits
- `feat: ...`
- `fix: ...`
- `docs: ...`
- `refactor: ...`
- `test: ...`
- `chore: ...`

## Revisión de código
Los reviewers deberían comprobar:
- claridad del código (nombres, responsabilidad)
- coherencia con arquitectura (capas)
- riesgos de accesibilidad
- tests (si aplica)

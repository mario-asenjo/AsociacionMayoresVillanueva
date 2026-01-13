# Asociación Mayores Villanueva (Android)

App Android para el **Centro de Mayores – Villanueva de la Cañada**.  
Objetivo: incentivar actividad física con rutinas/actividades adaptadas y experiencia “**voice-first**” (voz + texto).

> Stack: Android Studio + Kotlin + XML (Views) + ViewBinding (NO Compose)  
> SDK: compileSdk/targetSdk 36, minSdk 26

## Badges (ajusta USERNAME_OR_ORG)
- CI: `https://github.com/USERNAME_OR_ORG/AsociacionMayoresVillanueva/actions/workflows/android.yml/badge.svg`
- Workflow: `https://github.com/USERNAME_OR_ORG/AsociacionMayoresVillanueva/actions/workflows/android.yml`

## ¿Qué hay implementado hoy?
- Proyecto Android funcional con **una sola pantalla** (MainActivity).
- Arquitectura base por capas (ui / domain / data) + módulos lógicos (agent / voice / common).
- CI con GitHub Actions: ktlintCheck + test + lint + assembleDebug.
- Firebase preparado por dependencias y documentación (sin `google-services.json`).

## Requisitos
- Android Studio (versión reciente)
- JDK 17
- Gradle Wrapper (incluido por configuración; si faltase el JAR binario, regenera el wrapper desde Android Studio)

## Cómo ejecutar
1. Clona el repo
2. Abre en Android Studio
3. (Opcional) Configura Firebase (ver `docs/firebase.md`)
4. Run ▶️ sobre un emulador o dispositivo

## Configurar Firebase (resumen)
1. Crea un proyecto en Firebase Console
2. Registra la app con el paquete: `com.masenjo_android.asociacionmayoresvillanueva`
3. Descarga `google-services.json` y colócalo en `app/google-services.json`
4. Sincroniza Gradle y ejecuta

> Importante: este repo **no** incluye `google-services.json` por seguridad.

## Flujo de ramas y PRs (resumen)
- `main`: estable
- `develop`: integración
- `feature/<tema>`, `fix/<tema>`, `docs/<tema>`
- PR obligatorio con revisión (ver `CONTRIBUTING.md`)

## Docs
- Arquitectura: `docs/arquitectura.md`
- Firebase: `docs/firebase.md`
- CI/CD: `docs/ci-cd.md`
- Accesibilidad: `docs/accesibilidad.md`

## Licencia
MIT. Ver `LICENSE`.

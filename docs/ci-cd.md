# CI/CD

## Objetivo
Asegurar calidad mínima en cada PR:
- Formato consistente (ktlint)
- Tests unitarios
- Android Lint
- Build debug

## Workflow
Archivo: `.github/workflows/android.yml`

Pasos:
1. Checkout
2. JDK 17
3. Setup Gradle (cache + wrapper validation)
4. ktlintCheck
5. test
6. lint
7. assembleDebug

## Badges
En `README.md` tienes enlaces plantilla. Cambia `USERNAME_OR_ORG`.

Ejemplo:
- `.../badge.svg`
- `.../actions/workflows/android.yml`

## Reglas recomendadas en GitHub (manual)
- Requerir CI verde antes de merge
- Requerir al menos 1 reviewer
- Proteger `main` y `develop`
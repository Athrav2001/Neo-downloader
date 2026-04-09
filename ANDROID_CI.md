# Android CI Setup

This repository is configured for Android-only GitHub Actions builds via:

- `.github/workflows/android-build.yml`

## What the workflow does

- Installs JBR 21
- Installs Android SDK packages:
  - `platforms;android-36`
  - `build-tools;36.1.0`
- Builds debug APK (`:app:assembleDebug`)
- Uploads build artifacts to the workflow run

## Secrets

No keystore secrets are required for this workflow.

## Local Android build

```bash
./gradlew :app:assembleDebug
```

Signed packaging task:

```bash
./gradlew :app:createBinariesForCi
```

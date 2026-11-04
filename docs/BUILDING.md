# Building

The project is built with Gradle. It needs JDK 17 or newer; the Gradle wrapper downloads everything else.

| Module | Platform |
|---|---|
| `webApp` | Web (Kotlin/Wasm) |
| `desktopApp` | Windows, macOS, Linux |
| `androidApp` | Android |
| `shared` | The app's UI, used by all three |

## Web

```bash
# development server, opens the browser
./gradlew :webApp:wasmJsBrowserDevelopmentRun

# production build
./gradlew :webApp:wasmJsBrowserDistribution
```

The production build lands in `webApp/build/dist/wasmJs/productionExecutable`, ready to host on any static server.

## Desktop

```bash
# run the app
./gradlew :desktopApp:run

# installer for the current OS: .msi on Windows, .dmg on macOS, .deb on Linux
./gradlew :desktopApp:packageDistributionForCurrentOS
```

Installers land in `desktopApp/build/compose/binaries/main`.

## Android

The Android app has two flavors:

| Flavor | What it is |
|---|---|
| `gms` | The Google Play version, with Firebase Crashlytics and Analytics. The default in the IDE. |
| `default` | Free of analytics and of Google services; the one GitHub releases ship. |

```bash
# install a debug build on a connected device or emulator
./gradlew :androidApp:installDefaultDebug

# release APK, without Firebase
./gradlew :androidApp:assembleDefaultRelease

# Google Play bundle, with Firebase
./gradlew :androidApp:bundleGmsRelease
```

The `gms` flavor needs the Firebase config at `androidApp/src/gms/google-services.json`, downloaded from the Firebase console.
It is kept out of git. The Google Services and Crashlytics plugins are applied only when a task's name contains
`gms`, so the `default` flavor builds without it.

APKs land in `androidApp/build/outputs/apk/<flavor>/<type>`, and the bundle in `androidApp/build/outputs/bundle`.
Signing keys are read from `local.properties`, or
from environment variables on CI. A build type is signed with its own keystore only when all four of its values are
set and the keystore exists; otherwise the debug build uses the default debug key and the release build is unsigned.

```properties
DEBUG_STORE_FILE=keys/debug.keystore
DEBUG_STORE_PASSWORD=...
DEBUG_KEY_ALIAS=...
DEBUG_KEY_PASSWORD=...

RELEASE_STORE_FILE=keys/release.keystore
RELEASE_STORE_PASSWORD=...
RELEASE_KEY_ALIAS=...
RELEASE_KEY_PASSWORD=...
```

`RELEASE_STORE_FILE` is relative to the project root.

## CI and releases

[`.github/workflows/build.yml`](../.github/workflows/build.yml) runs on GitHub Actions:

| Event | What runs | Output |
|---|---|---|
| Pull request to `main` | Debug builds: `default` Android APK, Linux `.deb`, development web app | Workflow artifacts |
| Tag `v*` | Release builds: signed `default` Android APK, `.deb`, `.dmg` and `.msi`, production web app | A GitHub release, and the web app on the website |

CI builds only the `default` flavor, so it needs no Firebase config. The Google Play bundle is built and uploaded by
hand.

- The tag must match `project-versionName`, e.g. `v0.1.0` for `0.1.0`.
- The release is created after every build succeeds, with generated notes and these files:
  `compose-playground-v0.1.0.apk`, `-linux-amd64.deb`, `-macos.dmg`, `-windows.msi` and `-web.zip`.
- macOS packages need a major version above 0, so the `.dmg` of `0.x.y` carries version `1.x.y`.
- After the release, the production web app replaces the `playground` folder of
  [fast-compose.github.io](https://github.com/fast-compose/fast-compose.github.io), which serves it at
  [fast-compose.github.io/playground](https://fast-compose.github.io/playground/).

### Secrets

Set these as repository secrets, under Settings → Secrets and variables → Actions. Only tag builds use them.

| Secret | Value |
|---|---|
| `RELEASE_KEYSTORE_BASE64` | The release keystore, Base64 encoded |
| `RELEASE_STORE_PASSWORD` | The keystore's password |
| `RELEASE_KEY_ALIAS` | The key's alias |
| `RELEASE_KEY_PASSWORD` | The key's password |
| `SITE_DEPLOY_TOKEN` | A fine-grained personal access token with **Contents: Read and write** on `fast-compose/fast-compose.github.io` only |

`GITHUB_TOKEN` can only push to this repository, so the website needs its own token.

## Versions

Every version lives in [`gradle/libs.versions.toml`](../gradle/libs.versions.toml), in sections: `#project` for the
app's name, package and version, then `#plugins`, `#common`, `#jvm` and `#android`.

The `generateBuildInfo` task writes `BuildInfo.kt` into the `shared` module from that catalog. It holds the app's name,
version and links, and the Fast Compose and nemo-editor versions that the About dialog shows.

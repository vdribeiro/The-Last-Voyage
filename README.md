# The Last Voyage

The Last Voyage is an educational narrative-driven adventure that unfolds across the vast emptiness of space.

This is a product of love born out of an astronomy tool and crafted by a single creator, using real data from our universe.

I hope you enjoy the journey!

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/I2I81J9XQL)

## Website

You can **download** the game and track the development [here](https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb).

## Supported Platforms
- Android
- iOS
- Windows
- macOS
- Linux
- Web

## Tech Stack

- UI: [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
- Database: [SQLDelight](https://sqldelight.github.io/sqldelight)
- Networking: [Ktor](https://ktor.io/)
- Monitoring: [Sentry](https://sentry.io/)
- Code Coverage: [Kover](https://github.com/Kotlin/kotlinx-kover)

This is a **Kotlin Multiplatform** project following **Clean Architecture**. 
Its design ensures that the business logic remains platform-agnostic with a strict Unidirectional Dependency Flow.

# Architecture

## Package Responsibilities

### core
Business logic agnostic implementations.
* **audio**: Audio Player implementation.
* **flow**: Coroutine dispatchers.
* **locale**: Localization and date/time formatting.
* **platform**: OS-specific APIs.
* **security**: Encryption, hashing, and UUID utilities.
* **telemetry**: Logging and crash reporting.

### data
Responsible for data persistence and retrieval.
* **config**: User preferences and configurations.
* **database**: SQLDelight implementations and drivers.
* **http**: Ktor client configuration and network logic.
* **resource**: Resource index.
* **serializer**: JSON parsing and serialization.
* **storage**: File system access.

### domain
Business rules and entities.
* **flag**: Feature flags.
* **usecase**: Implementation of specific business workflows.

### ui
What the user interacts with.
* **audio**: Audio player UI definition.
* **cheats**: Easter eggs.
* **lifecycle**: Platform-aware lifecycle observers.
* **navigation**: Routing logic and navigation graph definitions.
* **screen**: UI Composables and State management.
* **theme**: Design system.

### test
Test utilities like annotations and fake data.

## Testing Strategy
The testing structure mirrors the source code to ensure 1:1 coverage.

# Simple Forking

Create a local.properties file in the root directory. 
This is used for Sentry, Android signing, and Apple notarization.

## Sentry
- sentryDsn=YOUR_SENTRY_DSN

## Android Signing
- android.storeFile=path/to/keystore.jks 
- android.keyAlias=alias 
- android.keyPassword=password 
- android.storePassword=password

## Mac Notarization
- mac.sign.identity=AppleID
- mac.notarization.appleId=email@example.com 
- mac.notarization.teamId=TEAMID 
- mac.notarization.password=password

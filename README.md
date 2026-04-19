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

# Architecture

This is a **Kotlin Multiplatform** project following **Clean Architecture**.
Its design ensures that the business logic remains platform-agnostic with a strict Unidirectional Dependency Flow.

It is a single `composeApp` module targeting Android, iOS, Desktop (JVM), and Web (WASM-JS).
All shared code lives under `composeApp/src/commonMain/kotlin/com/hybris/tlv/`.

## Tech Stack

- UI: [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
- Database: [SQLDelight](https://sqldelight.github.io/sqldelight)
- Networking: [Ktor](https://ktor.io/)
- Monitoring: [Sentry](https://sentry.io/)
- Code Coverage: [Kover](https://github.com/Kotlin/kotlinx-kover)

## Package Responsibilities

### core
Business logic agnostic implementations.
* **audio**: Audio Player implementation.
    * `AudioPlayer`: Abstraction with a sealed `Action` interface (Play, Pause, Resume, Toggle). Manages playlist deduplication and shuffling and delegates platform-specific playback to subclasses.
* **flow**: Coroutine dispatchers.
    * `Dispatcher`: Provides Main, Default and IO coroutine dispatchers as mutable properties so tests can substitute their own implementations.
* **locale**: Localization and date/time formatting.
    * `Locale`: Methods to get the current system language (`Language`), locale-aware datetime formatting, and a `Flow` that emits on locale changes.
    * `DateTime`: Utilities for getting the current time in UTC/ISO8601, epoch and duration calculation.
    * `Language`: App default language and other language listings.
* **platform**: OS-specific APIs.
    * `Platform`: A sealed interface that defines the possible platforms that the application can run on.
    * `System`: A Debug build flag.
* **security**: Encryption, hashing, and UUID utilities.
    * `Uuid`: UUID generation via the best available platform algorithm.
* **telemetry**: Logging and crash reporting.
    * `Telemetry`: Facade delegates to a pluggable `TelemetryEngine`.
    * `Logger` is the composite implementation: uses `PlatformLogger` in dev mode and `SentryLogger` in production.
    * `Console` is an in-memory circular buffer for the in-app console feature.

### data
Responsible for data persistence and retrieval.
* **config**: User preferences and configurations.
    * `Config`: Implements the `ConfigManager` interface, responsible for fetching remote configs with a 1-hour cache TTL (zero in dev mode) and persisting, caching and exposing `Configs` and `Preferences`. Uses a Mutex-protected file I/O to prevent race conditions.
* **database**: SQLDelight implementations and drivers.
    * `DatabaseFactory`: Creates `AppDatabase` with column adapters for custom types (Set, Enum, Int) like `SetColumnAdapter`.
    * `Database`: Database extension helpers.
    * `SqlIO`: Converts SQLDelight queries to `Flow`.
    * `SqlDriver`: Creates the database driver.
    * `NoOpSqlDriver`: A no-op implementation of `SqlDriver`.
* **http**: Ktor client configuration and network logic.
    * `HttpClientFactory`: Configures Ktor with telemetry logging, timeouts, HTTP caching, encoding and JSON content negotiation.
    * `Network`: Network status helpers.
    * `Http`: Wraps GET requests in a `Result<Success|Error>` type, checking network availability first.
    * `URL`: Sealed class of all remote endpoints.
    * `HttpEngine`: Creates the http engine.
    * `NoOpHttpEngine`: A no-op implementation of `HttpClientEngine`. Returns 204 for every request.
* **resource**: Resource index.
    * `AudioResource`: Resource index for audio in `commonMain/resources/tracks`.
    * `FontResource`: Resource index for fonts in `commonMain/composeResources/font`.
    * `ImageResource`: Resource index for images in `commonMain/composeResources/drawable`.
    * `JsonResource`: Resource index for JSONs in `commonMain/composeResources/files`, which are translations and game data.
    * `ResourceLoader`: Loads resources via the Compose Resources API.
* **serializer**: JSON parsing and serialization.
    * `Json`: Helpers for encoding/decoding and URL-safe serialization.
* **storage**: File system access.
    * `File`: Declarations for getting the app data directory and suspending save/load/delete file operations.
    * `FilePath`: All local storage file paths.
    * `JsonFile`: Handles JSON files persistence.

### domain
Business rules and entities.
* **flag**: Feature flags.
    * `Flags`: Data class with boolean controls for feature flags.
    * `FeatureFlags`: Exposes flags as a mutable `StateFlow` for runtime toggling.
* **usecase**: Implementation of specific business workflows. Each feature is a sub-package containing the use case interface, its gateway implementation, domain models, and mappers, all co-located.
    * `Gateways`: Aggregates all gateways under a single `UseCases` interface, injecting `ConfigManager`, `AppDatabase`, and `HttpClient`.

### ui
What the user interacts with.
At root level we find the main composable function `App` that assembles the application UI and acts as the top-level container for the user-facing elements.
* **audio**: Audio player UI definition.
    * `AudioPlayer`: Composable to manage app audio. Pauses on app background, resumes on foreground, and provides the `LocalAudioPlayer`.
    * `Tracks`: Determines the correct playlist per screen (menu tracks, gameplay tracks, or silence).
* **cheats**: Cheat tools.
    * `EasterEggs`: Konami code detection for both keyboard and gesture inputs, navigating to the cheat screen on match.
* **lifecycle**: Platform-aware lifecycle observers.
    * `Lifecycle`: Composable that registers foreground/background callbacks, with an optional recomposition key.
* **navigation**: Routing logic and navigation graph definitions.
    * `Screen`: Sealed interface that enumerates all destinations (Serializable for nav arguments).
    * `Navigation`: Composable that sets up the `NavHost` and defines all the possible navigation destinations within the app, linking each `Screen` to its corresponding composable content. Also provides the `LocalNavController`.
    * `Routing`: Navigation extensions, a utility to safely open external URLs, and a `Channel` to decouple command sending defined in `Navigate` from the `NavController`, with backstack deduplication and a custom `NavType` for passing complex objects.
* **screen**: UI screens implementations. Each screen is a sub-package containing the screen composable and respective store for state management, all co-located.
    * `Store`: `ViewModel` with a `StateFlow<State>` as the single source of truth for the UI and reducer override to process actions from the UI.
    * `StoreFactory`: Factory for creating `Store` instances.
    * `Screen`: Wrapper composable handles the loading indicator and topbar with back button, help, music, and feedback callbacks.
    * `LoadingScreen`: Default screen to show when the app is being setup and there are no dependencies available.
* **theme**: Design system and component definition.
    * `Theme`: Main app theme definition that uses `LocalColorScheme` provided by `Color`, `LocalShapes` provided by `Shapes` and `LocalTypography` provided by `Typography`.
    * `Translations`: Composable helpers to read from `TranslationCache`. Provides `LocalTranslationState`.

### test
Test utilities like annotations and fake data shared across test source sets.

## Testing Strategy
The testing structure mirrors the source code to ensure 1:1 coverage.

## Dependency Management
Manual, no framework. At root level, we find the `Dependency` class that wires everything together.
`TLV` is the singleton entry point that initializes `Dependency` and telemetry, and holds feature flags and global listeners.

## UI
At the **ui** package root, we find `App`: the main composable that assembles the application UI and acts as the top-level container for the user-facing elements. Uses the composition locals: `LocalTranslationState`, `LocalNavController`, `LocalAudioPlayer`.
Compose screens use a custom **MVI Store pattern**. Each screen has a Store (`{Screen}Store`) and a Screen composable (`{Screen}Screen`). Jobs inside a Store can be launched with an ID to cancel/replace prior work.
The UI state uses `kotlinx.collections.immutable` (`ImmutableList`, `ImmutableSet`) to prevent accidental mutations.

## Platform Source Sets

| Source Set    | Platform            |
|---------------|---------------------|
| `androidMain` | Android API 26+     |
| `appleMain`   | iOS 16+ / macOS     |
| `desktopMain` | JVM (Linux/Win/Mac) |
| `webMain`     | WASM-JS             |

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

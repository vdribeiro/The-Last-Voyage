# The Last Voyage

---

The Last Voyage is an Educational Space Adventure born out of an astronomy tool.

You can check the Development Board [here](https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb).

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/I2I81J9XQL)

# Architecture Overview

A **Kotlin Multiplatform** project following **Clean Architecture**. 
Its design ensures that the business logic remains platform-agnostic with a strict unidirectional dependency flow.

---

## Package Responsibilities

### core
Low-level utilities that are agnostic of the business logic and should never depend on any other package.
* **flow**: Coroutine dispatchers.
* **locale**: Localization and date/time formatting.
* **security**: Encryption, hashing, and UUID utilities.
* **telemetry**: Logging and crash reporting.

### infrastructure
Manages communication with the underlying Operating System.
* **audio**: Audio Player implementation.
* **platform**: OS-specific APIs.
* **resource**: Resource index.

### data
Responsible for data persistence and retrieval.
* **config**: User preferences and configurations.
* **database**: SQLDelight implementations and drivers.
* **http**: Ktor client configuration and network logic.
* **serializer**: JSON parsing and serialization.
* **storage**: File system access.

### domain
Business rules and entities.
* **cheats**: Easter eggs and secret command handling.
* **command**: Command-bus pattern for decoupled communication.
* **flag**: Feature flags.
* **usecase**: Implementation of specific business workflows.

### ui
What the user interacts with.
* **lifecycle**: Platform-aware lifecycle observers.
* **navigation**: Routing logic and navigation graph definitions.
* **screen**: UI Composables and State management.
* **theme**: Design system.

### test
Test annotations.

## Testing Strategy
The testing structure mirrors the source code to ensure 1:1 coverage.

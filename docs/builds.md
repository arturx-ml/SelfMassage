# Build Guide

## Product Flavors

The app uses Android product flavors to produce two variants from a single codebase:

| Flavor | Application ID | Content |
|---|---|---|
| **free** | `ai.mlxdroid.selfmassage.free` | 3 zones, 8 techniques, 3 routines |
| **paid** | `ai.mlxdroid.selfmassage.paid` | 5 zones, 14 techniques, 6 routines |

Each flavor has its own `MassageRepository.kt` in a flavor-specific source set (`app/src/free/` and `app/src/paid/`). All other code (UI, domain, DI, navigation) is shared in `app/src/main/`.

Both flavors can be installed side-by-side on the same device because they have different application IDs.

## Build Variants

Combining flavors with build types produces 4 variants:

| Variant | Command | APK location |
|---|---|---|
| `freeDebug` | `./gradlew assembleFreeDebug` | `app/build/outputs/apk/free/debug/` |
| `freeRelease` | `./gradlew assembleFreeRelease` | `app/build/outputs/apk/free/release/` |
| `paidDebug` | `./gradlew assemblePaidDebug` | `app/build/outputs/apk/paid/debug/` |
| `paidRelease` | `./gradlew assemblePaidRelease` | `app/build/outputs/apk/paid/release/` |

### Build all variants at once

```bash
./gradlew assemble
```

## Running from Android Studio

1. Open `Build > Select Build Variant`
2. Select `freeDebug` or `paidDebug` (or the release counterparts)
3. Click Run

## Running Tests

### Shared unit tests (both flavors)

```bash
# Free flavor tests (shared + testFree)
./gradlew testFreeDebugUnitTest

# Paid flavor tests (shared + testPaid)
./gradlew testPaidDebugUnitTest
```

### All tests

```bash
./gradlew test
```

## Test Source Sets

| Directory | Scope |
|---|---|
| `app/src/test/` | Shared tests — run for both flavors |
| `app/src/testFree/` | Free-flavor-specific tests (validates free catalog) |
| `app/src/testPaid/` | Paid-flavor-specific tests (validates paid catalog) |

## Release Builds

Release builds have R8 minification and resource shrinking enabled. You need a signing config to produce a signed APK.

### 1. Create a keystore (one-time)

```bash
keytool -genkey -v -keystore selfmassage.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias selfmassage
```

### 2. Configure signing in `local.properties` (git-ignored)

```properties
STORE_FILE=../selfmassage.jks
STORE_PASSWORD=your_store_password
KEY_ALIAS=selfmassage
KEY_PASSWORD=your_key_password
```

### 3. Add signing config to `build.gradle.kts`

```kotlin
signingConfigs {
    create("release") {
        val props = project.rootProject.file("local.properties")
            .let { java.util.Properties().apply { load(it.inputStream()) } }
        storeFile = file(props["STORE_FILE"] as String)
        storePassword = props["STORE_PASSWORD"] as String
        keyAlias = props["KEY_ALIAS"] as String
        keyPassword = props["KEY_PASSWORD"] as String
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        // ... existing minify/shrink config
    }
}
```

### 4. Build signed APKs

```bash
./gradlew assembleFreeRelease
./gradlew assemblePaidRelease
```

## CI Quick Reference

```bash
# Build both flavors
./gradlew assembleFreeDebug assemblePaidDebug

# Run all unit tests
./gradlew testFreeDebugUnitTest testPaidDebugUnitTest

# Check which flavor at runtime
BuildConfig.FLAVOR_TIER  // "free" or "paid"
```

## Source Set Layout

```
app/src/
├── main/          # Shared: interface, DI, domain, UI, navigation, theme
├── free/          # Free MassageRepository (3 zones, 8 techniques, 3 routines)
├── paid/          # Paid MassageRepository (5 zones, 14 techniques, 6 routines)
├── test/          # Shared unit tests (FakeMassageRepository, ViewModel, UseCase)
├── testFree/      # Free-specific tests (MassageRepositoryTest for free catalog)
└── testPaid/      # Paid-specific tests (MassageRepositoryTest for paid catalog)
```

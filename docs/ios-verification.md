# iOS verification list (to check on the Mac)

Add an entry whenever `iosMain`, an iOS `actual`, or Swift code is added/changed.
Format: `- [ ] <date> <module/file>: what to build/run and the expected behavior`

## Always check on the Mac
- [ ] Project builds for iosSimulatorArm64 and iosArm64 (no JVM-only API in `commonMain`).
- [ ] App launches; Compose screens render.
- [ ] `Dispatchers.IO` in `data/commonMain` (`DefaultDispatcherProvider`) compiles for iOS targets.

## Entries
- [ ] W1: `data/iosMain/.../PlatformDataModule.ios.kt` (actual, empty module) compiles.
- [ ] W1: add local SPM package `WalletCore` (official release 4.8.4 `Package.swift`), products `WalletCore` and `WalletCoreSwiftProtobuf` linked to the iosApp target.
- [ ] W1: Kotlin: `export(project(":data"))` in the framework block, `api(project(":data"))` in shared commonMain.
- [ ] W1: `shared/iosMain/.../IosKoin.kt` (`startKoinIos(walletCoreBridge)`) compiles.
- [ ] W1: `iosApp/iosApp/SwiftWalletCoreBridge.swift` conforms to `WalletCoreBridge` (check generated Swift names).
- [ ] W1: call `IosKoinKt.startKoinIos(walletCoreBridge: SwiftWalletCoreBridge())` at app start; app launches.
- [ ] W1: XCTest with the public BIP-39 vector returns `0x9858EfFD232B4033E47d90003D41EC34EcaEda94` (same as Android).
- [ ] W1b: Swift bridge signature is `generateMnemonic(strengthBits: Int32)` (no word-count switch); coin id compared with `DataConstants.shared.COIN_ETHEREUM` (check the exact generated name).
- [ ] W1b: `iOSApp.swift` has `import Shared`; test target has `@testable import iosApp` and can find the `Shared` framework (FRAMEWORK_SEARCH_PATHS).
- [ ] W1b: `iosAppTests` folder lives next to `iosApp.xcodeproj` (not inside it) and is a member of the test target.
- [ ] P1-5: `shared/iosMain/.../tools/secure/SecureScreen.ios.kt` compiles; REAL iOS implementation still missing (release blocker, see status.md).
- [ ] P1-5: Navigation Compose (`AppNavHost`) works on iOS: Welcome -> Create wallet -> back (swipe back gesture).
- [ ] P1-5: Create wallet screen shows a generated 12-word phrase through the Swift bridge; Reveal toggles words.
- [ ] P2-1: `SwiftSecureStorageBridge.swift` (Keychain) conforms to `SecureStorageBridge`; `startKoinIos` now takes both bridges; XCTest put/get/contains/remove round trip passes.
- [ ] P2-1: Keychain item attributes: generic password, service from `DataConstants.IOS_KEYCHAIN_SERVICE`, `WhenUnlockedThisDeviceOnly`, not synchronizable.
- [ ] S1: iOS has no `SecretStore`/`KeyValueStore` implementation yet: `WalletRepository` cannot be resolved on iOS until S4 (Keychain + NSUserDefaults/Swift bridge).
- [ ] S2: `SecureScreen` wraps the whole create-wallet wizard (phrase, verify, done): the iOS actual is still a no-op (release blocker).
- [ ] S2: keyboard options for the verify fields (`autoCorrectEnabled = false`, password keyboard type) behave on iOS.
- [ ] S2: Startup -> Welcome/Home routing, `popUpTo` behavior and back gesture on iOS.

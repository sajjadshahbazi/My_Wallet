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
- [ ] S3: recover wallet screen (`SecureScreen`, multiline phrase input with password keyboard type) behaves on iOS; Welcome -> Recover -> Home with popUpTo.
- [ ] S4 design: iOS Keychain `SecretStore` must update an existing item (SecItemUpdate) instead of failing on duplicates, and purge orphan items on first launch.
- [ ] S3: Recover wallet screen (multi-line phrase field, SecureScreen) works on iOS; `clearBackStack` navigation (uses `currentBackStack`) works on iOS.
- [ ] S4 (to do on the Mac): iOS `SecretStore` (Keychain) and `KeyValueStore` (UserDefaults) through Swift bridges, real `SecureScreen`, then run the full create + recover + restart flow.
- [ ] S4: Kotlin: `SecretStoreBridge`, `KeyValueStoreBridge`, `BridgeSecretStore`, `BridgeKeyValueStore`, iOS `platformDataModule`, `startKoinIos(walletCoreBridge, secretStoreBridge, keyValueStoreBridge)` compile for iOS.
- [ ] S4: Swift `KeychainStore` conforms to both protocols (check generated names: putSecret(key:secret:), secretExists(key:), getSecret(key:), removeSecret(key:), getValue(key:), putValue(key:value:), removeValue(key:)).
- [ ] S4: Keychain XCTests pass (round trip, overwrite, secrets and values are separate).
- [ ] S4: Create wallet on iOS: phrase -> verify -> Done -> Home, kill app, relaunch: Home shows the same address.
- [ ] S4: Recover wallet on iOS with the phrase of a wallet created on Android: same Ethereum address as on Android.
- [ ] S4: Delete the app, reinstall: starts on Welcome (fresh-install wipe works).
- [ ] S4: `PrivacyShield`: app switcher shows a blur; starting screen recording shows a blur.

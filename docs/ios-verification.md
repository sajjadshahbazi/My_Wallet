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

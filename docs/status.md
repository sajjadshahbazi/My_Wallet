# Project status (update at the end of every task)

**Current phase:** P1 Foundation (P2-A "Wallet Core bridge, Android" done early; see Done)

## Done
- Project skeleton created (modules: `androidApp`, `shared`, `iosApp`). Runs on Android.
- `AGENTS.md` + `docs/` created; Agent reads them (verified).
- Step 3: modules `domain` and `data` created, wired to `shared`, Koin started in `MyWalletApplication`;
  `DispatcherProvider` (domain) + `DefaultDispatcherProvider` (data); `AppKoinTest` passes.
- W1 (Android): Trust Wallet Core 4.8.4 added via GitHub Packages; `WalletCoreBridge` (thin native bridge),
  `BridgeMnemonicGenerator`, `BridgeAddressDeriver`, `AndroidWalletCoreBridge`; domain types `Mnemonic` (redacted `toString`),
  `MnemonicLength`, `Chain`, `AppResult`, `AppError`; host tests pass; `:androidApp:assembleDebug` passes.

- P1-4: MVI base (`tools/mvi`), `common/Constants.kt` (NavigationKeys), theme (`AppTheme`, `AppDimens`, color schemes), `AppNavigator`
  interface, `features/welcome` (+ tests). Host tests pass.

## Pending verification
- `androidApp/src/androidTest/.../WalletCoreAndroidTest.kt` (real Wallet Core on emulator, known BIP-39 test vector):
  not run yet (androidx.test artifacts could not be downloaded on the dev machine). Not committed until it runs.
- W1 iOS (Swift bridge, SPM, `startKoinIos`, XCTest): written by the developer, review in progress; not verified yet. See `docs/ios-verification.md`.

## Target networks (final)
Ethereum (ERC-20), BNB Smart Chain (BEP-20), TRON (TRC-20), Bitcoin, Solana, TON. See `docs/phases.md` and `docs/chains.md`.

## Decisions (do not revisit without asking)
- DI: Koin (Hilt is Android-only). Koin modules: `dataModule` in `data/di` (uses `expect val platformDataModule`),
  `domainModule` and `presentationModule` will live in `shared/di` (domain must not import Koin). `startAppKoin()` in `shared/di`.
- Dispatchers: `DispatcherProvider` interface (not raw `CoroutineDispatcher`), for type-safe DI and deterministic tests.
- Navigation: String route keys in `common/NavigationKeys`; nav library: JetBrains Navigation Compose.
- Modules: `domain` and `data` are separate Gradle modules next to `androidApp`/`iosApp`; `shared` = presentation + DI root.
- `shared` packages: `features`, `common`, `components`, `tools`, `navigation`, `theme`, `di`.
- MVI base in `tools/mvi`: `Mvi<State, Effect> by mvi(State())` + `IntentHandler<Intent>` (`onIntent`); `Nothing` as Effect when none; `ObserveEffects` composable.
- Design tokens live in `theme/` (color schemes, `AppDimens`); UI text in Compose resources (`org.sajad.shahabzi.resources.Res`).
- Navigation: `AppNavigator` interface; implementation `ChannelAppNavigator` emits `NavigationCommand`s that `AppNavHost` (Navigation Compose, JetBrains `navigation-compose` 2.9.2, unverified pairing with CMP 1.12.1) applies to the NavController. Routes only from `NavigationKeys`.
- Amounts: `TokenAmount(raw: BigInteger, decimals: Int)`; stored as TEXT; JSON as String (not written yet).
- Wallet Core: native only (Android AAR from GitHub Packages, iOS SPM local `Package.swift` from the official release),
  never the KMP beta. Pinned 4.8.4. Official repo `trustwallet/wallet-core` only (no forks).
- Native bridge pattern: `WalletCoreBridge` (public interface in `data/commonMain`, primitive types only) is implemented natively
  per platform; all validation/mapping to `AppResult` is shared in `commonMain` (`Bridge*` classes) and tested with a fake bridge.
- Errors: `AppResult<T>` (Success/Failure) + sealed `AppError` in `domain/common`.
- GitHub token for GitHub Packages lives only in the user-level `~/.gradle/gradle.properties` (`gpr.user`, `gpr.key`), never in the project.
  Maven repo is restricted with `content { includeGroup("com.trustwallet") }`.
- Constants: one constants file per Gradle module (`Constants.kt` in shared, `DataConstants`, `DomainConstants`); no local constants, no repeated literals.
- Bridge contract: `generateMnemonic(strengthBits: Int)`; Kotlin common maps `MnemonicLength` to 128/256 bits, native code has no word-count logic.
- iOS verified manually on Mac; development on Windows.
- Existing `material3` alpha from the project wizard is accepted; do not add new alpha/beta dependencies.

## Unverified assumptions (verify when first used, then move to Decisions)
- KMP BigInteger/BigDecimal library choice (maintenance status, iOS support).
- `Dispatchers.IO` in `commonMain` compiles on iOS targets (Android side compiles).
- Swift/Kotlin names in the iOS bridge (`WalletCoreBridge` protocol, `IosKoinKt.startKoinIos`).
- Wallet Core JNI works on the emulator ABI (pending androidTest).
- Exact `Mvi`/`mvi()` base API shape.

## Known gaps (must be closed before any release)
- iOS `SecureScreen` is NOT implemented (actual only renders content). Must hide content in the app switcher and react to screen capture. Release blocker.
- The recovery phrase screen (P1-5) shows a freshly generated phrase that is NOT persisted: if the process dies, a different phrase is generated.
  Persistence + backup verification come with secure storage (P2). Never put real funds on a wallet created before then.
- Import wallet flow does not exist yet; the Import button was removed from Welcome and returns with the import feature (P2).

## Open questions
- (none)

## Next
- P1-5 (in progress): Navigation Compose, DI modules (`domainModule`, `presentationModule`), `App` wiring, `GenerateMnemonicUseCase`, `SecureScreen` (Android), `features/createwallet` (recovery phrase screen).
- P2-1: secure storage (Android Keystore) + persistence, backup verification, then lock/biometrics.
- Run `WalletCoreAndroidTest` on the emulator once dependencies download; then commit it.
- P1: money types (`TokenAmount`, BigInteger library), MVI base, `common/NavigationKeys`, theme, quality tooling
  (detekt/ktlint/Konsist), reference feature `receive`.
- P2: secure storage, biometrics, app lock, onboarding (create/import/backup verification).

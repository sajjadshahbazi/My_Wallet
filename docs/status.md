# Project status (update at the end of every task)

**Current phase:** P1 finishing, P2 (keys and wallet persistence) in progress

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

- P1-5: navigation (`AppNavHost`, `ChannelAppNavigator`), DI modules, `SecureScreen` (Android), `features/createwallet` (shows a generated phrase, NOT saved).

## Roadmap to a complete create + restore wallet (Android and iOS)
- P2-1 (in progress): wallet persistence layer: domain (`Wallet`, `WalletRepository`, `CreateWalletUseCase`, `GetWalletUseCase`), `SecureStorageBridge`
  (Android Keystore AES-GCM, iOS Keychain via Swift), `WalletRepositoryImpl`, tests with fakes. No UI.
- P2-2: create flow complete: confirm checkbox, verify 3 random words, save wallet, Home screen showing the Ethereum address, start routing (wallet exists -> Home).
- P2-3: restore flow: import 12/24-word phrase (validate, derive, save), Import button returns on Welcome.
- P2-4: app lock (PIN + biometrics), keys bound to authentication, fresh auth to reveal the phrase, Keystore invalidation recovery.
- P2-5: iOS parity pass on the Mac; extra chains one by one, each with independent test vectors.

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

- Wallet storage (P2-1): single wallet for now. Secret (mnemonic) and public data (addresses, backup flag) are stored through `SecureStorageBridge`
  as separate keys; the key `wallet.exists` (holds the storage version) is written LAST and removed FIRST, so a half-written wallet reads as "no wallet".
  Android: AES-256-GCM key in Android Keystore (StrongBox when available), ciphertext + IV in app-private SharedPreferences, entry key used as AAD.
  iOS: Keychain generic password, `WhenUnlockedThisDeviceOnly`, not synchronized. Authentication binding comes with P2-4.
- Restore = import by recovery phrase first (private-key import later). Welcome shows the Import button again in P2-3.

## Unverified assumptions (verify when first used, then move to Decisions)
- KMP BigInteger/BigDecimal library choice (maintenance status, iOS support).
- `Dispatchers.IO` in `commonMain` compiles on iOS targets (Android side compiles).
- Swift/Kotlin names in the iOS bridge (`WalletCoreBridge` protocol, `IosKoinKt.startKoinIos`).
- Wallet Core JNI works on the emulator ABI (verified manually by running the create screen if it showed a phrase).
- Android Keystore storage works on device/emulator (first real check in P2-2 end to end: create, kill app, reopen).
- Swift names for `SecureStorageBridge` and `DataConstants` constants.
- Exact `Mvi`/`mvi()` base API shape.

## Product behavior decisions
- With a stored wallet the app opens on Home (later: lock screen first), not on Welcome. Welcome/Create/Recover are reached from Home via "Add or recover a wallet".
- Home shows the most recently added wallet (wallet switching comes later).
- Development reset: `adb shell pm clear <applicationId>` wipes app data; the next launch starts on Welcome.
- After a successful create/recover the whole back stack is cleared and Home is shown (Back exits the app).

## Known gaps (must be closed before any release)
- iOS `SecureScreen` (Kotlin actual) stays a no-op by design: protection is the global Swift `PrivacyShield` (blur in the app switcher and while the screen is recorded or mirrored). iOS cannot block screenshots. Must be verified on a device before release.
- iOS Keychain items use `WhenUnlockedThisDeviceOnly` for now; biometric/passcode access control comes with the app-lock stage (S5, needs a migration).
- The recovery phrase screen (P1-5) shows a freshly generated phrase that is NOT persisted: if the process dies, a different phrase is generated.
  Persistence + backup verification come with secure storage (P2). Never put real funds on a wallet created before then.
- Import wallet flow does not exist yet; the Import button was removed from Welcome and returns with the import feature (P2).

- Wizard state uses `List<Int>` marked `@Immutable` (kotlinx-collections-immutable not added yet); switch to `ImmutableList` when that library is added.
- Typed verification words live only in composable-local state (never in ViewModel state, intents are redacted); they are lost on rotation by design.

- BIP-39 passphrase (25th word) is not supported yet: phrases created with a passphrase elsewhere recover a different wallet.
- Only one wallet per device for now (Startup opens the first wallet). Multiple wallets and "remove wallet" (needs authentication) come later.
- Development reset: Android `adb shell pm clear <applicationId>`; iOS: erase the simulator or delete the app AND purge Keychain items (Keychain survives app deletion).

## Open questions
- (none)

## Next (P2 roadmap, Android first, iOS in one Mac batch)
- S1 (done): wallet persistence: `Wallet`, `WalletRepository`, `CreateWalletUseCase`, `GetWalletsUseCase`, `SecretStore` (Android Keystore), `KeyValueStore`; explicit backup exclusion rules.
- S2 (done, verified on Android emulator: create, verify, persist, open Home after restart): feature `createwallet` as a wizard (phrase -> verify 3 words -> done with address), `startup` routing (STARTUP -> WELCOME or HOME), minimal `home` showing the address, shared `LabeledCheckbox` component, `AppNavigator.navigateTo(route, popUpTo)`.
- S3 (done on Android): feature `recoverwallet` (recover by recovery phrase: 12/15/18/21/24 words) with its own state and errors; `ParseMnemonicUseCase`, `RecoverWalletUseCase`; Welcome gets the recover button back.
- S4 (in progress): iOS batch: `SecretStoreBridge` + `KeyValueStoreBridge` (Kotlin interfaces, Swift `KeychainStore`), `PrivacyShield` (Swift), `startKoinIos` with three bridges, then run create + recover + restart on the Mac and all of `docs/ios-verification.md`.
- S5: app lock (biometric + PIN), Keystore key bound to user authentication (re-encrypt with a storage migration), auto-lock.

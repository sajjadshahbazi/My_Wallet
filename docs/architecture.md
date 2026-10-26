# Architecture (Clean + MVI)

## Modules
```
androidApp/   Android entry point only (MainActivity, manifest). No logic.
iosApp/       Xcode project and Swift code (iOS entry, platform bridges).
shared/       Presentation: Compose UI, ViewModels, navigation, theme, DI root.
domain/       Pure Kotlin business layer. Own Gradle module.
data/         Repository implementations, network, database, platform security. Own Gradle module.
```
Dependency rules (hard):
- `androidApp` -> `shared`
- `shared` -> `domain`. `shared` may import `data` ONLY inside the `di` package (composition root).
- `data` -> `domain`
- `domain` -> nothing in the project. Only Kotlin stdlib, coroutines, datetime and our big-number wrapper.
  No Android, Compose, Ktor, Koin, SQLDelight or `java.*` in `domain`.

## `shared` packages (under `org.sajad.shahabzi`, in `commonMain`)
```
features/<feature>/   One package per feature (receive, send, swap, home, onboarding, ...)
common/               Constants.kt: the ONE constants file of `shared` (NavigationKeys, AppConstants: route keys, argument names, shared static fields)
components/<name>/    Reusable UI used by several features, one package per component/dialog
                      (components/datepicker, components/confirmdialog, components/amountinput, ...)
tools/                Reusable helpers (tools/format for digit grouping, tools/mvi base, tools/clipboard, ...)
navigation/           AppNavigator abstraction and nav graph
theme/                AppTheme, colors, typography, spacing
di/                   Koin modules for presentation; the only place that wires `data`
```
Inside a feature:
```
features/send/
  SendScreen.kt        Screen + private Content + Previews
  SendViewModel.kt
  SendState.kt  SendIntent.kt  SendEffect.kt
  models/              UI models and mappers (domain -> UI)
  components/          Composables used only by this feature
  tools/               UI helpers used only by this feature
```
Rules:
- A feature must NOT import another feature. Shared things go to `common`, `components` or `tools`.
- `common` holds constants/simple declarations only: no logic, no state, no imports from `features`, `components`, `tools`.
- `components` must NOT import `features`. Components are stateless: state in, callbacks out; no ViewModel, no repository.
- Check `tools/` and `components/` for an existing helper before creating a new one.
- Platform-specific code: interface in `domain` (or `shared`), implementation in `data`/platform source set via
  `expect/actual` or a Koin-injected interface. iOS implementations written in Swift are injected at app start.

## `domain` and `data` packages
Organized by area first, then layer:
```
domain/<area>/{model, repository, service, usecase}      domain/common/{error, result, money, dispatcher}
(`service` = small role interfaces implemented in `data` that are not repositories, e.g. MnemonicGenerator, AddressDeriver)
data/<area>/{repository, remote(api,dto), local, mapper}      data/security/   data/walletcore/
```
Areas: wallet, account, chain, token, transaction, send, swap, price, security, settings.

## Layers
`presentation -> domain <- data`
- **Domain**: entities/value objects, `XxxRepository` interfaces, `XxxUseCase` classes (single `operator fun invoke`,
  one responsibility), domain errors. No formatting, no UI strings.
- **Data**: `XxxRepositoryImpl`, API clients, DTOs, DB, mappers DTO -> domain. DTOs never leave `data`.
- **Presentation**: UI models, formatting, ViewModels, Compose.
- ViewModels call UseCases, not repositories (unless a use case would be a pure pass-through and it was agreed).
- **Errors**: no exceptions across layer boundaries. Sealed `AppError` hierarchy + result type in `domain/common`.
  Always rethrow `CancellationException`.
- **Coroutines**: inject a plain `CoroutineDispatcher` through the constructor, selected with the Koin qualifier names in
  `domain/common/dispatcher/DispatcherQualifier` (`IO`, `DEFAULT`). Take only the dispatcher a class needs. Never hardcode
  `Dispatchers.*` outside `data/di`. `Main` is not injected (`viewModelScope` already runs on Main). No `GlobalScope`;
  structured concurrency only.

## MVI contract
Base lives in `tools/mvi` (delegation style):
`class XxxViewModel(...) : ViewModel(), Mvi<XxxState, XxxIntent, XxxEffect> by mvi(XxxState())`
exposing `uiState: StateFlow<S>`, `effects: Flow<E>`, `onIntent(intent)`, plus helpers `currentUiState`,
`updateUiState { copy(...) }`, `sendEffect(effect)`. Do not change this API without approval.
- `XxxState`: immutable `data class` with defaults; lists are `ImmutableList`; never holds secrets longer than needed.
- `XxxIntent`: `sealed interface` (objects for no-arg, data classes for intents with data).
- `XxxEffect`: `sealed interface` for ONE-TIME UI events (snackbar, copy, haptic). Navigation goes through `AppNavigator`.
- One exhaustive `when (intent)` in `onIntent`; each branch delegates to a small private function.
- ViewModels never reference Compose, Android or iOS types.

## Navigation keys (`common`)
Every screen has exactly ONE route key: a String constant in `common`, and nowhere else.
```kotlin
// common/Constants.kt (together with AppConstants)
object NavigationKeys {
    const val HOME = "home"
    const val RECEIVE = "receive"
    const val SEND = "send"
    const val SWAP = "swap"
}
```
- Register: `composable(NavigationKeys.SEND) { SendScreen() }`.
- Navigate: `appNavigator.navigateTo(NavigationKeys.SEND)`. Never import another feature's package.
- Screens with arguments: key + a small route-builder function + argument-name constants next to it in `common`.
- Arguments carry only small non-sensitive identifiers (token ID, chain ID, tx ID). Never secrets; amounts never as numbers
  (raw String smallest-unit, or keep them in a shared domain/session holder).
- Adding a screen = add its key to `common` first. Other cross-feature constants also go in `common`,
  grouped in clearly named objects/files.

## Constants (one file per Gradle module)
Every fixed value with a meaning (identifiers, keys, route names, coin ids, library names, limits, sizes, timeouts, URLs) is a
`const val` in the module's single constants file, and everything else references it. Never define a constant next to its use and never
repeat the literal in another place.

| Module | File | Object(s) |
|---|---|---|
| `shared` | `shared/.../common/Constants.kt` | `NavigationKeys`, `AppConstants` |
| `data` | `data/.../common/DataConstants.kt` | `DataConstants` |
| `domain` | `domain/.../common/DomainConstants.kt` | `DomainConstants` |

Why one file per module and not a single file in `shared/common`: `domain` and `data` cannot import `shared` (dependency direction),
and `data` is the one that needs identifiers like the bridge coin ids. A value belongs to the lowest module that needs it; higher modules
reference it (`shared` -> `data` -> `domain`).
- Allowed as local literals: trivial values (0, 1, empty string, separators), and test inputs/expected values in tests.
- UI text is not a constant: it goes in Compose resources.
- Swift code references exported constants (e.g. `DataConstants.shared.COIN_ETHEREUM`) instead of repeating the string.

## Dependency injection (Koin)
- `domain` must not import Koin. Therefore: `dataModule` lives in `data/di` (data may use Koin core);
  `domainModule` (use cases) and `presentationModule` (ViewModels) live in `shared/di`.
  `startAppKoin()` in `shared/di` assembles all modules.
- Platform pieces are provided with `expect val platformDataModule: Module` (actual in `androidMain`/`iosMain`) which `dataModule` includes.
  iOS implementations written in Swift are passed in at app start (`startKoinIos(...)` in `shared/iosMain`).
- Constructor injection only. `get()/inject()` only in DI modules; `koinViewModel()` only in `XxxScreen`.
- Inject dispatchers (`DispatcherProvider`), clock, logger and platform services so they can be faked.

## Native bridge pattern (platform libraries such as Wallet Core)
- A thin public interface in `data/commonMain` (e.g. `WalletCoreBridge`) uses only primitive types (String, Boolean, Int).
- Each platform implements it natively (Android: Kotlin/JNI in `androidMain`; iOS: Swift). Keep native code minimal.
- All validation, error mapping and conversion to domain types (`AppResult`, `Mnemonic`) is written once in `commonMain`
  (`Bridge*` classes) and unit-tested with a fake bridge. Native implementations are tested with the same public test vectors on both platforms.

## SOLID and patterns (use only when they solve a concrete problem here)
- S: one reason to change per class. O: new chain/DEX = new implementation, not edits to `when` chains.
  L: implementations honor the contract (including errors). I: small role interfaces (`TransactionSigner`,
  `AddressDeriver`, `SecureStorage`), not a giant `WalletManager`. D: depend on abstractions in `domain`.
- Repository (all data access); Strategy/Adapter (`ChainAdapter` per chain family, resolved by `ChainAdapterRegistry`);
  Factory (chain-specific signing inputs); Mapper (explicit DTO->domain, domain->UI);
  Observer (Flow for balances, prices, tx status); Decorator (cache/retry/logging around repos and clients);
  Facade (one `SwapQuoteProvider`-style interface over 1inch/0x/LI.FI/Jupiter);
  State machine (tx lifecycle: Draft -> Signing -> Broadcast -> Pending -> Confirmed/Failed as a sealed hierarchy).

## Libraries (approved; add to the version catalog when first needed; ask before anything else)
Koin; Ktor client + kotlinx.serialization; kotlinx.coroutines; kotlinx-datetime; SQLDelight (non-secret data only);
a KMP BigInteger/BigDecimal library (e.g. ionspin `bignum`) hidden behind our domain types (verify maintenance first);
kotlinx-collections-immutable; JetBrains Navigation Compose; Turbine, kotlin.test, kotlinx-coroutines-test, Ktor MockEngine;
Wallet Core for derivation/addresses/signing only.

## Naming and style
- `XxxUseCase`, `XxxRepository` (domain) / `XxxRepositoryImpl` (data), `XxxApi`, `XxxDto`, `XxxEntity` (DB),
  `XxxMapper` or `toDomain()/toUi()`, `XxxScreen`, `XxxViewModel`, `XxxState/Intent/Effect`, `XxxUiModel`.
- Packages lowercase, no underscores. One top-level public type per file, file named after it (exception: a constants file may hold several constant objects).
- Kotlin official style: `val`, immutability, sealed types over flags, named arguments for 3+ params.
- Public `domain` API has KDoc (behavior and failure cases). No magic numbers/strings: named constants.

## Rules enforced by Konsist tests (to be added in P1)
- `domain` has no imports from `androidx`, `io.ktor`, `org.koin`, `compose`, `java.`, `android.`
- `data` does not import `shared`; `shared` imports `data` only in `di`
- `features.X` does not import `features.Y`; `components` does not import `features`
- `common` has no imports from `features`, `components`, `tools`
- Route strings are declared only in `common` (no literal routes in `composable(...)` / `navigateTo(...)`)
- No `toLong()/toInt()/toDouble()` on money types in `domain` and `shared`
- `commonMain` has no `java.`/`android.` imports
- `const val` is declared only in the module's constants file (tests excluded)
- Naming suffixes (UseCase, Repository, ViewModel, State, Intent, Effect)
- Every `XxxScreen` file has a matching preview; no `Double`/`Float` in `domain`

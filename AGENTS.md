# MyWallet: multi-chain, non-custodial crypto wallet

Kotlin Multiplatform (Android + iOS), Compose Multiplatform UI. Base package: `org.sajad.shahabzi`.
This is a financial-security product: a small bug can mean lost user funds.
Correctness and security beat speed and cleverness. When in doubt, ASK. Do not invent behavior.

## Start of every task
1. Read `docs/status.md` (current phase, decisions, open issues, unverified assumptions).
2. Read the docs for the area you touch (table below). Rules tagged `[P1]`..`[P6]` follow `docs/phases.md`:
   rules of the current phase and earlier are mandatory; rules of later phases are not required yet,
   but never write code that would violate them. Do not build later-phase features unless asked.
3. Plan first: list files to create/change, new public types/functions, and the tests you will write.
   WAIT for approval before writing code. One small task at a time, no drive-by refactors.
4. Follow the reference feature (`features/receive`, once it exists) for structure and style.

## Which doc to read
| You are touching | Read |
|---|---|
| Modules, packages, layers, MVI, navigation keys, DI, patterns, naming | `docs/architecture.md` |
| Any Compose UI, ViewModel-to-UI wiring, previews, recomposition | `docs/compose.md` |
| Any amount, balance, fee, price, gas, decimals, formatting, DB/JSON of numbers | `docs/numbers.md` |
| Keys, storage, logging, clipboard, signing, network, dependencies | `docs/security.md` |
| Create/import/backup/delete wallet, accounts, key invalidation, storage format | `docs/wallet-lifecycle.md` |
| Chain-specific logic (EVM, Bitcoin, Solana, memo/tag chains) | `docs/chains.md` |
| Writing or running tests | `docs/testing.md` |
| iOS-specific code or Swift | `docs/ios-verification.md` |
| Future ideas | `docs/backlog.md` (do not implement from it unless asked) |

## Hard rules (always, every phase)
- Money/token amounts, fees, gas, prices: BigInteger/BigDecimal via our domain types. NEVER `Int`/`Long`/`Double`/`Float`,
  never `toLong()/toInt()/toDouble()`. Amounts can be up to uint256 (~78 digits) with 18+ decimals. See `docs/numbers.md`.
- NEVER log, print, persist in plain text, or send anywhere: seed phrase, private keys, passphrases, signed payloads.
- No secrets, API keys or test mnemonics in source. Never put secrets in nav args, state, effects or `toString()`.
- Never touch crypto, key storage or signing code unless the task explicitly says so. Never write your own crypto.
- Dependency direction: `presentation -> domain <- data`. `domain` is pure Kotlin. See `docs/architecture.md`.
- Every screen: `XxxScreen` (ViewModel + Koin) -> private `XxxContent(state, onIntent)` -> `@Preview`.
  Composables never call ViewModel functions directly; all actions go through `onIntent(...)`.
- Features never import other features. Route strings live only in `common` (`NavigationKeys`).
- `commonMain` has no `java.*` / `android.*` imports (iOS must keep compiling).
- No new dependency or version change without approval. Add versions only in `gradle/libs.versions.toml`, prefer stable releases.
- Core logic has tests (use cases, money math, ViewModels, mappers). Prefer fakes over mocks.
- No `TODO()`, no commented-out code, no debug prints, no fake implementations in production source sets.
- Do not disable or weaken any check, test or security rule to make something work. Ask instead.

## Definition of done
- `.\gradlew.bat :androidApp:assembleDebug` succeeds and tests for the change pass (`docs/testing.md`).
- Security impact considered (`docs/security.md`); numbers rules respected (`docs/numbers.md`).
- If `iosMain`, iOS `actual`s or Swift was touched: add an entry to `docs/ios-verification.md`.
- `docs/status.md` updated: what was done, decisions made, new open questions.
- If a doc rule is wrong, unclear or unverified: do NOT silently deviate. Say so and propose an edit to the doc.

## Platform notes
- Development runs on Windows (`.\gradlew.bat`). iOS cannot be built there; do not "fix" iOS-only build errors.
  Android must always build. iOS is verified on a Mac every few days via `docs/ios-verification.md`.
- Android minSdk 24: handle API differences (Keystore, biometrics), do not ignore them.
- Pinned stack (see `gradle/libs.versions.toml`): Kotlin 2.4.20, AGP 9.1.1, Compose Multiplatform 1.12.1.

## Never do
- Add/upgrade dependencies silently; put business logic in `androidApp`, Composables or DTOs.
- Skip previews, tests or the Screen/Content/`onIntent` pattern "to save time".
- Use real funds' keys or mainnet in development. Development uses testnets only until told otherwise.

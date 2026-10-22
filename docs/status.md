# Project status (update at the end of every task)

**Current phase:** P1 Foundation

## Done
- Project skeleton created (modules: `androidApp`, `shared`, `iosApp`). Runs on Android.
- `AGENTS.md` + `docs/` created.

## Decisions (do not revisit without asking)
- DI: Koin (Hilt is Android-only).
- Navigation: String route keys in `common/NavigationKeys`; nav library: JetBrains Navigation Compose.
- Modules: `domain` and `data` are separate Gradle modules next to `androidApp`/`iosApp`; `shared` = presentation + DI root.
- `shared` packages: `features`, `common`, `components`, `tools`, `navigation`, `theme`, `di`.
- MVI base in `tools/mvi`: `Mvi<State, Intent, Effect> by mvi(State())` with `onIntent`.
- Amounts: `TokenAmount(raw: BigInteger, decimals: Int)`; stored as TEXT; JSON as String.
- iOS verified manually on Mac; development on Windows.
- Existing `material3` alpha from the project wizard is accepted; do not add new alpha/beta dependencies.

## Unverified assumptions (verify when first used, then move to Decisions)
- Host test Gradle task name `testAndroidHostTest` (check with `.\gradlew.bat tasks`).
- Exact `Mvi`/`mvi()` base API shape.
- Choice of KMP BigInteger/BigDecimal library (maintenance status, iOS support).
- Koin, Ktor, SQLDelight and Wallet Core compatibility with Kotlin 2.4.20 and AGP 9.1.1.
- Wallet Core on iOS: Swift implementation injected into Koin vs cinterop.

## Open questions
- (none yet)

## Next
- P1 step 3: create `domain` and `data` modules, add Koin and base libraries to the version catalog.

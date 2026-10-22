# Testing

Core functionality MUST have tests. Prefer fakes over mocks (MockK is not multiplatform).

## What must be tested
- Domain: every UseCase; money types (`docs/numbers.md` boundaries); address validation; fee/amount logic; tx state machine.
- ViewModels: state transitions per intent, effects, error paths (Turbine + `runTest` + test dispatcher through the injected `DispatcherProvider`).
- Data: repositories with Ktor `MockEngine` or fake APIs; mappers; DTO parsing (including huge numbers).
- [P2] Crypto glue: derivation and signing against official test vectors (BIP-39/32/44 and chain-specific vectors from
  Wallet Core docs/tests). Never use real funds' keys in tests.
- [P2] Storage: encryption/decryption round trip, tampering detection, key-invalidated path, storage version migration.
- Compose: UI tests only for critical flows (send confirmation, unlock). Previews cover the rest.

## Conventions
- Tests live in the module they test; `commonTest` first, platform-specific only when necessary.
- Names: file `XxxTest`; functions camelCase with underscore: `invoke_withInvalidAddress_returnsError`.
- Arrange / Act / Assert; one behavior per test; no logic in tests; deterministic (no real time or network).
- Fakes live in `commonTest/fake` (or a shared test-fixtures package); reuse them.
- Run host tests per module, e.g.
  `.\gradlew.bat :domain:testAndroidHostTest :data:testAndroidHostTest :shared:testAndroidHostTest`.
  If a task name does not exist, run `.\gradlew.bat tasks`, then update this file; do not guess.

## Quality gates (added in P1)
detekt, ktlint, Konsist architecture tests, Kover coverage report, CI (GitHub Actions) running build + tests on every push.

# Phases

Rule tags `[P1]`..`[P6]` in the docs say when a rule becomes mandatory. The current phase is in `docs/status.md`.
Rules of the current and earlier phases are mandatory. Rules of later phases: do not build ahead, but never
write code that would make them impossible or unsafe to add.

| Phase | Goal | Done when |
|---|---|---|
| P1 Foundation | Modules (`domain`, `data`, `shared`), Koin, MVI base, `common` nav keys, theme, money types + tests, quality tooling (detekt/ktlint/Konsist), reference feature `receive` (UI + mock/in-memory data, no real keys) | Android builds, tests green, architecture rules enforced, reference feature approved |
| P2 Keys and lock | Wallet Core bridge (Android first), mnemonic generation, secure storage (Keystore/Keychain), biometric + PIN, app lock, SecureScreen, onboarding create/import, backup verification, storage versioning | Create/import/unlock works on Android; secrets never leave secure storage; tests with official vectors |
| P3 One EVM chain (testnet) | Balance, receive, send: fee estimation, nonce, signing, broadcast, tx status, confirmation screen, RPC validation | Send/receive works on a testnet; safety checks active |
| P4 Tokens and swap | Token list, scam-token handling, approvals, aggregator swap, slippage, re-quote | Swap works on testnet/mainnet-fork; approvals safe |
| P5 More chains and inputs | Solana, Bitcoin via `ChainAdapter`, ENS, payment URIs/QR, memo/tag chains | Each chain passes its own test vectors |
| P6 Integrations and release | WalletConnect, hardening (pinning, dependency verification, R8), privacy review, external audit, store compliance | Audit findings fixed, release checklist complete |

Mainnet with real funds is allowed only after P6 audit.

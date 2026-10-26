# Phases

Rule tags `[P1]`..`[P6]` in the docs say when a rule becomes mandatory. The current phase is in `docs/status.md`.
Rules of the current and earlier phases are mandatory. Rules of later phases: do not build ahead, but never
write code that would make them impossible or unsafe to add.

Target networks (final): Ethereum (ERC-20), BNB Smart Chain (BEP-20), TRON (TRC-20), Bitcoin, Solana, TON.

| Phase | Goal | Done when |
|---|---|---|
| P1 Foundation | Modules, Koin, MVI base, `common` constants, theme, navigation, money types + tests, quality tooling (detekt/ktlint/Konsist), first feature slice (Welcome) and reference feature `receive` | Android builds, tests green, architecture rules enforced, reference feature approved |
| P2 Keys and lock | Wallet Core bridge (all six coins: derivation verified with independent test vectors), secure storage (Keystore/Keychain), biometric + PIN, app lock, SecureScreen, onboarding create/import, backup verification, storage versioning | Create/import/unlock works on Android; secrets never leave secure storage; test vectors pass on both platforms |
| P3 EVM chains (testnets) | Ethereum (Sepolia) then BNB Smart Chain testnet with the same `EvmChainAdapter`: balance, receive, send, fee, nonce, signing, broadcast, tx status, confirmation screen, RPC validation | Send/receive works on both testnets; safety checks active |
| P4 Tokens and swap | ERC-20/BEP-20 first (token list, scam handling, approvals), then aggregator swap, slippage, re-quote | Token send/swap works on testnets; approvals safe |
| P5 Other chains | Suggested order by complexity: TRON (TRC-20), Solana (SPL), TON (Jettons), Bitcoin (UTXO). Each via `ChainAdapter`, own test vectors | Each chain passes its vectors and testnet send/receive |
| P6 Integrations and release | WalletConnect, hardening (pinning, dependency verification, R8), privacy review, external audit, store compliance | Audit findings fixed, release checklist complete |

Mainnet with real funds is allowed only after P6 audit.

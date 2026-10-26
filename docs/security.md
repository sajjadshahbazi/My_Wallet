# Security (non-custodial wallet)

Tags `[P1]`..`[P6]` = phase from which the rule is mandatory (`docs/phases.md`). Untagged = always.

## Secrets (always)
- Seed phrase, private keys and passphrases are never stored in plain text, never in SQLDelight/SharedPreferences/DataStore/files,
  never kept in app state longer than needed.
- Never log secrets, signed payloads or full RPC bodies. Use the injected `Logger` (redacting). Release builds log nothing sensitive.
- Wrap secrets in a `Secret`/`SensitiveData` type whose `toString()` is redacted. Never put a secret in a `data class` that has a
  generated `toString()` (State, Intent, Effect, UI models).
- Keep secrets in `ByteArray`/`CharArray` where possible and wipe after use. Strings cannot be wiped: keep their lifetime minimal.
- No secrets in `SavedStateHandle`, navigation args, deep links, Effects, analytics or crash reports.
- No secrets, API keys or test mnemonics in source/resources. Use git-ignored config files; keep them in `.aiexclude`.
  **Exception:** public, well-known BIP-39 test vectors (e.g. the "abandon ... about" phrase) may appear in tests only, with a comment
  saying so. Never any generated or real phrase. If a test with a known vector fails, verify the expected value with an independent
  offline tool; never edit the expected value just to make the test pass.
- Never write your own cryptography. Use platform APIs and Wallet Core.

## Basics [P1]
- HTTPS only; cleartext traffic disabled (Android) and ATS enabled (iOS). Timeouts and retry limits on all clients.
- Android: `allowBackup=false` and data-extraction rules excluding everything; not debuggable in release.
- Development uses testnets only. No real mnemonic anywhere in the repo.
- Dependencies: pinned stable versions; minimize dependencies in `domain` and anything touching keys.
- Wallet Core: official `trustwallet/wallet-core` only (no forks), exact pinned version in the version catalog, read the release notes
  (breaking changes) before upgrading. The GitHub Packages token is a classic token with only `read:packages`, stored in the user-level
  `~/.gradle/gradle.properties` (never in the project, never committed, never pasted into chats/AI). Prefer an expiring token and rotate it.
  The Maven repository entry is restricted to group `com.trustwallet`.

## Key storage and lock [P2]
- Android: Android Keystore, hardware-backed (StrongBox when available), key requires user authentication, invalidated on new
  biometric enrollment. iOS: Keychain with `ThisDeviceOnly` accessibility + access control (biometry/passcode), Secure Enclave where
  applicable, excluded from iCloud/backup. Only encrypted blobs are stored; key material stays in secure hardware.
- Mnemonic generation only with a secure RNG (Wallet Core or OS SecureRandom). Never `Random`.
- Keystore key invalidation (biometric changed, lock screen removed, device restored): detect it and show a recovery flow
  (restore with seed). Never crash and never silently reset the wallet. See `docs/wallet-lifecycle.md`.
- Encrypted storage format and DB schema are versioned with migrations (updates must never lose a user's key).
- App lock: biometric + PIN fallback, auto-lock after a configurable time in background, lock on launch.
- Signing requires fresh user authentication.
- `SecureScreen` wrapper (expect/actual) on all sensitive screens: Android `FLAG_SECURE`; iOS hide content in the app switcher
  and react to screen recording.
- Sensitive input fields (seed/passphrase/PIN): disable autofill, suggestions, personalized learning and third-party-keyboard
  data collection where the platform allows; no text prediction.
- Tapjacking: protect confirm/approve buttons against overlays (`filterTouchesWhenObscured` on Android).
- Clipboard: never copy seed/private key. Addresses are fine. If a secret must ever be copied, mark it sensitive and clear it after a
  short timeout.
- Root/jailbreak detection is advisory only (warn). Never rely on it for security.

## Privacy [P2]
- Analytics and crash reports contain no addresses, balances, amounts, tx hashes or IP-linkable wallet data.
- RPC/indexer providers can link IP and addresses: use multiple providers with fallback, avoid logging requests, and keep API keys
  out of the app binary where possible (proxy/backend) [P6 for the proxy].

## Addresses and transactions [P3]
- Validate every address per chain (format and checksum, e.g. EIP-55) before use.
- Show the FULL address on confirmation, highlighting first and last characters (address-poisoning defense). Warn on first-time recipients.
- Confirmation screen shows recipient, amount, network, fee and max fee before signing.
- EVM: include chain ID in signing (replay protection) and verify the RPC's chain ID matches the selected network.
- Never trust RPC/API data blindly: validate ranges (nonce, gas, amounts, decoded data).
- Fees: show estimates, allow override within sane limits, warn on abnormal values.
- Broadcast failures/timeouts must never lead to a blind double-spend: check nonce/pending state before retrying; broadcasting must
  be idempotent; pending transactions survive process death.
- Warn when the pasted address differs from the address the user copied (clipboard hijack).

## Tokens and swap [P4]
- No unlimited token approvals by default; show spender and amount clearly; offer revoke.
- Swaps: conservative default slippage with a visible cap, re-quote before signing, verify router/spender against an allow-list,
  warn on high price impact.
- Scam tokens hidden by default; maintain a trusted token list; handle fee-on-transfer and rebasing tokens carefully.

## Inputs [P5]
- Resolved names (ENS etc.): show the resolved address before sending (homoglyph and look-alike defense).
- Payment URIs/QR (EIP-681, BIP-21) and deep links: strict parsing and validation; never auto-send.
- Chains requiring memo/tag (XRP, TON, Cosmos...): block sending when required fields are missing.

## Integrations and release [P6]
- WalletConnect: show request origin and permissions; decode signing requests (EIP-712, personal_sign); warn on blind signing.
- Gradle dependency verification (`gradle/verification-metadata.xml`); certificate pinning for our own backend (if any).
- Release: R8/minify, signed release pipeline, remote kill switch for swaps/features, threat model, external audit, bug bounty,
  store compliance (Apple/Google policies, local regulations, fee disclosure).
- Do not "temporarily" disable a security check to make something work. Ask instead.

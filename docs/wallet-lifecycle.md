# Wallet lifecycle (mandatory from P2)

## Creating a wallet
- Generate the mnemonic (12 or 24 words) with a secure RNG (Wallet Core / OS SecureRandom).
- Show the mnemonic once on a `SecureScreen`, never copyable by default; no screenshots.
- **Backup verification**: the user must re-enter selected words (e.g. 3 random positions) before the wallet becomes usable for
  funds. Until verified, mark the wallet "backup pending" and warn on receive.
- Optional BIP-39 passphrase: explain clearly that losing it loses the funds; it is never stored.

## Importing
- Import by mnemonic (BIP-39 checksum validated, wordlist validated), by private key (per chain), or watch-only (address only).
- Imported secrets are validated, encrypted into secure storage immediately and wiped from memory/UI state.
- Disable autofill/suggestions on import fields; do not log input.

## Accounts and derivation
- Multiple wallets and multiple accounts per wallet. Standard derivation paths per chain (BIP-44/49/84, SLIP-44 coin types);
  the path used is stored with the account (never guessed again later).
- Account index and chain list are non-secret metadata (SQLDelight); secrets live only in secure storage.

## Storage
- Encrypted blob + metadata, with an explicit `storageVersion` and migrations. Migration is tested with old-format fixtures.
- Secrets are decrypted only to sign/derive, only after fresh authentication, and wiped right after.

## Lock and authentication
- App lock on launch and after a configurable background timeout; biometric + PIN fallback; limited PIN attempts with backoff.
- Fresh authentication to sign, to reveal the seed, and to delete a wallet.

## Key invalidation and recovery
- If the Keystore/Keychain key becomes unusable (biometric change, lock screen removed, device restore): detect, show a clear
  recovery flow (restore with seed phrase), never crash, never silently erase.
- If the user has no backup and the key is lost, say so honestly.

## Deleting / resetting
- Deleting a wallet requires authentication and a warning that funds are lost without a backup.
- Delete removes encrypted blobs, keys in the Keystore/Keychain, and related metadata. Wipe in-memory copies.

## Tests
- Create -> verify backup -> lock -> unlock -> sign flow; import with valid/invalid mnemonic; key-invalidated path; migration from
  an old storage version; delete leaves no residue.

## Storage layout v1 (written by `WalletRepositoryImpl`)
- Wallet id = the wallet's Ethereum address in lowercase (public, deterministic: importing the same phrase twice is detected as a duplicate).
- Secret (encrypted by the platform `SecretStore`): key `wallet_mnemonic.<id>` = recovery phrase.
- Non-secret metadata (`KeyValueStore`): `wallet_ids` = ids joined by `,`; `wallet_address.<id>.<chainId>` = address; `storage_version` = 1.
- Write order: secret first, then metadata; if metadata fails the secret is removed (rollback). Never metadata without secret.
- Changing any key name or format requires a new `storage_version` and a migration with tests (fixtures of the old layout).
- Android secret encryption: AES-256-GCM key in the Android Keystore (StrongBox when available), IV + ciphertext in a private preferences file.
  Gap: the key is not yet bound to user authentication (comes with the app-lock stage).

## Recovery (import) rules
- Input is normalized (trim, lowercase, any whitespace -> single space) and must have 12, 15, 18, 21 or 24 words; then BIP-39 validity (wordlist + checksum) is checked by the platform library.
- Typed phrases live only in composable-local state and a redacted `PhraseInput` intent payload; never in ViewModel state, logs or saved state.
- Recovering a phrase whose wallet already exists fails with a dedicated error (no overwrite, no duplicate).
- Not supported yet: BIP-39 passphrase.

## iOS note
- Keychain items survive app deletion. On first launch after install (no metadata present) purge orphan Keychain secrets, otherwise a reinstall can leave an old secret behind and a new wallet with the same id would collide.

## iOS storage (same layout v1)
- Secret items: Keychain service `org.sajad.shahabzi.mywallet.secrets`, account = key (`wallet_mnemonic.<id>`).
- Metadata items: Keychain service `org.sajad.shahabzi.mywallet.values`, account = key (`wallet_ids`, `wallet_address.<id>.<chainId>`, `storage_version`).
- Kotlin side: `BridgeSecretStore` / `BridgeKeyValueStore` (commonMain, unit-tested with fake bridges) wrap the Swift `KeychainStore`.

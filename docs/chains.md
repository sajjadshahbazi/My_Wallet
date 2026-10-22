# Chain-specific notes (read when touching a chain; phases P3 and P5)

All chain logic sits behind `ChainAdapter` (or small role interfaces) in `domain`, implemented in `data`. Use cases stay chain-agnostic.

## EVM [P3]
- EIP-1559 vs legacy fees; show max fee and priority fee. Chain ID in every signature.
- Nonce: use the pending nonce, track local pending transactions, never reuse a nonce blindly.
- Replace/cancel stuck transactions (same nonce, higher fee). Handle reorgs and confirmation counts.
- ERC-20: `decimals` from the contract/metadata; approvals per `docs/security.md` [P4]; fee-on-transfer tokens.
- Gas estimation can fail or be wrong: add safe margins within limits and surface failures to the user.
- Start on a testnet (e.g. Sepolia).

## Bitcoin [P5]
- UTXO selection, dust limits, change address, fee rate estimation, RBF. Address types (legacy, SegWit, Taproot).
- Amounts in satoshis (int64 range) via our money types.

## Solana [P5]
- Recent blockhash expiry and retry, rent exemption, associated token accounts, compute budget/priority fees.
- Amounts in lamports (uint64 range).

## Chains with memo/tag [P5]
- XRP (destination tag), TON, Cosmos family, Stellar and others: block sending when a required memo/tag is missing or malformed.

## Cross-chain [P4+]
- Aggregator/bridge quotes: validate destination chain/token/address, show time and fees, handle partial/failed legs honestly.

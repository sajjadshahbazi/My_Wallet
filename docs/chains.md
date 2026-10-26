# Chain-specific notes (read when touching a chain; phases P2, P3 and P5)

| Network | Family | Token standard | Phase |
|---|---|---|---|
| Ethereum | EVM | ERC-20 | P3 |
| BNB Smart Chain | EVM (same adapter, different chain ID/RPC/fees) | BEP-20 | P3 |
| TRON | account-based, own address format (Base58, starts with T) | TRC-20 | P5 |
| Solana | account-based | SPL | P5 |
| TON | account-based, wallet contract + memo | Jettons | P5 |
| Bitcoin | UTXO | none | P5 |

Rule for ALL chains: derivation and address expectations in tests come from official/independent test vectors, never from memory
or from the library's own output. A chain is added to `Chain` and the bridge only together with its verified test vectors.

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

## BNB Smart Chain [P3]
- Same EVM logic as Ethereum: different chain ID, RPC and gas price behavior. Never copy-paste the adapter; parametrize it by chain config.
- BEP-20 tokens behave like ERC-20 (decimals can differ per chain for the same token: read decimals per chain).

## TRON [P5]
- Fees are paid in bandwidth/energy (TRX burn if missing); show an estimate. TRC-20 transfers need energy.
- Address is Base58Check starting with `T`; validate checksum. The recipient account may not be activated yet.

## TON [P5]
- Address has several encodings (bounceable/non-bounceable); wallet contract version affects the derived address (verify what the library derives
  and that other wallets show the same address). Transfers have an optional comment/memo; Jettons use a separate jetton wallet per user.
- Handle seqno and message expiry; confirm finality before showing success.

# Numbers and money (critical, all phases)

Crypto amounts have many decimals (18+) and huge integers (uint256, ~78 digits).

## Types
- NEVER use `Int`, `Long`, `Double` or `Float` for token amounts, balances, fees, gas, prices or fiat values.
  Use BigInteger/BigDecimal through our own domain types (library hidden behind them, so it can be swapped).
- Source of truth: `TokenAmount(raw: BigInteger, decimals: Int)`, where `raw` is the smallest unit (wei, lamports, satoshi).
  Convert to a decimal string only for display.
- Fiat/price values: BigDecimal built from strings.
- `Long` is allowed only for non-money values: timestamps, counters, chain IDs (check range), indexes.
- `commonMain` must not use `java.*` or `android.*` (no `java.math.BigInteger`, no `String.format`).

## Size assumptions
- Raw integers can reach uint256. Never assume 64 bits. Decimals can be 0 to 36 or more; never assume at most 18.
  Read `decimals` from token metadata and validate it (reject absurd values).
- No narrowing, ever: no `toLong()`, `toInt()`, `toDouble()`, no `Int`/`Long` intermediates, no `Long`-based formatting on money.
- Per-chain limits: validate against the chain's real range (uint256 on EVM, uint64 on Solana, int64 satoshis on Bitcoin)
  before building or signing. Overflow/underflow must be impossible, not unlikely. Subtraction below zero is an error.
- Arithmetic (add, subtract, multiply, compare, scale) goes through the domain money types, with an explicit rounding mode
  wherever division is involved. Compare with `compareTo`, never by comparing Strings or floats.

## Boundaries (serialization and storage)
- Amounts cross JSON, DB and navigation boundaries only as String (decimal digits).
- JSON: parse numeric fields as strings or with a custom serializer; a value must never pass through `Double`.
  kotlinx.serialization decodes JSON numbers to `Double` by default for floating fields: do not use that for money.
- Database: store raw amounts as `TEXT` (SQLite `INTEGER` is 64-bit and corrupts big values). Sorting/summing is done in code
  on the big-number type, not in SQL.

## Input
- Amount text fields keep the raw typed String in state; cap the length; allow one decimal separator; parse exactly.
  Never round-trip through a number type while typing.
- Reject: negative values, malformed input, more fractional digits than `decimals`.
- Normalize Persian/Arabic-Indic digits (and locale decimal separators) to Latin digits before parsing.
- "Max" button = exact balance minus fee, computed in smallest units.

## Display
- Formatting (3-digit grouping, decimal separator, trimming trailing zeros, abbreviating tiny values like `<0.000001`) lives in
  `tools/format` and works on the big-number types/Strings.
- Very long numbers must not break layouts: ellipsis/scaling, and always let the user see the exact full value
  (e.g. on the confirmation screen).

## Tests (mandatory for every money type/function)
Zero; 1 smallest unit; max uint256 and max uint256 + 1; uint64 limits; decimals 0/6/8/18/24/36; 50+ digit fractions;
leading/trailing zeros; rounding in every division; overflow and underflow; JSON and DB round-trip without loss.

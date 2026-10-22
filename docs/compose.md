# Compose rules

## Screen / Content pattern (mandatory for every screen)
```kotlin
@Composable
fun SendScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<SendViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveEffects(viewModel.effects) { effect -> /* handle UI effects */ }

    SendContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
private fun SendContent(
    state: SendState,
    onIntent: (SendIntent) -> Unit,
    modifier: Modifier = Modifier,
) { /* stateless UI */ }

@Preview
@Composable
private fun SendContentPreview() {
    AppTheme { SendContent(state = SendState(), onIntent = {}) }
}
```
- `XxxScreen` is the ONLY place that knows the ViewModel and Koin. It passes `state` and `viewModel::onIntent` to `XxxContent`.
  The ViewModel is never passed further down.
- Composables never call ViewModel functions directly. Every user action is `onIntent(XxxIntent.Something)`.
- Every `XxxContent` and every reusable component has at least one `@Preview` with fake state: default, plus loading / error /
  filled variants where they differ visually. Previews are `private`, named `XxxContentPreview` / `XxxContentLoadingPreview`,
  wrapped in `AppTheme`, never use Koin or real data.
- [P2] Sensitive screens (unlock, show seed, sign/confirm) are wrapped in the app-lock gate and `SecureScreen`.

## State hoisting
- Business state lives in the ViewModel. Pure UI state (focus, scroll, expanded, cursor) lives in the composable via
  `remember`/`rememberSaveable` or a small `rememberXxxState`.
- Reusable components are stateless (`value` + `onValueChange` style).
- Every composable takes `modifier: Modifier = Modifier` as first optional parameter and applies it to its root.

## Avoid unnecessary recomposition
- Leaf composables receive only the primitives/small immutable objects they need, not the whole state.
- State and UI models are immutable. Use `ImmutableList`/`ImmutableMap` (or `@Immutable`/`@Stable` where needed);
  never expose `List`/`Set`/`Map` from state.
- `LazyColumn`/`LazyRow`: always a stable `key` and a `contentType`.
- Do expensive work (formatting, sorting, filtering, mapping) in the ViewModel/mapper, not in composition.
  If unavoidable inside a composable: `remember(keys)` or `derivedStateOf`.
- Read fast-changing state as late as possible: lambda-based modifiers (`Modifier.offset { }`, `graphicsLayer { }`,
  `drawBehind { }`) and `() -> T` lambdas for animated values.
- Do not allocate objects (formatters, lists, brushes, shapes) in composables without `remember`.
- `collectAsStateWithLifecycle()` for ViewModel flows, never `collectAsState()`.
- Forward `onIntent` rather than creating lambdas that capture the whole ViewModel/state.
- When recomposition behavior is in doubt, say so and propose enabling Compose compiler reports.

## Text and resources
- No hardcoded user-visible strings in composables: use Compose resources (`stringResource`).
- Amounts and addresses are formatted via `tools/format`, never ad-hoc string manipulation.
- [P2] Numbers, addresses and hashes are always displayed LTR even in RTL layouts; amount input accepts
  Persian/Arabic-Indic digits and normalizes them to Latin digits before parsing (see `docs/numbers.md`).

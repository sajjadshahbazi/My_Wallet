package org.sajad.shahabzi.tools.secure

import androidx.compose.runtime.Composable

/**
 * Wraps screens that show secrets (recovery phrase, private keys). On Android it blocks screenshots,
 * screen recording and the recents thumbnail. See docs/security.md.
 */
@Composable
expect fun SecureScreen(content: @Composable () -> Unit)
package org.sajad.shahabzi.tools.secure

import androidx.compose.runtime.Composable

/**
 * NOT secured yet: only renders the content. Release blocker: must hide the content in the
 * app switcher and react to screen capture (see docs/status.md, Known gaps).
 */
@Composable
actual fun SecureScreen(content: @Composable () -> Unit) {
    content()
}
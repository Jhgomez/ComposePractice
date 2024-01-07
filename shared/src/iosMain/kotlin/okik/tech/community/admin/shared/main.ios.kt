package okik.tech.community.admin.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    ComposeUIViewController { Home(modifier = Modifier.fillMaxSize()) }
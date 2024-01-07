package okik.tech.community.admin.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Home(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        Text("Hello world")
    }
}
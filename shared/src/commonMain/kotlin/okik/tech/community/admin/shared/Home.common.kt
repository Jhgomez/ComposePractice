package okik.tech.community.admin.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import okik.tech.community.admin.shared.resources.SharedRes

@Composable
fun Home(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        val app_name: String = stringResource(SharedRes.strings.test)
        val greetingText by remember { mutableStateOf("Hello, World! $app_name") }
        Text(greetingText)
        Image(modifier = Modifier.size(180.dp), painter = painterResource(SharedRes.images.myImage), contentDescription = null)
    }
}
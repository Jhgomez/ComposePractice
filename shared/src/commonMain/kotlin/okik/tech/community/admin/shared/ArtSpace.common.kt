package okik.tech.community.admin.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import okik.tech.community.admin.shared.resources.SharedRes

@Composable
fun ArtSpace() {

}

@Composable
fun ImageCard(
    image: ImageResource,
    modifier: Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        )
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

    }
}
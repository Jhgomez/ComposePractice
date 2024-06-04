package okik.tech.community.admin.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import okik.tech.community.admin.shared.resources.SharedRes

@Composable
fun ArtSpace(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        ImageCard(
            image = SharedRes.images.juan,
        )

        PictureFooter(
            "My first picture with my new born ",
            "Dalia Flores",
            "2009",
            {},
            {}
        )
    }
}

@Composable
fun ImageCard(
    image: ImageResource,
    cornerRadius: Dp = 0.dp,
    modifier: Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        ),
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ColumnScope.PictureFooter(
    title: String,
    artist: String,
    year: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Surface(
        color = Color.LightGray,
        modifier = Modifier
            .weight(.25f, true)
            .padding(24.dp)
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            val annotatedString = buildAnnotatedString {
                this.withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(artist)
                }

                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("($year)")
                }
            }

            Text(
                text = annotatedString,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    Row(
        modifier = Modifier
            .weight(.15f, true)
            .fillMaxWidth()
    ) {
        Button(
            onClick = onPrevious,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Previous"
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Next"
            )
        }
    }
}
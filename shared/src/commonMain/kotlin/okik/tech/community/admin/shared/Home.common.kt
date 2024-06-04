package okik.tech.community.admin.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import okik.tech.community.admin.shared.resources.SharedRes

@Composable
fun Home(modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier,
        color = Color.Green
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val appName: String = stringResource(SharedRes.strings.test)
            val greetingText by remember { mutableStateOf("Hello, xxWorld! $appName") }

            Text(greetingText)

//            Presentation(
//                painter = painterResource(SharedRes.images.android_logo),
//                fullName = stringResource(SharedRes.strings.full_name),
//                title = stringResource(SharedRes.strings.title)
//            )
//
//            ContactInfo(
//                phone = stringResource(SharedRes.strings.phone),
//                socialProfile = stringResource(SharedRes.strings.social_profile),
//                email = stringResource(SharedRes.strings.email)
//            )

            ArtSpace(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun Presentation(
    painter: Painter,
    fullName: String,
    title: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(120.dp).background(Color.Black),
            painter = painter,
            contentDescription = null,
        )

        Text(
            text = fullName,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF3ddc84)
        )
    }
}

@Composable
fun ContactInfo(
    phone: String,
    socialProfile: String,
    email: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.width(200.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = phone, style = MaterialTheme.typography.bodySmall)
        }

        Row(
            modifier = Modifier.width(200.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = socialProfile, style = MaterialTheme.typography.bodySmall)
        }

        Row(
            modifier = Modifier.width(200.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = email, style = MaterialTheme.typography.bodySmall)
        }
    }
}

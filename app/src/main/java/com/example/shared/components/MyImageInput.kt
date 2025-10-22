package com.example.shared.components

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shared.utils.toImageBitmap
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream
import kotlin.io.path.readBytes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyImageInput(modifier: Modifier = Modifier, value: Bitmap?, onChanged: (Bitmap?) -> Unit) {
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), onResult = { uri ->
            val file = createTempFile()
            uri?.let {
                context.contentResolver.openInputStream(it)
            }.use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }

            val b = file.readBytes().toImageBitmap()
            onChanged(b)
        })


    Box(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center

    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .combinedClickable(
                    onClick = { galleryLauncher.launch("image/*") },
                    onLongClick = { onChanged(null) })
                .border(BorderStroke(4.dp, MaterialTheme.colorScheme.primary), CircleShape)
                .padding(4.dp)
                .clip(CircleShape),
        ) {
            value?.let {
                Image(
                    bitmap = it.copy(Bitmap.Config.RGB_565, false).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            } ?: AsyncImage(
                "https://rmtta-website.s3.us-east-2.amazonaws.com/profiles/no-image.png",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
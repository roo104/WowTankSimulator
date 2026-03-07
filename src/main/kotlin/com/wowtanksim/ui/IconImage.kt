package com.wowtanksim.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wowtanksim.util.DebugLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import javax.imageio.ImageIO

private val imageCache = ConcurrentHashMap<String, ImageBitmap?>()

@Composable
fun IconImage(url: String, size: Dp = 28.dp, modifier: Modifier = Modifier) {
    var bitmap by remember(url) { mutableStateOf(imageCache[url]) }
    var loading by remember(url) { mutableStateOf(url.isNotBlank() && !imageCache.containsKey(url)) }

    LaunchedEffect(url) {
        if (url.isBlank()) return@LaunchedEffect
        if (imageCache.containsKey(url)) {
            bitmap = imageCache[url]
            return@LaunchedEffect
        }
        loading = true
        bitmap = withContext(Dispatchers.IO) {
            try {
                DebugLog.info("Loading icon: $url")
                val connection = URI(url).toURL().openConnection()
                connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                connection.connectTimeout = 10_000
                connection.readTimeout = 10_000
                val stream = connection.getInputStream()
                val img = ImageIO.read(stream)
                stream.close()
                if (img == null) {
                    DebugLog.error("Icon loaded but ImageIO returned null: $url")
                    null
                } else {
                    DebugLog.info("Icon loaded OK: ${img.width}x${img.height} $url")
                    img.toComposeImageBitmap()
                }
            } catch (e: Exception) {
                DebugLog.error("Icon load failed: $url", e)
                null
            }
        }
        imageCache[url] = bitmap
        loading = false
    }

    val shape = RoundedCornerShape(4.dp)
    if (bitmap != null) {
        Image(
            bitmap = bitmap!!,
            contentDescription = null,
            modifier = modifier.size(size).clip(shape),
            contentScale = ContentScale.Crop,
        )
    } else {
        Box(modifier.size(size).clip(shape).background(Color(0xFF2A2A2A)))
    }
}

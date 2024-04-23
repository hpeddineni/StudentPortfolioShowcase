package uk.ac.tees.mad.w9643793.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun ImageViewerCard(images: List<Painter>) {
    for (image in images) {
        Image(
            painter = image,
            contentDescription = "student uploaded images"
        )
    }
}
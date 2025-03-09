package nl.marc_apps.ivycameracontrol.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RecordingIndicator(isRecording: Boolean, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box (
            modifier = Modifier
                .size(12.dp)
                .background(if (isRecording) Color.Red else Color.Gray, CircleShape)
        )

        Text("REC", modifier = Modifier.alpha(if (isRecording) 1f else 0.38f))
    }
}
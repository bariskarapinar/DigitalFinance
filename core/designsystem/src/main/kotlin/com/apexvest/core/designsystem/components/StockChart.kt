package com.apexvest.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.apexvest.core.designsystem.*

@Composable
fun StockChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    isPositive: Boolean = true
) {
    val color = if (isPositive) BullishGreen else BearishRed
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        if (data.size < 2) return@Canvas

        val max = data.maxOrNull() ?: 1.0
        val min = data.minOrNull() ?: 0.0
        val range = (max - min).coerceAtLeast(1.0)
        val width = size.width
        val height = size.height
        val stepX = width / (data.size - 1)

        val path = Path().apply {
            data.forEachIndexed { index, value ->
                val x = index * stepX
                val y = height - ((value - min) / range * height).toFloat()
                if (index == 0) moveTo(x, y) else lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3.dp.toPx())
        )

        // Glow effect
        drawPath(
            path = path,
            color = color.copy(alpha = 0.3f),
            style = Stroke(width = 8.dp.toPx())
        )

        // Fill area under the curve
        val fillPath = Path().apply {
            addPath(path)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(color.copy(alpha = 0.2f), Color.Transparent)
            )
        )
    }
}

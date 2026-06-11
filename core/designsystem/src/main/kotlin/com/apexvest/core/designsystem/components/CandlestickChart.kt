package com.apexvest.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.apexvest.core.designsystem.BullishGreen
import com.apexvest.core.designsystem.BearishRed

data class CandleData(
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float,
    val timestamp: Long
)

/**
 * ApexVest Interactive Candlestick Engine.
 * Built on Jetpack Compose Canvas with hardware-accelerated layering.
 */
@Composable
fun CandlestickChart(
    data: List<CandleData>,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                // ApexVest Optimization: Defer visual transformations to Hardware Layer
                scaleX = scale
                scaleY = 1f
                translationX = offset.x
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    offset += pan
                }
            }
    ) {
        if (data.isEmpty()) return@Canvas

        val candleWidth = 20.dp.toPx()
        val spacing = 10.dp.toPx()
        
        val maxPrice = data.maxOf { it.high }
        val minPrice = data.minOf { it.low }
        val range = maxOf(maxPrice - minPrice, 1f)
        
        data.forEachIndexed { index, candle ->
            val x = index * (candleWidth + spacing)
            val isBullish = candle.close >= candle.open
            val color = if (isBullish) BullishGreen else BearishRed
            
            // Draw wick
            drawLine(
                color = color,
                start = Offset(x + candleWidth / 2, size.height - ((candle.high - minPrice) / range * size.height)),
                end = Offset(x + candleWidth / 2, size.height - ((candle.low - minPrice) / range * size.height)),
                strokeWidth = 2f
            )
            
            // Draw body
            val top = size.height - ((maxOf(candle.open, candle.close) - minPrice) / range * size.height)
            val bottom = size.height - ((minOf(candle.open, candle.close) - minPrice) / range * size.height)
            
            drawRect(
                color = color,
                topLeft = Offset(x, top),
                size = Size(candleWidth, maxOf(bottom - top, 1f))
            )
        }
    }
}

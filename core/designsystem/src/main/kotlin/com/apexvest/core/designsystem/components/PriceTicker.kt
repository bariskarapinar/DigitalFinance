package com.apexvest.core.designsystem.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.apexvest.core.designsystem.BullishGreen
import com.apexvest.core.designsystem.BearishRed
import java.util.Locale

@Composable
fun PriceTicker(
    price: Double,
    modifier: Modifier = Modifier,
    currencySymbol: String = "$"
) {
    var previousPrice by remember { mutableDoubleStateOf(price) }
    val color by animateColorAsState(
        targetValue = when {
            price > previousPrice -> BullishGreen
            price < previousPrice -> BearishRed
            else -> Color.White
        },
        label = "price_color"
    )

    SideEffect {
        previousPrice = price
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currencySymbol,
            style = MaterialTheme.typography.bodyLarge,
            color = color
        )
        AnimatedContent(
            targetState = price,
            modifier = modifier.graphicsLayer {
                // ApexVest Optimization: Defer visual properties to Draw/Layout phase
                shadowElevation = 2f
            },
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { -it } + fadeOut()
                } else {
                    slideInVertically { -it } + fadeIn() togetherWith
                            slideOutVertically { it } + fadeOut()
                }.using(SizeTransform(clip = false))
            },
            label = "price_animation"
        ) { targetPrice ->
            Text(
                text = String.format(Locale.US, "%.2f", targetPrice),
                style = MaterialTheme.typography.bodyLarge,
                color = color
            )
        }
    }
}

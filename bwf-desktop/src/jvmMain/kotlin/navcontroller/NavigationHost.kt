package navcontroller

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * NavigationHost class
 */
class NavigationHost(
    private val window: ComposeWindow,
    val navController: NavController,
    val contents: @Composable NavigationGraphBuilder.() -> Unit
) {

    @Composable
    fun build() {
        NavigationGraphBuilder(window).renderContents()
    }

    inner class NavigationGraphBuilder(
        val window: ComposeWindow,
        val navController: NavController = this@NavigationHost.navController
    ) {
        @Composable
        fun renderContents() {
            this@NavigationHost.contents(this)
        }
    }
}


/**
 * Composable to build the Navigation Host
 */
@Composable
fun NavigationHost.NavigationGraphBuilder.composable(
    route: String,
    content: @Composable () -> Unit
) {
    val rightRoute = navController.currentScreen.value == route
    val transition = updateTransition(rightRoute, label = "screen_transition")
    val screenAlpha by transition.animateFloat(transitionSpec = { tween(durationMillis = 150, easing = LinearOutSlowInEasing) }) { if (rightRoute) 1f else 0f }
    if (rightRoute) {
        Box(
            Modifier.fillMaxSize().alpha(screenAlpha)
                .offset { IntOffset((window.width * (1 - screenAlpha)).roundToInt(), 0) }) {
            content()
        }
    }
}
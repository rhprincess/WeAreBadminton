package navcontroller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * NavController Class
 *
 * this generic T is used to define what type of the ScreenBundle is.
 * you can use navigate(route: String, bundle: MutableState<T>) and getCurrentBundle function to pass data through screens
 */
class NavController(
    private val startDestination: String,
    private var backStackScreens: MutableSet<String> = mutableSetOf()
) {
    // Variable to store the state of the current screen
    var currentScreen: MutableState<String> = mutableStateOf(startDestination)
    var previousScreen: MutableState<String> = mutableStateOf(startDestination)
    var screenBundle = ScreenBundle()

    class ScreenBundle {
        val strings: HashMap<String, String> = hashMapOf()
        val booleans: HashMap<String, Boolean> = hashMapOf()
        val ints: HashMap<String, Int> = hashMapOf()
        val floats: HashMap<String, Float> = hashMapOf()
        val longs: HashMap<String, Long> = hashMapOf()
        val doubles: HashMap<String, Double> = hashMapOf()
    }

    // Function to handle the navigation between the screen
    fun navigate(route: String) {
        if (route != currentScreen.value) {

            if (backStackScreens.contains(currentScreen.value) && currentScreen.value != startDestination) {
                backStackScreens.remove(currentScreen.value)
            }

            if (route == startDestination) {
                backStackScreens = mutableSetOf()
            } else {
                backStackScreens.add(currentScreen.value)
                previousScreen.value = currentScreen.value
            }

            // switch to current screen
            currentScreen.value = route
        }
    }

    fun navigate(route: String, bundle: ScreenBundle) {
        this.screenBundle = bundle
        navigate(route)
    }

    // Function to handle the back
    fun navigateBack() {
        if (backStackScreens.isNotEmpty()) {
            currentScreen.value = backStackScreens.last()
            backStackScreens.remove(currentScreen.value)
        }
    }

    fun navigateBack(bundle: ScreenBundle) {
        this.screenBundle = bundle
        navigateBack()
    }
}


/**
 * Composable to remember the state of the navcontroller
 */
@Composable
fun rememberNavController(
    startDestination: String,
    backStackScreens: MutableSet<String> = mutableSetOf()
): MutableState<NavController> = rememberSaveable {
    mutableStateOf(NavController(startDestination, backStackScreens))
}
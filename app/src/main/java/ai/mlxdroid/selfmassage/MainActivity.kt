package ai.mlxdroid.selfmassage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ai.mlxdroid.selfmassage.navigation.AppNavGraph
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SelfMassageTheme {
                AppNavGraph()
            }
        }
    }
}

package com.apexvest

import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.apexvest.core.designsystem.FinanceTheme
import com.apexvest.feature.fraud.FraudViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val fraudViewModel: FraudViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ApexVest Production Security: Memory & View Protection
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        
        enableEdgeToEdge()
        setContent {
            FinanceTheme {
                MainScreen()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // ApexVest Behavioral Telemetry: Anonymously profile user touch dynamics
        fraudViewModel.onUserTouch(ev.x, ev.y, ev.pressure)
        return super.dispatchTouchEvent(ev)
    }
}

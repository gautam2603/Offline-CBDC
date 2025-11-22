package com.zkk.offlinecbdc.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zkk.offlinecbdc.ui.NavRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(nav: NavHostController) {
    val context = LocalContext.current

    // scale animation
    val scale = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing))
        delay(900) // keep splash visible briefly
        // navigate to home and remove splash from backstack
        nav.navigate(NavRoutes.HOME) {
            popUpTo(NavRoutes.SPLASH) { inclusive = true }
        }
    }

    // safely resolve resource id for ic_logo drawable (returns 0 if not found)
    val logoResId = remember {
        context.resources.getIdentifier("ic_logo", "drawable", context.packageName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF0F4C81), Color(0xFF1E88E5)))
            ),
        contentAlignment = Alignment.Center
    ) {
        if (logoResId != 0) {
            // only call painterResource when resource exists
            val painter = painterResource(id = logoResId)
            Image(
                painter = painter,
                contentDescription = "App logo",
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale.value)
            )
        } else {
            // fallback UI — no crash
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Offline CBDC",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Prototype",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

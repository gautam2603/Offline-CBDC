package com.zkk.offlinecbdc.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zkk.offlinecbdc.ui.components.SuccessDialog
import com.zkk.offlinecbdc.viewmodel.TokenViewModel

/**
 * FINAL Scan & Pay screen (simulate paying someone)
 */
@Composable
fun ScanScreen(nav: NavHostController, vm: TokenViewModel) {

    var scanned by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf(0) }
    var lastName by remember { mutableStateOf("") }
    var lastPhone by remember { mutableStateOf("") }
    var noTokens by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable {

                val payAmount = 40
                amount = payAmount

                // spend a token
                val packet = vm.spendToken(payAmount)

                if (packet == null) {
                    // no tokens available
                    noTokens = true
                    scanned = true
                    showDialog = true
                    return@clickable
                }

                // fetch latest transaction dummy name + phone
                vm.transactions.lastOrNull()?.let {
                    lastName = it.name ?: "Unknown"
                    lastPhone = it.phone ?: ""
                }

                scanned = true
                showDialog = true
            },
        contentAlignment = Alignment.Center
    ) {

        // camera frame
        Card(
            modifier = Modifier
                .size(300.dp, 380.dp)
                .border(3.dp, Color.White, RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(Color.Transparent),
            shape = RoundedCornerShape(18.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                AnimatedScanLine()

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tap anywhere to simulate payment",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // top banner
        if (scanned) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (noTokens) {
                    Text(
                        "No tokens available",
                        color = Color.Yellow,
                        fontSize = 16.sp
                    )
                } else {
                    Text(
                        text = "Paid -₹$amount to $lastName",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Phone: $lastPhone",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { nav.popBackStack() }) {
                    Text("Done")
                }
            }
        }

        // popup dialog
        if (showDialog) {
            if (noTokens) {
                SuccessDialog("Payment failed — no tokens available") {
                    showDialog = false
                    nav.popBackStack()
                }
            } else {
                SuccessDialog("Payment Successful — Paid ₹$amount to $lastName") {
                    showDialog = false
                    nav.popBackStack()
                }
            }
        }
    }
}

/** CLEAN single scan line animation */
@Composable
fun AnimatedScanLine() {

    val infiniteTransition = rememberInfiniteTransition()

    val progress by infiniteTransition.animateFloat(
        0f,
        1f,
        animationSpec = infiniteRepeatable(
            tween(1600, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    val offsetY = (progress * 260f) - 130f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset(y = offsetY.dp)
                .height(6.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF00E5FF), Color(0xFF2979FF))
                    ),
                    alpha = 0.9f
                )
        )
    }
}

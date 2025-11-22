package com.zkk.offlinecbdc.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zkk.offlinecbdc.viewmodel.TokenViewModel
import kotlinx.coroutines.delay

@Composable
fun PayScreen(nav: NavHostController, vm: TokenViewModel) {

    var stage by remember { mutableStateOf(0) }
    var selectedDevice by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }

    when (stage) {

        // device list
        0 -> DeviceListScreen(
            onSelect = {
                selectedDevice = it
                stage = 1
            },
            nav = nav
        )

        // connecting animation
        1 -> ConnectingScreen(
            device = selectedDevice,
            onConnected = { stage = 2 }
        )

        // combined amount + pin
        2 -> AmountAndPinScreen(
            amount = amount,
            onAmountChange = { amount = it.filter { ch -> ch.isDigit() } }, // digits only
            pin = pin,
            onDigit = { if (pin.length < 4) pin += it },
            onDelete = { pin = pin.dropLast(1) },
            onSubmit = {
                // safe conversion, guard against invalid input
                val amt = amount.toIntOrNull()
                if (amt != null && amt > 0 && pin.length == 4) {
                    vm.spendToken(amt)
                    // reset pin (optional)
                    pin = ""
                    stage = 3
                } else {
                    // invalid, you can show a snackbar/toast later
                }
            },
            onBack = { stage = 0 }
        )

        // success
        3 -> PaymentSuccessScreen(amount = amount, nav = nav)
    }
}

/* -------------------------------------------------------- */
/* DEVICE LIST — POLISHED */
/* -------------------------------------------------------- */
@Composable
fun DeviceListScreen(onSelect: (String) -> Unit, nav: NavHostController) {

    val devices = listOf(
        "Vivo T19",
        "OnePlus Nord",
        "Samsung A52",
        "Merchant POS #04"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Nearby Devices",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        devices.forEach { name ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onSelect(name) },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F7)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                // Column centers device name and pins "Tap to connect" near the bottom within the card
                Column(
                    modifier = Modifier
                        .padding(18.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Bluetooth,
                            contentDescription = null,
                            tint = Color(0xFF2979FF),
                            modifier = Modifier.size(42.dp)
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            name,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // pinned at bottom of card
                    Text(
                        "Tap to connect",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(36.dp))

        Button(onClick = { nav.popBackStack() }) {
            Text("Back")
        }
    }
}

/* -------------------------------------------------------- */
/* CONNECTING — WITH ANIMATION */
/* -------------------------------------------------------- */
@Composable
fun ConnectingScreen(device: String, onConnected: () -> Unit) {

    val pulse = rememberInfiniteTransition()
    val scale by pulse.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            tween(600, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )

    LaunchedEffect(device) {
        // short simulated connection delay
        delay(1400)
        onConnected()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.92f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color(0xFF2979FF).copy(alpha = 0.18f),
                    shape = CircleShape
                )
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF2979FF),
                strokeWidth = 5.dp
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            "Connecting to $device…",
            color = Color.White,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Secure channel • AES-256 handshake",
            color = Color.LightGray,
            fontSize = 13.sp
        )
    }
}

/* -------------------------------------------------------- */
/* AMOUNT + PIN — SAME SCREEN (single canonical implementation) */
/* -------------------------------------------------------- */
@Composable
fun AmountAndPinScreen(
    amount: String,
    onAmountChange: (String) -> Unit,
    pin: String,
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Top: amount field
        Text("Enter Amount", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text("₹ Amount") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // PIN label + display
        Text("Enter PIN", fontSize = 20.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            text = pin.padEnd(4, '•'),
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )

        // push keypad to bottom
        Spacer(modifier = Modifier.weight(1f))

        NumericPinPad(
            onDigit = onDigit,
            onDelete = onDelete,
            onSubmit = onSubmit,
            isSubmitEnabled = (pin.length == 4 && amount.isNotEmpty())
        )

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun NumericPinPad(
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit,
    isSubmitEnabled: Boolean
) {
    val nums = listOf("1","2","3","4","5","6","7","8","9","0")

    nums.chunked(3).forEach { row ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            row.forEach { digit ->
                PinButton(digit) { onDigit(digit) }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(onClick = onDelete) { Text("Delete") }

        Button(
            onClick = onSubmit,
            enabled = isSubmitEnabled
        ) {
            Text("Confirm")
        }
    }
}

@Composable
fun PinButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(72.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3E4A80)
        )
    ) {
        Text(label, color = Color.White, fontSize = 20.sp)
    }
}

/* -------------------------------------------------------- */
/* PAYMENT SUCCESS SCREEN (implemented) */
/* -------------------------------------------------------- */
@Composable
fun PaymentSuccessScreen(amount: String, nav: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF00C853),
            modifier = Modifier.size(120.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            "₹$amount Sent Successfully",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Payment completed securely over Bluetooth",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(30.dp))

        Button(onClick = { nav.popBackStack() }) {
            Text("Back to Home")
        }
    }
}

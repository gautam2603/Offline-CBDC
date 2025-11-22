package com.zkk.offlinecbdc.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zkk.offlinecbdc.viewmodel.TokenViewModel
import kotlinx.coroutines.delay

@Composable
fun IssueNotesScreen(nav: NavHostController, vm: TokenViewModel) {

    var stage by remember { mutableStateOf(0) }
    var amount by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var issuedTokens by remember { mutableStateOf(listOf<com.zkk.offlinecbdc.data.models.Token>()) }

    when (stage) {

        /* --------------------------------------------------------
           STEP 1 → ENTER AMOUNT
        -------------------------------------------------------- */
        0 -> AmountEntryScreen(
            amount = amount,
            onChange = { amount = it },
            onNext = { stage = 1 },
            onBack = { nav.popBackStack() }
        )

        /* --------------------------------------------------------
           STEP 2 → PIN ENTRY
        -------------------------------------------------------- */
        1 -> PinEntryScreen(
            pin = pin,
            onDigit = { if (pin.length < 4) pin += it },
            onDelete = { pin = pin.dropLast(1) },
            onSubmit = {
                stage = 2
            },
            onBack = { stage = 0 }
        )

        /* --------------------------------------------------------
           STEP 3 → ANIMATION (processing)
        -------------------------------------------------------- */
        2 -> ProcessingAnimation(
            amount = amount,
            onDone = {
                issuedTokens = vm.issueAmount(amount.toInt())
                stage = 3
            }
        )

        /* --------------------------------------------------------
           STEP 4 → RECEIPT
        -------------------------------------------------------- */
        3 -> ReceiptScreen(
            amount = amount,
            tokens = issuedTokens,
            onBack = { nav.popBackStack() }
        )
    }
}

/* ================================================================
   1. AMOUNT ENTRY
================================================================ */

@Composable
fun AmountEntryScreen(
    amount: String,
    onChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            Spacer(Modifier.width(8.dp))
            Text("Bank Issuance", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(40.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = onChange,
            label = { Text("Enter Amount") },
            singleLine = true
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onNext,
            enabled = amount.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

/* ================================================================
   2. PIN ENTRY
================================================================ */

@Composable
fun PinEntryScreen(
    pin: String,
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            Spacer(Modifier.width(8.dp))
            Text("Enter PIN", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(40.dp))

        Text(pin.padEnd(4, '•'), fontSize = 42.sp, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(30.dp))

        val nums = listOf("1","2","3","4","5","6","7","8","9","0")

        nums.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { digit ->
                    Button(
                        onClick = { onDigit(digit) },
                        modifier = Modifier.size(70.dp),
                        shape = CircleShape
                    ) {
                        Text(digit, fontSize = 20.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = onDelete) { Text("Delete") }

            Button(
                onClick = onSubmit,
                enabled = pin.length == 4
            ) {
                Text("Confirm")
            }
        }
    }
}

/* ================================================================
   3. PROCESSING ANIMATION
================================================================ */

@Composable
fun ProcessingAnimation(amount: String, onDone: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(1200)
        onDone()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            color = Color(0xFF1E88E5),
            strokeWidth = 6.dp,
            modifier = Modifier.size(60.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            "Issuing ₹$amount...",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

/* ================================================================
   4. RECEIPT
================================================================ */

@Composable
fun ReceiptScreen(
    amount: String,
    tokens: List<com.zkk.offlinecbdc.data.models.Token>,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF00C853),
            modifier = Modifier.size(90.dp)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Tokens Issued Successfully",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E88E5)
        )

        Spacer(Modifier.height(10.dp))

        Text("Amount: ₹$amount", fontSize = 16.sp, color = Color.Gray)

        Spacer(Modifier.height(20.dp))

        Text("Issued Notes:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(12.dp))

        /* List of issued notes */
        tokens.forEach { token ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text("₹${token.amount}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Token ID:", fontSize = 12.sp, color = Color.Gray)
                    Text(token.tokenId, fontSize = 12.sp)
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Back to Home")
        }
    }
}

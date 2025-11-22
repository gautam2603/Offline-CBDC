package com.zkk.offlinecbdc.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zkk.offlinecbdc.data.models.TransactionItem
import com.zkk.offlinecbdc.viewmodel.TokenViewModel
import com.zkk.offlinecbdc.ui.NavRoutes
import androidx.compose.material3.Divider

@Composable
fun HomeScreen(nav: NavHostController, vm: TokenViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        /* -------------------------------------------------------------
           HEADER — ZK SETU in India Tricolor Gradient
        -------------------------------------------------------------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFF9933),
                            Color.White,
                            Color(0xFF138808)
                        )
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "OFFLINE CBDC",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            "Powered by Offline CBDC • RBI Sandbox Prototype",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(22.dp))

        /* -------------------------------------------------------------
           ACTION BUTTONS
        -------------------------------------------------------------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ActionIcon(Icons.Default.Bluetooth, "Bluetooth Pay") {
                nav.navigate(NavRoutes.PAY)
            }
            ActionIcon(Icons.Default.QrCode, "Scan & Pay") {
                nav.navigate(NavRoutes.SCAN)
            }
            ActionIcon(Icons.Default.Wallet, "Issue Notes") {
                nav.navigate(NavRoutes.ISSUE)
            }
            ActionIcon(Icons.Default.Money, "Balance") {
                nav.navigate(NavRoutes.BALANCE)
            }
        }

        Spacer(Modifier.height(28.dp))

        /* -------------------------------------------------------------
           TITLE: RECENT TRANSACTIONS
        -------------------------------------------------------------- */
        Text(
            "Recent Transactions",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Divider(
            color = Color.LightGray.copy(alpha = 0.4f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        /* -------------------------------------------------------------
           RECENT TRANSACTIONS LIST
        -------------------------------------------------------------- */
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(vm.transactions) { index, item ->
                AnimatedTransactionRow(item, index)
            }
        }

        Spacer(Modifier.height(16.dp))

        /* -------------------------------------------------------------
           FOOTER
        -------------------------------------------------------------- */
        Text(
            "Offline • Secure • Hardware-Backed CBDC",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 6.dp)
        )
    }
}

/* -------------------------------------------------------------
   ACTION ICON BUTTONS
-------------------------------------------------------------- */
@Composable
fun ActionIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .size(65.dp)
                .background(Color(0xFFE8E8E8), shape = CircleShape)
                .clickable { onClick() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black
            )
        }

        Spacer(Modifier.height(6.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Black)
    }
}

/* -------------------------------------------------------------
   TRANSACTION ROW CARD
-------------------------------------------------------------- */
@Composable
fun TransactionRow(
    name: String,
    phone: String,
    amount: Int,
    type: String
) {
    val isCredit = type == "credit" || type == "bank_credit"
    val color = if (isCredit) Color(0xFF0A9B40) else Color(0xFFD32F2F)
    val prefix = if (isCredit) "+" else "-"

    val displayName =
        if (type == "bank_credit") "RBI Sandbox Node"
        else name

    val displayPhone =
        if (type == "bank_credit") "CBDC Issuance"
        else phone

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(displayPhone, fontSize = 12.sp, color = Color.Gray)
            }

            Text(
                text = "$prefix₹${kotlin.math.abs(amount)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

/* -------------------------------------------------------------
   ANIMATED TRANSACTION ENTRY
-------------------------------------------------------------- */
@Composable
fun AnimatedTransactionRow(item: TransactionItem, index: Int) {
    val delay = 80 * index

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 300, delayMillis = delay)
        ) + slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(durationMillis = 300, delayMillis = delay)
        )
    ) {
        TransactionRow(
            name = item.name,
            phone = item.phone,
            amount = item.amount,
            type = item.type
        )
    }
}

package com.zkk.offlinecbdc.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zkk.offlinecbdc.viewmodel.TokenViewModel

@Composable
fun BalanceScreen(nav: NavHostController, vm: TokenViewModel) {

    val visible = remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible.value) 1f else 0f,
        animationSpec = tween(600)
    )

    val offset by animateDpAsState(
        targetValue = if (visible.value) 0.dp else 20.dp,
        animationSpec = tween(600)
    )

    LaunchedEffect(Unit) {
        visible.value = true
    }

    val clipboard = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {

        // HEADER - TRICOLOR WITH ZK SETU
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFF9933), // saffron
                            Color.White,
                            Color(0xFF138808) // green
                        )
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "OFFLINE CBDC",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(Modifier.height(20.dp))

        // USER PROFILE CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = offset)
                .alpha(alpha),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(Modifier.padding(20.dp)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE3EFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "GS",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(
                            "Gautam Soni",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text("User ID: ZKK123", fontSize = 13.sp)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Copy", fontSize = 12.sp, color = Color.Gray)
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        clipboard.setText(
                                            androidx.compose.ui.text.AnnotatedString("ZKK123")
                                        )
                                    }
                            )
                        }

                        Spacer(Modifier.height(6.dp))
                        Text("Email: gs.cbdc@zero.gov.in", fontSize = 13.sp)
                        Text("Phone: +91 •••• ••9813", fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(14.dp))

                Divider()

                Spacer(Modifier.height(12.dp))

                Text("KYC Status: Verified", color = Color(0xFF0A9B40), fontSize = 14.sp)
                Spacer(Modifier.height(4.dp))

                Text("Issued Tokens: ${vm.tokens.size}", fontSize = 13.sp)
                Text("Last Sync: Today, 6:22 PM", fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        // BALANCE CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5)),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Available Balance", fontSize = 14.sp, color = Color.White.copy(0.9f))
                Text(
                    "₹ ${vm.getBalance()}",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(26.dp))

        Text("Token Summary", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(10.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryCard("Total", vm.tokens.size.toString(), Color(0xFF3949AB))
            SummaryCard("Unspent", vm.tokens.count { !it.spent }.toString(), Color(0xFF00897B))
            SummaryCard("Spent", vm.tokens.count { it.spent }.toString(), Color(0xFFD32F2F))
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(90.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White.copy(0.9f), fontSize = 13.sp)
            Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

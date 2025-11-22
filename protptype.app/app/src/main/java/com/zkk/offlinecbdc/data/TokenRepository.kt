package com.zkk.offlinecbdc.data

import androidx.compose.runtime.mutableStateListOf
import com.zkk.offlinecbdc.data.models.Token
import com.zkk.offlinecbdc.data.models.TransactionItem
import com.zkk.offlinecbdc.data.models.TransferPacket
import java.util.UUID

class TokenRepository {

    private val sampleNames = listOf(
        "Riya Sharma", "Aman Verma", "Neha Patel", "Karan Mehta",
        "Priya Singh", "Rohit Chouksey", "Aditi Rao", "Abhishek Gupta",
        "Sneha Rathore", "Vikas Yadav"
    )

    private fun randomName() = sampleNames.random()
    private fun randomPhone() = (6000000000..9999999999).random().toString()

    val tokens = mutableStateListOf<Token>()
    val transactions = mutableStateListOf<TransactionItem>()

    fun getBalance(): Int =
        tokens.filter { !it.spent }.sumOf { it.amount }


    // --------------------------------------------------------------
    // ISSUE NOTES — denomination aware (100 / 50 / 10 INR)
    // --------------------------------------------------------------
    fun issueDenominated(amount: Int): List<Token> {

        val result = mutableListOf<Token>()
        var remaining = amount

        val denominations = listOf(100, 50, 10)

        for (d in denominations) {
            while (remaining >= d) {

                val t = Token(
                    tokenId = UUID.randomUUID().toString(),
                    amount = d,
                    issuedAt = System.currentTimeMillis(),
                    signature = "bank_mock_signature"
                )

                result.add(t)
                remaining -= d
            }
        }

        // Add all created tokens to wallet
        tokens.addAll(result)

        // --------------------------
        // BANK LOG ENTRY
        // --------------------------
        if (result.isNotEmpty()) {

            val totalIssued = result.sumOf { it.amount }

            transactions.add(
                TransactionItem(
                    id = UUID.randomUUID().toString(),
                    name = "RBI Sandbox Node",
                    phone = "CBDC Issuance",
                    amount = totalIssued,        // positive credit
                    timestamp = System.currentTimeMillis(),
                    type = "bank_credit"
                )
            )
        }

        return result
    }


    // --------------------------------------------------------------
    // SPEND TOKEN
    // --------------------------------------------------------------
    fun spendToken(amount: Int): TransferPacket? {

        val token = tokens.firstOrNull { !it.spent } ?: return null

        token.spent = true

        transactions.add(
            TransactionItem(
                id = UUID.randomUUID().toString(),
                name = randomName(),
                phone = randomPhone(),
                amount = -amount,
                timestamp = System.currentTimeMillis(),
                type = "debit"
            )
        )

        return TransferPacket(
            tokenId = token.tokenId,
            amount = amount,
            senderSignature = "mock_sender_signature",
            timestamp = System.currentTimeMillis()
        )
    }


    // --------------------------------------------------------------
    // RECEIVE TOKEN
    // --------------------------------------------------------------
    fun receiveToken(packet: TransferPacket) {

        val newToken = Token(
            tokenId = packet.tokenId,
            amount = packet.amount,
            issuedAt = packet.timestamp,
            signature = "received_mock_signature"
        )

        tokens.add(newToken)

        transactions.add(
            TransactionItem(
                id = UUID.randomUUID().toString(),
                name = randomName(),
                phone = randomPhone(),
                amount = packet.amount,
                timestamp = System.currentTimeMillis(),
                type = "credit"
            )
        )
    }
}

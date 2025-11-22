//transferpacket
package com.zkk.offlinecbdc.data.models

data class TransferPacket(
    val tokenId: String,
    val amount: Int,
    val senderSignature: String,
    val timestamp: Long
)

//tokens
package com.zkk.offlinecbdc.data.models

data class Token(
    val tokenId: String,
    val amount: Int,
    val issuedAt: Long,
    val signature: String,
    var spent: Boolean = false
)

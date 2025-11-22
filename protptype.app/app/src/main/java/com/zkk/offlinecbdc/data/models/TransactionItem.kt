//tokenactionitem
package com.zkk.offlinecbdc.data.models

data class TransactionItem(
    val id: String,
    val name: String,
    val phone: String,
    val amount: Int,
    val timestamp: Long,
    val type: String // "credit" or "debit"
)

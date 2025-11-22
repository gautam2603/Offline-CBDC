//tokenviewmodel
package com.zkk.offlinecbdc.viewmodel
import androidx.lifecycle.ViewModel
import com.zkk.offlinecbdc.data.TokenRepository
import com.zkk.offlinecbdc.data.models.Token
import com.zkk.offlinecbdc.data.models.TransferPacket

class TokenViewModel : ViewModel() {

    private val repo = TokenRepository()

    val tokens = repo.tokens
    val transactions = repo.transactions

    fun issueAmount(amount: Int): List<Token> {
        return repo.issueDenominated(amount)
    }

    fun spendToken(amount: Int) = repo.spendToken(amount)
    fun receive(packet: TransferPacket) = repo.receiveToken(packet)
    fun getBalance() = repo.getBalance()
}


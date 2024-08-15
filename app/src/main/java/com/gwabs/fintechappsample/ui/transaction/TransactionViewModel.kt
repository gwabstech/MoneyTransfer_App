package com.gwabs.fintechappsample.ui.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gwabs.fintechappsample.data.local.AccountDao
import com.gwabs.fintechappsample.data.local.TransactionDao
import com.gwabs.fintechappsample.data.model.Account
import com.gwabs.fintechappsample.data.model.AccountUiState
import com.gwabs.fintechappsample.data.model.Transaction
import com.gwabs.fintechappsample.data.model.TransactionUiState
import com.gwabs.fintechappsample.util.mockUserAccounts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionDao: TransactionDao, private val accountDao: AccountDao): ViewModel() {

    private val _transactionState = MutableStateFlow<TransactionUiState>(TransactionUiState.Loading)
    val transactionState: StateFlow<TransactionUiState> = _transactionState.asStateFlow()

    private val _accountState = MutableStateFlow<AccountUiState>(AccountUiState.Loading)
    val accountState: StateFlow<AccountUiState> = _accountState.asStateFlow()

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    init {
        addAccounts(mockUserAccounts)
        loadAccounts()
        loadTransactions()
    }



    fun getAccountById(accountId: Int, onResult: (Account?) -> Unit) {
        viewModelScope.launch {
            try {
                accountDao.getAccountById(accountId).collect { account ->
                    onResult(account)
                }
            } catch (e: Exception) {
                Log.e("TransactionViewModel", "Error fetching account by ID: ${e.message}")
                onResult(null)
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _transactionState.value = TransactionUiState.Loading
            try {
                transactionDao.getAllTransactions().collect { transactions ->
                    if (transactions.isEmpty()) {
                        _transactionState.value = TransactionUiState.Error("No transactions have been recorded yet. Please check back later.")
                    } else {
                        _transactionState.value = TransactionUiState.Success(transactions)
                    }
                }
            } catch (e: Exception) {
                _transactionState.value = TransactionUiState.Error(e.message ?: "Unknown error")
            }
        }
    }




  fun addTransaction(transaction: Transaction) {
        _transactionState.value = TransactionUiState.Loading
        viewModelScope.launch {
            try {
                transactionDao.insert(transaction)
                // Reload transactions after insertion
                loadTransactions()
            } catch (e: Exception) {
                _transactionState.value = TransactionUiState.Error(e.message ?: "Failed to add transaction")
            }
        }
    }


    private fun loadAccounts() {
        viewModelScope.launch {
            _accountState.value = AccountUiState.Loading
            try {
                accountDao.getAllAccounts().collect { accounts ->
                    if (accounts.isEmpty()) {
                        _accountState.value = AccountUiState.Error("No accounts found.")
                    } else {
                        _accounts.value = accounts
                        _accountState.value = AccountUiState.Success(accounts)

                    }
                }
            } catch (e: Exception) {
                _accountState.value = AccountUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addAccounts(accounts: List<Account>) {
        viewModelScope.launch {
            try {
                accountDao.insertAll(accounts)
                loadAccounts() // Reload accounts after insertion
            } catch (e: Exception) {
                _accountState.value = AccountUiState.Error(e.message ?: "Failed to add accounts")
            }
        }
    }

    fun validateAndTransfer(
        sourceAccountId: String,
        destinationAccountId: String,
        amount: Double,
        onTransferComplete: (Boolean) -> Unit
    ) {
        val sourceAccount = _accounts.value.find { it.id.toString() == sourceAccountId }
        val destinationAccount = _accounts.value.find { it.id.toString() == destinationAccountId }

        Log.i("TAG","the sourceAccount is $sourceAccount")
        Log.i("TAG","the destinationAccount is $destinationAccount")
        if (sourceAccount == null || destinationAccount == null || amount <= 0) {
            onTransferComplete(false)
            return
        }

        if (sourceAccount.balance < amount) {
            onTransferComplete(false)
            return
        }

        performTransfer(sourceAccount, destinationAccount, amount, onTransferComplete)
    }

    private fun performTransfer(
        sourceAccount: Account,
        destinationAccount: Account,
        amount: Double,
        onTransferComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Update the source account balance by debiting the amount
                accountDao.debitAccount(sourceAccount.id.toString(), amount)

                // Update the destination account balance by crediting the amount
                accountDao.creditAccount(destinationAccount.id.toString(), amount)

                // Create a transaction record
                val transaction = Transaction(
                    sourceAccount = sourceAccount.name,
                    destinationAccount = destinationAccount.accountName,
                    amount = amount,
                    transactionType = "Transfer",
                    dateTime = System.currentTimeMillis().toString()
                )
                transactionDao.insert(transaction)

                // Optionally reload transactions
                loadTransactions()

                onTransferComplete(true)
            } catch (e: Exception) {
                Log.i("TAG", e.toString())
                onTransferComplete(false)
            }
        }
    }


    class TransactionViewModelFactory(private val transactionDao: TransactionDao, private val accountDao: AccountDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(transactionDao,accountDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

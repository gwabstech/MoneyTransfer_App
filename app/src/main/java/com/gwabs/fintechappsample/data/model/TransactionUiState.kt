package com.gwabs.fintechappsample.data.model



sealed class TransactionUiState {
    data object Loading : TransactionUiState()
    data class Success(val transactions: List<Transaction>) : TransactionUiState()
    data class Error(val message: String) : TransactionUiState()
}

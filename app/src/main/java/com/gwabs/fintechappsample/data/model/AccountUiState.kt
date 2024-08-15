package com.gwabs.fintechappsample.data.model

sealed class AccountUiState {
    data object Idle : AccountUiState()
    data object Loading : AccountUiState()
    data class Success(val accounts: List<Account>) : AccountUiState()
    data class Error(val error: String) : AccountUiState()
}
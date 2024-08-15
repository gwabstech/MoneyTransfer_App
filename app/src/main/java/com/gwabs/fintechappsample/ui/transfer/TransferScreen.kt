package com.gwabs.fintechappsample.ui.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gwabs.fintechappsample.data.model.Account
import com.gwabs.fintechappsample.data.model.AccountUiState
import com.gwabs.fintechappsample.ui.DropdownMenu
import com.gwabs.fintechappsample.ui.GradientButton
import com.gwabs.fintechappsample.ui.LoadingIndicator
import com.gwabs.fintechappsample.ui.StandardTextField
import com.gwabs.fintechappsample.ui.SuggestionButtons
import com.gwabs.fintechappsample.ui.TransferSummary
import com.gwabs.fintechappsample.ui.transaction.TransactionViewModel

@Composable
fun TransferScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    transactionViewModel: TransactionViewModel,
) {
    val accountsState by transactionViewModel.accountState.collectAsState()
    var sourceAccount by remember { mutableStateOf<Account?>(null) }
    var destinationAccount by remember { mutableStateOf<Account?>(null) }
    var amount by remember { mutableStateOf("") }
    var showSummary by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        Header (title = "Transfer Money",){
            navController.popBackStack()
        }
        Spacer(modifier = Modifier.height(20.dp))
        when (accountsState) {
            AccountUiState.Idle -> {}
            AccountUiState.Loading -> {
                LoadingIndicator()
            }
            is AccountUiState.Success -> {
                val accounts = (accountsState as AccountUiState.Success).accounts
                Spacer(modifier = Modifier.height(20.dp))

                DropdownMenu(
                    accounts = accounts,
                    selectedAccount = sourceAccount,
                    onAccountSelected = { account -> sourceAccount = account },
                    label = "Select Source Account",
                )

                Spacer(modifier = Modifier.height(16.dp))

                DropdownMenu(
                    accounts = accounts.filter { it != sourceAccount },
                    selectedAccount = destinationAccount,
                    onAccountSelected = { account -> destinationAccount = account },
                    label = "Select Destination Account"
                )

                Spacer(modifier = Modifier.height(20.dp))


                StandardTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Amount",
                    keyboardType = KeyboardType.Number
                )


                Spacer(modifier = Modifier.height(20.dp))

                SuggestionButtons {
                    amount = it
                }

                Spacer(modifier = Modifier.height(30.dp))
                GradientButton(
                    title = "Continue",
                    onClick = { showSummary = true },
                    isEnable = sourceAccount != null && destinationAccount != null && amount.isNotEmpty()
                )
            }
            is AccountUiState.Error -> {
                Text(text = "Error loading accounts")
            }
        }

        if (showSummary) {
            TransferSummary(
                sourceAccount = sourceAccount,
                destinationAccount = destinationAccount,
                amount = amount.toDoubleOrNull() ?: 0.0,
                onConfirm = {
                    transactionViewModel.validateAndTransfer(
                        sourceAccountId = (sourceAccount?.id ?: "").toString(),
                        destinationAccountId = (destinationAccount?.id ?: "").toString(),
                        amount = amount.toDoubleOrNull() ?: 0.0
                    ) { success ->
                        if (success) {
                            showSummary = false
                            showSuccessDialog = true
                        } else {
                            // Handle failure case if needed
                        }
                    }
                },
                onDismiss = { showSummary = false }
            )
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text(text = "Transfer Successful") },
                text = { Text(text = "Your transfer has been completed successfully.") },
                confirmButton = {
                    Button(
                        onClick = { showSuccessDialog = false }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    title: String,
    onBack: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
        windowInsets = TopAppBarDefaults.windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    letterSpacing = 5.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
    )
}


@Preview
@Composable
private fun TransferScreenPreview(){
    MaterialTheme{
       // TransferScreen()
    }
}
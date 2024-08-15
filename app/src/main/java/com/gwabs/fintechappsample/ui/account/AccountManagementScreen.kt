package com.gwabs.fintechappsample.ui.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gwabs.fintechappsample.data.model.AccountUiState
import com.gwabs.fintechappsample.ui.AccountItem
import com.gwabs.fintechappsample.ui.ErrorMessage
import com.gwabs.fintechappsample.ui.LoadingIndicator
import com.gwabs.fintechappsample.ui.navigation.Destinations
import com.gwabs.fintechappsample.ui.transaction.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManagementScreen(
    transactionViewModel: TransactionViewModel,
    navController: NavController,
    onLogout:()->Unit
) {
    val accountUiState by transactionViewModel.accountState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        Header(){
            onLogout()
        }
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 40.dp)
        ) {
            when (accountUiState) {
                is AccountUiState.Success -> {
                    val accounts = (accountUiState as AccountUiState.Success).accounts

                    items(accounts) { account ->
                        AccountItem(
                            account = account,
                            onClick = {
                                try {
                                  //  val accountJson = Gson().toJson(account)
                                   // val encodedUserAccountJson = URLEncoder.encode(accountJson, "UTF-8")
                                    navController.navigate("${Destinations.AccountDetailedScreen}/${account.id}")
                                } catch (e: Exception) {
                                    Log.i("TAG",e.toString())
                                }

                            }
                        )
                    }
                }

                is AccountUiState.Error -> {
                    item {
                        ErrorMessage(message = (accountUiState as AccountUiState.Error).error)
                    }
                }
                AccountUiState.Idle -> {

                }
                AccountUiState.Loading -> {
                   item {
                       LoadingIndicator(message = "Loading Accounts Please Wait..")
                   }

                }
            }

        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(onLogout:()->Unit) {
    TopAppBar(
        windowInsets = TopAppBarDefaults.windowInsets,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
            Text(
                text = "Account Manager",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {

                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.titleMedium,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 10.dp, bottom = 5.dp)
                        .clickable { onLogout() }
                )

        }
    )
}

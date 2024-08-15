package com.gwabs.fintechappsample

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.gwabs.fintechappsample.data.local.AppDatabase
import com.gwabs.fintechappsample.ui.account.AccountDetailedScreen
import com.gwabs.fintechappsample.ui.account.AccountManagementScreen
import com.gwabs.fintechappsample.ui.navigation.Destinations
import com.gwabs.fintechappsample.ui.theme.FintechAppSampleTheme
import com.gwabs.fintechappsample.ui.transaction.TransactionViewModel
import com.gwabs.fintechappsample.ui.transfer.TransferScreen

class HomeActivity : ComponentActivity() {

    private var mAuth: FirebaseAuth? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mAuth = FirebaseAuth.getInstance()
        val transactionDao = AppDatabase.getInstance(this).transactionDao()
        val accountDao = AppDatabase.getInstance(this).accountDao()
        val transactionViewModel: TransactionViewModel by viewModels {
            TransactionViewModel.TransactionViewModelFactory(transactionDao,accountDao)
        }

        setContent {
            FintechAppSampleTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavHost(
                        this,
                        paddingValues = innerPadding,
                        transactionViewModel = transactionViewModel
                    ){
                        mAuth!!.signOut()
                        finish()
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    context: Context,
    navController: NavHostController = rememberNavController(),
    paddingValues: PaddingValues,
    transactionViewModel: TransactionViewModel,
    onLogout:()->Unit
    ) {
    NavHost(
        navController = navController,
        startDestination = Destinations.AccountManagementScreen.route,
    ) {
        composable(Destinations.AccountManagementScreen.route) {
            AccountManagementScreen(transactionViewModel,navController = navController,){
               onLogout()
            }

        }
        composable(
            Destinations.AccountDetailedScreen.route,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            AccountDetailedScreen(
                navController = navController,
                navBackStackEntry = backStackEntry,
                transactionViewModel = transactionViewModel
            )
        }

        composable(
            Destinations.TransferScreen.route,
        ) {
            TransferScreen(paddingValues,navController,transactionViewModel)
        }

    }
}

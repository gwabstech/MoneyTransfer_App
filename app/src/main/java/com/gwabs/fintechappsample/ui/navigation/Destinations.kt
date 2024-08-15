
package com.gwabs.fintechappsample.ui.navigation

import com.gwabs.fintechappsample.data.model.Account

sealed class Destinations(val route: String) {
	data object AccountManagementScreen : Destinations(route = "AccountManagementScreen")
	data object AccountDetailedScreen : Destinations(route= "AccountDetailedScreen/{accountId}") {
		fun createRoute(accountId: Int) = "AccountDetailedScreen/$accountId"
	}
	data object TransferScreen : Destinations(route = "TransferScreen")

	//data object TransactionHistoryScreen:Destinations("TransactionHistoryScreen")
}



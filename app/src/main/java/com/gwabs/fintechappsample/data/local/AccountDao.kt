package com.gwabs.fintechappsample.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gwabs.fintechappsample.data.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getAccountById(accountId: Int): Flow<Account>

    @Insert
    suspend fun insert(account: Account)

    @Insert
    suspend fun insertAll(accounts: List<Account>)

    @Query("UPDATE accounts SET balance = balance - :amount WHERE id = :accountId")
    suspend fun debitAccount(accountId: String, amount: Double)

    @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :accountId")
    suspend fun creditAccount(accountId: String, amount: Double)

    @Query("SELECT balance FROM accounts WHERE id = :accountId")
    fun getBalance(accountId: String): Flow<Double>
}
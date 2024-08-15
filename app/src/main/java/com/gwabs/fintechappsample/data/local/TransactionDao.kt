package com.gwabs.fintechappsample.data.local



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gwabs.fintechappsample.data.model.Account
import com.gwabs.fintechappsample.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions():  Flow<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)
}



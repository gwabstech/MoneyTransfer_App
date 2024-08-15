package com.gwabs.fintechappsample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val transactionType: String,
    val dateTime: String,
    val destinationAccount: String,
    val sourceAccount:String
)
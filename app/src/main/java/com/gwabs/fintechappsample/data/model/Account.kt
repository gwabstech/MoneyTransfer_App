package com.gwabs.fintechappsample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    var balance: Double,
    val accountNumber: String,
    val bankName: String,
    val accountName: String
)

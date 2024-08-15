package com.gwabs.fintechappsample.util

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import com.gwabs.fintechappsample.data.model.Account
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


fun formatToNaira(amount: Double): String {
    // Define the symbols for the Nigerian locale
    val symbols = DecimalFormatSymbols(Locale("en", "NG")).apply {
        currencySymbol = "₦"
        groupingSeparator = ','
        decimalSeparator = '.'
    }

    // Define the format with the symbols and pattern
    val decimalFormat = DecimalFormat("#,##0.00", symbols).apply {
        isDecimalSeparatorAlwaysShown = false
    }

    return decimalFormat.format(amount)
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestampToNigerianTime(timestamp: Long): String {
    // Define the Nigerian timezone
    val zoneId = ZoneId.of("Africa/Lagos")

    // Convert the timestamp to an Instant
    val instant = Instant.ofEpochMilli(timestamp)

    // Format the Instant to a readable date and time string in the specified timezone
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(zoneId)
    return formatter.format(instant)
}


val suggestions = listOf("200", "500", "1000")
val mockUserAccounts = listOf(
    Account(1, "Abubakar Abdullahi", 1500.0, "1234567890", "First Bank", "Abubakar Abdullahi"),
    Account(2, "Jane Smith", 2300.5, "2345678901", "GT Bank", "Jane Smith"),
    Account(3, "Aliyu musa", 500.0, "3456789012", "Access Bank", "Aliyu musa"),
    Account(4, "Michael Brown", 780.75, "4567890123", "Zenith Bank", "Michael Brown"),
    Account(5, "Abbah Gwabare", 9500.0, "5678901234", "Union Bank", "Abbah Gwabare"),
    Account(6, "Daniel Wilson", 1100.0, "6789012345", "UBA", "Daniel Wilson"),
    Account(7, "Emma Martinez", 6450.30, "7890123456", "Ecobank", "Emma Martinez"),
    Account(8, "Matthew Taylor", 3200.0, "8901234567", "Sterling Bank", "Matthew Taylor"),
    Account(9, "Olivia Anderson", 410.0, "9012345678", "Fidelity Bank", "Olivia Anderson"),
    Account(10, "Liam Thomas", 2750.0, "0123456789", "Heritage Bank", "Liam Thomas"),
    Account(11, "Amina Bello", 1500.0, "1029384756", "Jaiz Bank", "Amina Bello"),
    Account(12, "Ibrahim Musa", 3200.0, "5647382910", "Unity Bank", "Ibrahim Musa"),
    Account(13, "Chinwe Okeke", 2500.0, "0192837465", "Polaris Bank", "Chinwe Okeke"),
    Account(14, "Abdullahi Sani", 1800.0, "8392017465", "Wema Bank", "Abdullahi Sani"),
    Account(15, "Ngozi Nwosu", 5000.0, "6574839201", "FCMB", "Ngozi Nwosu")
)


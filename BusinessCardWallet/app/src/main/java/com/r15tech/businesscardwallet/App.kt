package com.r15tech.businesscardwallet

import android.app.Application
import com.r15tech.businesscardwallet.data.AppDatabase
import com.r15tech.businesscardwallet.repository.BusinessCardRepository

class App : Application() {

    val database by lazy{ AppDatabase.getDatabase(this)}
    val repository by lazy { BusinessCardRepository(database.businessDao()) }
}
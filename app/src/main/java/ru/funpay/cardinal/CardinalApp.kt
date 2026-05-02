package ru.funpay.cardinal

import android.app.Application
import ru.funpay.cardinal.data.db.CardinalDatabase

class CardinalApp : Application() {
    lateinit var database: CardinalDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = CardinalDatabase(this)
    }
}

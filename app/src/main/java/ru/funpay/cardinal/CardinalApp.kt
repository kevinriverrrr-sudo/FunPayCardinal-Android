package ru.funpay.cardinal

import android.app.Application
import androidx.room.Room
import ru.funpay.cardinal.data.db.CardinalDatabase

class CardinalApp : Application() {
    lateinit var database: CardinalDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            CardinalDatabase::class.java,
            "cardinal_database"
        ).build()
    }
}

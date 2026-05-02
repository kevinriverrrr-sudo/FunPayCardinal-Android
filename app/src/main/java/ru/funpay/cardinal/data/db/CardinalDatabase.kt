package ru.funpay.cardinal.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.funpay.cardinal.data.db.dao.*
import ru.funpay.cardinal.data.db.entities.*

@Database(
    entities = [
        ConfigEntity::class,
        AutoDeliveryEntity::class,
        AutoResponseEntity::class,
        LogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CardinalDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun autoDeliveryDao(): AutoDeliveryDao
    abstract fun autoResponseDao(): AutoResponseDao
    abstract fun logDao(): LogDao
}

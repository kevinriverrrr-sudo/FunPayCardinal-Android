package ru.funpay.cardinal.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configs")
data class ConfigEntity(
    @PrimaryKey
    val key: String,
    val value: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "auto_delivery_items")
data class AutoDeliveryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val lotName: String,
    val responseTemplate: String,
    val productFileName: String,
    val enabled: Boolean = true,
    val multiDelivery: Boolean = false
)

@Entity(tableName = "auto_response_items")
data class AutoResponseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val command: String,
    val responseText: String,
    val enabled: Boolean = true
)

@Entity(tableName = "log_entries")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val level: String = "INFO",
    val tag: String,
    val message: String
)

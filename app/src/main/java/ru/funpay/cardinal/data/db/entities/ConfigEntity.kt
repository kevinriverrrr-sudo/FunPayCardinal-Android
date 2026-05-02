package ru.funpay.cardinal.data.db.entities

data class ConfigEntity(
    val key: String,
    val value: String,
    val updatedAt: Long = System.currentTimeMillis()
)

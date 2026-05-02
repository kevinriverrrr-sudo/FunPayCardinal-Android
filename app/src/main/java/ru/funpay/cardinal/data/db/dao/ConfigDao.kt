package ru.funpay.cardinal.data.db

import kotlinx.coroutines.flow.Flow
import ru.funpay.cardinal.data.db.entities.ConfigEntity

interface ConfigDao {
    suspend fun getConfig(key: String): ConfigEntity?
    fun getAllConfigs(): Flow<List<ConfigEntity>>
    suspend fun insertConfig(config: ConfigEntity)
    suspend fun insertConfigs(configs: List<ConfigEntity>)
    suspend fun deleteConfig(config: ConfigEntity)
    suspend fun clearAll()
    suspend fun exists(key: String): Int
    suspend fun isConfigured(): Boolean
}

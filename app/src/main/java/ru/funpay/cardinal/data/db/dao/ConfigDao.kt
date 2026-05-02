package ru.funpay.cardinal.data.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.funpay.cardinal.data.db.entities.*

@Dao
interface ConfigDao {
    @Query("SELECT * FROM configs WHERE `key` = :key LIMIT 1")
    suspend fun getConfig(key: String): ConfigEntity?

    @Query("SELECT * FROM configs")
    fun getAllConfigs(): Flow<List<ConfigEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigs(configs: List<ConfigEntity>)

    @Delete
    suspend fun deleteConfig(config: ConfigEntity)

    @Query("DELETE FROM configs")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM configs WHERE `key` = :key")
    suspend fun exists(key: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM configs WHERE `key` = 'golden_key' AND value != '')")
    suspend fun isConfigured(): Boolean
}

@Dao
interface AutoDeliveryDao {
    @Query("SELECT * FROM auto_delivery_items ORDER BY lotName ASC")
    fun getAllDeliveries(): Flow<List<AutoDeliveryEntity>>

    @Query("SELECT * FROM auto_delivery_items WHERE id = :id")
    suspend fun getDeliveryById(id: Long): AutoDeliveryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDelivery(delivery: AutoDeliveryEntity): Long

    @Update
    suspend fun updateDelivery(delivery: AutoDeliveryEntity)

    @Delete
    suspend fun deleteDelivery(delivery: AutoDeliveryEntity)

    @Query("UPDATE auto_delivery_items SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: Long, enabled: Boolean)
}

@Dao
interface AutoResponseDao {
    @Query("SELECT * FROM auto_response_items ORDER BY command ASC")
    fun getAllResponses(): Flow<List<AutoResponseEntity>>

    @Query("SELECT * FROM auto_response_items WHERE id = :id")
    suspend fun getResponseById(id: Long): AutoResponseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: AutoResponseEntity): Long

    @Update
    suspend fun updateResponse(response: AutoResponseEntity)

    @Delete
    suspend fun deleteResponse(response: AutoResponseEntity)

    @Query("UPDATE auto_response_items SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: Long, enabled: Boolean)
}

@Dao
interface LogDao {
    @Query("SELECT * FROM log_entries ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentLogs(limit: Int = 200): Flow<List<LogEntity>>

    @Insert
    suspend fun insertLog(log: LogEntity)

    @Query("DELETE FROM log_entries WHERE timestamp < :beforeTimestamp")
    suspend fun deleteLogsBefore(beforeTimestamp: Long)

    @Query("DELETE FROM log_entries")
    suspend fun clearAll()
}

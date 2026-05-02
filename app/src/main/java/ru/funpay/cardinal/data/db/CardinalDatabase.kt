package ru.funpay.cardinal.data.db

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.funpay.cardinal.data.db.entities.*

/**
 * Простое хранилище настроек на основе SharedPreferences.
 * Используется вместо Room для быстрой сборки.
 * 
 * Для полноценной версии с Room:
 * 1. Раскомментируйте Chaquopy plugin в build.gradle.kts
 * 2. Добавьте annotationProcessor/ksp для Room
 * 3. Используйте CardinalDatabase из Room
 */
class CardinalDatabase(context: Context) {

    private val prefs = context.getSharedPreferences("cardinal_prefs", Context.MODE_PRIVATE)

    private val _configsChanged = MutableStateFlow(0L)
    val configsChanged: Flow<Long> = _configsChanged

    fun configDao(): ConfigDao = ConfigDaoImpl(prefs, _configsChanged)
}

class ConfigDaoImpl(
    private val prefs: android.content.SharedPreferences,
    private val changeNotifier: MutableStateFlow<Long>
) : ConfigDao {

    override suspend fun getConfig(key: String): ConfigEntity? {
        val value = prefs.getString(key, null) ?: return null
        return ConfigEntity(key, value, prefs.getLong("${key}_updated", System.currentTimeMillis()))
    }

    override fun getAllConfigs(): Flow<List<ConfigEntity>> {
        return changeNotifier.map {
            prefs.all.entries.mapNotNull { entry ->
                if (!entry.key.endsWith("_updated") && entry.value is String) {
                    ConfigEntity(
                        key = entry.key,
                        value = entry.value as String,
                        updatedAt = prefs.getLong("${entry.key}_updated", System.currentTimeMillis())
                    )
                } else null
            }
        }
    }

    override suspend fun insertConfig(config: ConfigEntity) {
        prefs.edit()
            .putString(config.key, config.value)
            .putLong("${config.key}_updated", config.updatedAt)
            .apply()
        changeNotifier.value = System.currentTimeMillis()
    }

    override suspend fun insertConfigs(configs: List<ConfigEntity>) {
        prefs.edit().apply {
            configs.forEach { config ->
                putString(config.key, config.value)
                putLong("${config.key}_updated", config.updatedAt)
            }
        }.apply()
        changeNotifier.value = System.currentTimeMillis()
    }

    override suspend fun deleteConfig(config: ConfigEntity) {
        prefs.edit().remove(config.key).remove("${config.key}_updated").apply()
        changeNotifier.value = System.currentTimeMillis()
    }

    override suspend fun clearAll() {
        prefs.edit().clear().apply()
        changeNotifier.value = System.currentTimeMillis()
    }

    override suspend fun exists(key: String): Int {
        return if (prefs.contains(key)) 1 else 0
    }

    override suspend fun isConfigured(): Boolean {
        val gk = prefs.getString("golden_key", "") ?: ""
        return gk.isNotEmpty()
    }
}

package ru.funpay.cardinal.data.repository

import kotlinx.coroutines.flow.Flow
import ru.funpay.cardinal.data.db.entities.ConfigEntity

class ConfigRepository(
    private val configDao: Any
) {
    @Suppress("UNCHECKED_CAST")
    private val dao = configDao as ru.funpay.cardinal.data.db.ConfigDao

    suspend fun getConfig(key: String): String? = dao.getConfig(key)?.value

    fun getAllConfigs(): Flow<List<ConfigEntity>> = dao.getAllConfigs()

    suspend fun saveConfig(key: String, value: String) {
        dao.insertConfig(ConfigEntity(key = key, value = value))
    }

    suspend fun saveConfigs(configs: Map<String, String>) {
        dao.insertConfigs(configs.map { ConfigEntity(key = it.key, value = it.value) })
    }

    suspend fun isConfigured(): Boolean = dao.isConfigured()

    fun getGoldenKey(): Flow<String?> = kotlinx.coroutines.flow.flow {
        emit(dao.getConfig("golden_key")?.value)
    }

    suspend fun getGoldenKeyOnce(): String? = dao.getConfig("golden_key")?.value

    suspend fun getUserAgent(): String? = dao.getConfig("user_agent")?.value

    suspend fun getTelegramToken(): String? = dao.getConfig("telegram_token")?.value

    suspend fun setAutoRaise(enabled: Boolean) = saveConfig("auto_raise", enabled.toString())
    suspend fun getAutoRaise(): Boolean = getConfig("auto_raise")?.toBoolean() ?: true

    suspend fun setAutoDelivery(enabled: Boolean) = saveConfig("auto_delivery", enabled.toString())
    suspend fun getAutoDelivery(): Boolean = getConfig("auto_delivery")?.toBoolean() ?: true

    suspend fun setAutoResponse(enabled: Boolean) = saveConfig("auto_response", enabled.toString())
    suspend fun getAutoResponse(): Boolean = getConfig("auto_response")?.toBoolean() ?: false

    suspend fun setAutoRestore(enabled: Boolean) = saveConfig("auto_restore", enabled.toString())
    suspend fun getAutoRestore(): Boolean = getConfig("auto_restore")?.toBoolean() ?: true

    suspend fun setAutoDisable(enabled: Boolean) = saveConfig("auto_disable", enabled.toString())
    suspend fun getAutoDisable(): Boolean = getConfig("auto_disable")?.toBoolean() ?: true

    suspend fun setLocale(locale: String) = saveConfig("locale", locale)
    suspend fun getLocale(): String? = getConfig("locale")

    suspend fun setProxyEnabled(enabled: Boolean) = saveConfig("proxy_enabled", enabled.toString())
    suspend fun getProxyEnabled(): Boolean = getConfig("proxy_enabled")?.toBoolean() ?: false

    suspend fun setProxyAddress(address: String) = saveConfig("proxy_address", address)
    suspend fun getProxyAddress(): String? = getConfig("proxy_address")

    suspend fun setRequestsDelay(delay: Int) = saveConfig("requests_delay", delay.toString())
    suspend fun getRequestsDelay(): Int = getConfig("requests_delay")?.toIntOrNull() ?: 4

    suspend fun clearAllConfigs() = dao.clearAll()
}

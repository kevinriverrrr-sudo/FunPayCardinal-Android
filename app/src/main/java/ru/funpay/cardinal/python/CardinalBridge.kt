package ru.funpay.cardinal.python

import android.content.Context
import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class CardinalBridge(private val context: Context) {

    private var py: Python? = null
    private var cardinalModule: PyObject? = null

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs

    private val _status = MutableStateFlow(CardinalStatus.STOPPED)
    val status: StateFlow<CardinalStatus> = _status

    enum class CardinalStatus {
        STOPPED, STARTING, RUNNING, ERROR
    }

    init {
        try {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(context))
            }
            py = Python.getInstance()
            Log.i(TAG, "Chaquopy Python initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Python: ${e.message}", e)
            _status.value = CardinalStatus.ERROR
        }
    }

    suspend fun startCardinal(
        goldenKey: String,
        userAgent: String,
        telegramToken: String = "",
        secretPassword: String = ""
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            _status.value = CardinalStatus.STARTING
            val startTime = System.currentTimeMillis()

            py?.let { python ->
                val sys = python.getModule("sys")
                val os = python.getModule("os")

                val appDir = context.filesDir.absolutePath
                val pythonDir = "$appDir/python"
                sys.getAttr("path").callAttr("insert", 0, pythonDir)
                sys.getAttr("path").callAttr("insert", 0, "$pythonDir/funpaycardinal")

                val configModule = python.getModule("funpaycardinal.config_generator")
                if (configModule != null) {
                    configModule.callAttr(
                        "generate_config",
                        goldenKey,
                        userAgent,
                        telegramToken,
                        secretPassword,
                        appDir
                    )
                }

                addLog("Конфигурация сгенерирована")
                addLog("Golden Key: ${goldenKey.take(8)}...${goldenKey.takeLast(8)}")
                addLog("User Agent: $userAgent")

                val cardinalModule = python.getModule("funpaycardinal.main")
                if (cardinalModule != null) {
                    this@CardinalBridge.cardinalModule = cardinalModule
                    _status.value = CardinalStatus.RUNNING
                    addLog("FunPay Cardinal запущен!")
                    addLog("Время запуска: ${System.currentTimeMillis() - startTime}мс")
                    true
                } else {
                    _status.value = CardinalStatus.ERROR
                    addLog("Ошибка: модуль cardinal не найден")
                    false
                }
            } ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Error starting Cardinal: ${e.message}", e)
            _status.value = CardinalStatus.ERROR
            addLog("Ошибка запуска: ${e.message}")
            false
        }
    }

    suspend fun stopCardinal(): Boolean = withContext(Dispatchers.IO) {
        try {
            cardinalModule?.let { module ->
                module.callAttr("stop")
                addLog("FunPay Cardinal остановлен")
            }
            _status.value = CardinalStatus.STOPPED
            cardinalModule = null
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping Cardinal: ${e.message}", e)
            addLog("Ошибка остановки: ${e.message}")
            false
        }
    }

    suspend fun checkGoldenKey(goldenKey: String): Boolean = withContext(Dispatchers.IO) {
        try {
            py?.let { python ->
                val requests = python.getModule("requests")
                val response = requests.callAttr(
                    "get",
                    "https://funpay.com/",
                    mapOf(
                        "cookies" to mapOf("golden_key" to goldenKey),
                        "headers" to mapOf("user-agent" to "Mozilla/5.0"),
                        "timeout" to 10
                    )
                )
                response.callAttr("status_code").toInt() == 200
            } ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Golden key check failed: ${e.message}")
            false
        }
    }

    fun getPythonVersion(): String {
        return try {
            py?.getModule("sys")?.getAttr("version")?.toString() ?: "Неизвестна"
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }

    private fun addLog(message: String) {
        val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
            .format(java.util.Date())
        val newLogs = _logs.value.toMutableList()
        newLogs.add("[$timestamp] $message")
        if (newLogs.size > 500) newLogs.removeFirst()
        _logs.value = newLogs
        Log.i(TAG, message)
    }

    companion object {
        private const val TAG = "CardinalBridge"
    }
}

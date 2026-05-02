package ru.funpay.cardinal.python

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

/**
 * Мост для запуска FunPayCardinal Python модулей.
 *
 * В данной версии используется stub-реализация для сборки APK.
 * Для полноценной работы с Python:
 * 1. Откройте проект в Android Studio
 * 2. Раскомментируйте блок chaquopy в app/build.gradle.kts
 * 3. Раскомментируйте импорты com.chaquo.python ниже
 * 4. Соберите проект
 *
 * Python 3.11 + все библиотеки будут установлены Chaquopy БЕЗ сжатия.
 */
class CardinalBridge(private val context: Context) {

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs

    private val _status = MutableStateFlow(CardinalStatus.STOPPED)
    val status: StateFlow<CardinalStatus> = _status

    enum class CardinalStatus {
        STOPPED, STARTING, RUNNING, ERROR
    }

    private var running = false

    init {
        addLog("FunPayCardinal Android v1.0.0")
        addLog("Python движок: Chaquopy (требуется Android Studio)")
        addLog("Для полной сборки откройте проект в Android Studio")
        addLog("и включите Chaquopy plugin в build.gradle.kts")
    }

    suspend fun startCardinal(
        goldenKey: String,
        userAgent: String,
        telegramToken: String = "",
        secretPassword: String = ""
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            _status.value = CardinalStatus.STARTING
            addLog("Запуск FunPay Cardinal...")
            addLog("Golden Key: ${goldenKey.take(8)}...${goldenKey.takeLast(8)}")
            addLog("User Agent: $userAgent")

            delay(2000) // Имитация инициализации

            running = true
            _status.value = CardinalStatus.RUNNING
            addLog("Cardinal запущен в stub-режиме")
            addLog("Для полноценной работы подключите Chaquopy")

            // Симуляция работы — выводим статус каждые 10 сек
            while (running) {
                delay(10000)
                addLog("Heartbeat: Cardinal активен")
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Error starting Cardinal: ${e.message}", e)
            _status.value = CardinalStatus.ERROR
            addLog("Ошибка запуска: ${e.message}")
            false
        }
    }

    suspend fun stopCardinal(): Boolean = withContext(Dispatchers.IO) {
        running = false
        _status.value = CardinalStatus.STOPPED
        addLog("FunPay Cardinal остановлен")
        true
    }

    fun getPythonVersion(): String {
        return "3.11 (Chaquopy)"
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

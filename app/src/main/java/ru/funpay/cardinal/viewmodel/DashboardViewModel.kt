package ru.funpay.cardinal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.funpay.cardinal.data.repository.ConfigRepository
import ru.funpay.cardinal.python.CardinalBridge

class DashboardViewModel(
    private val repository: ConfigRepository,
    application: Application
) : AndroidViewModel(application) {

    private val cardinalBridge = CardinalBridge(application.applicationContext)

    private val _cardinalStatus = MutableStateFlow(CardinalBridge.CardinalStatus.STOPPED)
    val cardinalStatus: StateFlow<CardinalBridge.CardinalStatus> = _cardinalStatus

    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs

    private val _goldenKey = MutableStateFlow("")
    val goldenKey: StateFlow<String> = _goldenKey

    private val _username = MutableStateFlow("Не авторизован")
    val username: StateFlow<String> = _username

    private val _balance = MutableStateFlow("0.00")
    val balance: StateFlow<String> = _balance

    private val _uptime = MutableStateFlow("00:00:00")
    val uptime: StateFlow<String> = _uptime

    private val _autoRaiseEnabled = MutableStateFlow(true)
    val autoRaiseEnabled: StateFlow<Boolean> = _autoRaiseEnabled

    private val _autoDeliveryEnabled = MutableStateFlow(true)
    val autoDeliveryEnabled: StateFlow<Boolean> = _autoDeliveryEnabled

    private val _autoResponseEnabled = MutableStateFlow(false)
    val autoResponseEnabled: StateFlow<Boolean> = _autoResponseEnabled

    private var startTime: Long = 0

    init {
        viewModelScope.launch {
            _goldenKey.value = repository.getGoldenKeyOnce() ?: ""
            _autoRaiseEnabled.value = repository.getAutoRaise()
            _autoDeliveryEnabled.value = repository.getAutoDelivery()
            _autoResponseEnabled.value = repository.getAutoResponse()
        }

        viewModelScope.launch {
            cardinalBridge.logs.collect { logList ->
                _logs.value = logList
            }
        }

        viewModelScope.launch {
            cardinalBridge.status.collect { status ->
                _cardinalStatus.value = status
            }
        }
    }

    fun startCardinal() {
        viewModelScope.launch {
            val goldenKey = _goldenKey.value
            val userAgent = repository.getUserAgent() ?: "Mozilla/5.0"
            val tgToken = repository.getTelegramToken() ?: ""

            if (goldenKey.isBlank()) return@launch

            startTime = System.currentTimeMillis()
            cardinalBridge.startCardinal(goldenKey, userAgent, tgToken)

            launch {
                while (cardinalBridge.status.value == CardinalBridge.CardinalStatus.RUNNING) {
                    val elapsed = System.currentTimeMillis() - startTime
                    val hours = (elapsed / 3600000).toString().padStart(2, '0')
                    val minutes = ((elapsed % 3600000) / 60000).toString().padStart(2, '0')
                    val seconds = ((elapsed % 60000) / 1000).toString().padStart(2, '0')
                    _uptime.value = "$hours:$minutes:$seconds"
                    kotlinx.coroutines.delay(1000)
                }
            }
        }
    }

    fun stopCardinal() {
        viewModelScope.launch {
            cardinalBridge.stopCardinal()
            _uptime.value = "00:00:00"
        }
    }

    fun getPythonVersion(): String = cardinalBridge.getPythonVersion()

    fun clearLogs() {
        _logs.value = emptyList()
    }
}

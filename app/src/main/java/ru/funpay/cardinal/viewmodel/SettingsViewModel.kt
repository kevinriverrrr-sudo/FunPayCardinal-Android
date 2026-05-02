package ru.funpay.cardinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.funpay.cardinal.data.repository.ConfigRepository

class SettingsViewModel(private val repository: ConfigRepository) : ViewModel() {

    private val _autoRaiseEnabled = MutableStateFlow(true)
    val autoRaiseEnabled: StateFlow<Boolean> = _autoRaiseEnabled

    private val _autoDeliveryEnabled = MutableStateFlow(true)
    val autoDeliveryEnabled: StateFlow<Boolean> = _autoDeliveryEnabled

    private val _autoResponseEnabled = MutableStateFlow(false)
    val autoResponseEnabled: StateFlow<Boolean> = _autoResponseEnabled

    private val _autoRestoreEnabled = MutableStateFlow(true)
    val autoRestoreEnabled: StateFlow<Boolean> = _autoRestoreEnabled

    private val _autoDisableEnabled = MutableStateFlow(true)
    val autoDisableEnabled: StateFlow<Boolean> = _autoDisableEnabled

    private val _requestsDelay = MutableStateFlow(4)
    val requestsDelay: StateFlow<Int> = _requestsDelay

    private val _proxyEnabled = MutableStateFlow(false)
    val proxyEnabled: StateFlow<Boolean> = _proxyEnabled

    private val _proxyAddress = MutableStateFlow("")
    val proxyAddress: StateFlow<String> = _proxyAddress

    private val _locale = MutableStateFlow("ru")
    val locale: StateFlow<String> = _locale

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    init {
        viewModelScope.launch {
            _autoRaiseEnabled.value = repository.getAutoRaise()
            _autoDeliveryEnabled.value = repository.getAutoDelivery()
            _autoResponseEnabled.value = repository.getAutoResponse()
            _autoRestoreEnabled.value = repository.getAutoRestore()
            _autoDisableEnabled.value = repository.getAutoDisable()
            _requestsDelay.value = repository.getRequestsDelay()
            _proxyEnabled.value = repository.getProxyEnabled()
            _proxyAddress.value = repository.getProxyAddress() ?: ""
            _locale.value = repository.getLocale() ?: "ru"
        }
    }

    fun setAutoRaise(enabled: Boolean) {
        _autoRaiseEnabled.value = enabled
        viewModelScope.launch { repository.setAutoRaise(enabled) }
    }

    fun setAutoDelivery(enabled: Boolean) {
        _autoDeliveryEnabled.value = enabled
        viewModelScope.launch { repository.setAutoDelivery(enabled) }
    }

    fun setAutoResponse(enabled: Boolean) {
        _autoResponseEnabled.value = enabled
        viewModelScope.launch { repository.setAutoResponse(enabled) }
    }

    fun setAutoRestore(enabled: Boolean) {
        _autoRestoreEnabled.value = enabled
        viewModelScope.launch { repository.setAutoRestore(enabled) }
    }

    fun setAutoDisable(enabled: Boolean) {
        _autoDisableEnabled.value = enabled
        viewModelScope.launch { repository.setAutoDisable(enabled) }
    }

    fun setRequestsDelay(delay: Int) {
        _requestsDelay.value = delay
        viewModelScope.launch { repository.setRequestsDelay(delay) }
    }

    fun setProxyEnabled(enabled: Boolean) {
        _proxyEnabled.value = enabled
        viewModelScope.launch { repository.setProxyEnabled(enabled) }
    }

    fun setProxyAddress(address: String) {
        _proxyAddress.value = address
        viewModelScope.launch { repository.setProxyAddress(address) }
    }

    fun setLocale(locale: String) {
        _locale.value = locale
        viewModelScope.launch { repository.setLocale(locale) }
    }

    fun resetConfigs() {
        viewModelScope.launch { repository.clearAllConfigs() }
    }
}

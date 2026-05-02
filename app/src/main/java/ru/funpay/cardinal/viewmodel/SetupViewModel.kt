package ru.funpay.cardinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.funpay.cardinal.data.repository.ConfigRepository

class SetupViewModel(private val repository: ConfigRepository) : ViewModel() {

    private val _goldenKey = MutableStateFlow("")
    val goldenKey: StateFlow<String> = _goldenKey

    private val _userAgent = MutableStateFlow("Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36")
    val userAgent: StateFlow<String> = _userAgent

    private val _telegramToken = MutableStateFlow("")
    val telegramToken: StateFlow<String> = _telegramToken

    private val _secretPassword = MutableStateFlow("")
    val secretPassword: StateFlow<String> = _secretPassword

    private val _isConfigured = MutableStateFlow(false)
    val isConfigured: StateFlow<Boolean> = _isConfigured

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError

    private val _isGoldenKeyValid = MutableStateFlow<Boolean?>(null)
    val isGoldenKeyValid: StateFlow<Boolean?> = _isGoldenKeyValid

    init {
        viewModelScope.launch {
            _isConfigured.value = repository.isConfigured()
            if (_isConfigured.value) {
                _goldenKey.value = repository.getGoldenKeyOnce() ?: ""
                _userAgent.value = repository.getUserAgent() ?: _userAgent.value
                _telegramToken.value = repository.getTelegramToken() ?: ""
            }
        }
    }

    fun updateGoldenKey(key: String) {
        _goldenKey.value = key.trim()
        _isGoldenKeyValid.value = null
    }

    fun updateUserAgent(ua: String) {
        _userAgent.value = ua
    }

    fun updateTelegramToken(token: String) {
        _telegramToken.value = token.trim()
    }

    fun updateSecretPassword(password: String) {
        _secretPassword.value = password
    }

    fun validateGoldenKey(): Boolean {
        val key = _goldenKey.value
        return when {
            key.isBlank() -> {
                _saveError.value = "Golden Key не может быть пустым"
                false
            }
            key.length != 32 -> {
                _saveError.value = "Golden Key должен содержать 32 символа"
                false
            }
            else -> {
                _saveError.value = null
                true
            }
        }
    }

    fun saveConfig() {
        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null
            try {
                if (!validateGoldenKey()) {
                    _isSaving.value = false
                    return@launch
                }
                repository.saveConfig("golden_key", _goldenKey.value)
                repository.saveConfig("user_agent", _userAgent.value)
                if (_telegramToken.value.isNotBlank()) {
                    repository.saveConfig("telegram_token", _telegramToken.value)
                }
                _isConfigured.value = true
            } catch (e: Exception) {
                _saveError.value = "Ошибка сохранения: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }
}

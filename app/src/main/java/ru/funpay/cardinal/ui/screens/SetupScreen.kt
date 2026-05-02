package ru.funpay.cardinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.funpay.cardinal.ui.theme.*
import ru.funpay.cardinal.viewmodel.SetupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    viewModel: SetupViewModel,
    onNavigateToDashboard: () -> Unit
) {
    val goldenKey by viewModel.goldenKey.collectAsState()
    val userAgent by viewModel.userAgent.collectAsState()
    val telegramToken by viewModel.telegramToken.collectAsState()
    val secretPassword by viewModel.secretPassword.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()
    val isConfigured by viewModel.isConfigured.collectAsState()

    var showGoldenKey by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(isConfigured) {
        if (isConfigured) onNavigateToDashboard()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CardinalBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Logo
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardinalPurple)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = CardinalGold,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "FunPay Cardinal",
                style = MaterialTheme.typography.headlineLarge,
                color = CardinalTextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Мобильный бот-ассистент для продавцов FunPay",
                style = MaterialTheme.typography.bodyMedium,
                color = CardinalTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Step indicator
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalSurfaceVariant
            ) {
                Text(
                    text = "Шаг 1 из 1 — Настройка",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = CardinalPurpleLight,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Golden Key field
            Text(
                text = "Golden Key",
                color = CardinalTextPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Получите его в настройках FunPay (файлы cookie → golden_key)",
                color = CardinalTextSecondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = goldenKey,
                onValueChange = viewModel::updateGoldenKey,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Введите golden_key (32 символа)", color = CardinalTextSecondary) },
                visualTransformation = if (showGoldenKey) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showGoldenKey = !showGoldenKey }) {
                        Icon(
                            imageVector = if (showGoldenKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showGoldenKey) "Скрыть" else "Показать",
                            tint = CardinalTextSecondary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardinalPurple,
                    unfocusedBorderColor = CardinalSurfaceVariant,
                    focusedTextColor = CardinalTextPrimary,
                    unfocusedTextColor = CardinalTextPrimary,
                    cursorColor = CardinalPurple
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // User Agent field
            Text(
                text = "User Agent",
                color = CardinalTextPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "User Agent браузера, в котором вы получили golden key",
                color = CardinalTextSecondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = userAgent,
                onValueChange = viewModel::updateUserAgent,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Mozilla/5.0 ...", color = CardinalTextSecondary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardinalPurple,
                    unfocusedBorderColor = CardinalSurfaceVariant,
                    focusedTextColor = CardinalTextPrimary,
                    unfocusedTextColor = CardinalTextPrimary,
                    cursorColor = CardinalPurple
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Telegram Token field
            Text(
                text = "Токен Telegram бота (необязательно)",
                color = CardinalTextPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Для получения уведомлений и управления через Telegram",
                color = CardinalTextSecondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = telegramToken,
                onValueChange = viewModel::updateTelegramToken,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("123456:ABC-DEF...", color = CardinalTextSecondary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardinalPurple,
                    unfocusedBorderColor = CardinalSurfaceVariant,
                    focusedTextColor = CardinalTextPrimary,
                    unfocusedTextColor = CardinalTextPrimary,
                    cursorColor = CardinalPurple
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Secret Password field
            Text(
                text = "Секретный пароль",
                color = CardinalTextPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Для авторизации в панели управления Telegram (8+ символов, заглавные, строчные, цифры)",
                color = CardinalTextSecondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = secretPassword,
                onValueChange = viewModel::updateSecretPassword,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Введите пароль", color = CardinalTextSecondary) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showPassword) "Скрыть" else "Показать",
                            tint = CardinalTextSecondary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CardinalPurple,
                    unfocusedBorderColor = CardinalSurfaceVariant,
                    focusedTextColor = CardinalTextPrimary,
                    unfocusedTextColor = CardinalTextPrimary,
                    cursorColor = CardinalPurple
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Error message
            if (saveError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CardinalError.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = CardinalError, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(saveError!!, color = CardinalError, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save button
            Button(
                onClick = viewModel::saveConfig,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isSaving && goldenKey.isNotBlank(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardinalPurple,
                    disabledContainerColor = CardinalPurple.copy(alpha = 0.3f)
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = CardinalGold, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Сохранить и продолжить",
                        style = MaterialTheme.typography.titleMedium,
                        color = CardinalTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

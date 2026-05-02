package ru.funpay.cardinal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.funpay.cardinal.ui.theme.*
import ru.funpay.cardinal.viewmodel.SetupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelegramSettingsScreen(
    viewModel: SetupViewModel,
    onBack: () -> Unit
) {
    val telegramToken by viewModel.telegramToken.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Telegram", color = CardinalTextPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = CardinalTextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardinalSurface)
            )
        },
        containerColor = CardinalBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Токен бота", color = CardinalTextPrimary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.titleSmall)
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Получите токен у @BotFather в Telegram. Имя бота должно начинаться с 'funpay'.",
                        color = CardinalTextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Telegram features list
            Text("Возможности через Telegram:", color = CardinalTextPrimary, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            val features = listOf(
                Icons.Default.Notifications to "Уведомления о новых сообщениях и заказах",
                Icons.Default.Chat to "Ответ на сообщения прямо из Telegram",
                Icons.Default.Settings to "Полная панель управления настройками",
                Icons.Default.BarChart to "Статистика продаж и баланса",
                Icons.Default.PowerSettingsNew to "Удалённый запуск и остановка Cardinal"
            )

            features.forEach { (icon, text) ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CardinalSurface
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, contentDescription = null, tint = CardinalPurpleLight, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text, color = CardinalTextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.funpay.cardinal.ui.theme.*
import ru.funpay.cardinal.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onNavigateToAutoDelivery: () -> Unit,
    onNavigateToAutoResponse: () -> Unit,
    onNavigateToTelegram: () -> Unit,
    onNavigateToProxy: () -> Unit
) {
    val autoRaiseEnabled by viewModel.autoRaiseEnabled.collectAsState()
    val autoDeliveryEnabled by viewModel.autoDeliveryEnabled.collectAsState()
    val autoResponseEnabled by viewModel.autoResponseEnabled.collectAsState()
    val autoRestoreEnabled by viewModel.autoRestoreEnabled.collectAsState()
    val autoDisableEnabled by viewModel.autoDisableEnabled.collectAsState()
    val requestsDelay by viewModel.requestsDelay.collectAsState()
    val locale by viewModel.locale.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки", color = CardinalTextPrimary, fontWeight = FontWeight.Bold) },
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
            Spacer(modifier = Modifier.height(8.dp))

            // Automation section
            SectionHeader("Автоматизация")

            SettingToggle(
                icon = Icons.Default.TrendingUp,
                title = "Автоподнятие лотов",
                description = "Автоматическое поднятие лотов для повышения видимости",
                enabled = autoRaiseEnabled,
                onToggle = viewModel::setAutoRaise
            )

            SettingToggle(
                icon = Icons.Default.LocalShipping,
                title = "Автодоставка товаров",
                description = "Выдача товаров после оплаты",
                enabled = autoDeliveryEnabled,
                onToggle = viewModel::setAutoDelivery
            )

            SettingToggle(
                icon = Icons.Default.Chat,
                title = "Автоответы",
                description = "Автоматические ответы на команды покупателей",
                enabled = autoResponseEnabled,
                onToggle = viewModel::setAutoResponse
            )

            SettingToggle(
                icon = Icons.Default.Restore,
                title = "Автовосстановление",
                description = "Автоматическое восстановление проданных лотов",
                enabled = autoRestoreEnabled,
                onToggle = viewModel::setAutoRestore
            )

            SettingToggle(
                icon = Icons.Default.NotificationsOff,
                title = "Автоотключение",
                description = "Отключение лотов при отсутствии товаров",
                enabled = autoDisableEnabled,
                onToggle = viewModel::setAutoDisable
            )

            // Delay slider
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("Параметры запросов")

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Задержка запросов", color = CardinalTextPrimary, fontWeight = FontWeight.Medium)
                        Text("$requestsDelay сек", color = CardinalPurpleLight, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = requestsDelay.toFloat(),
                        onValueChange = { viewModel.setRequestsDelay(it.toInt()) },
                        valueRange = 2f..30f,
                        steps = 27,
                        colors = SliderDefaults.colors(
                            thumbColor = CardinalPurple,
                            activeTrackColor = CardinalPurple,
                            inactiveTrackColor = CardinalSurfaceVariant
                        )
                    )
                    Text(
                        "Интервал между запросами к FunPay API. Меньше = быстрее, но выше нагрузка.",
                        color = CardinalTextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Navigation section
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("Конфигурация")

            SettingNavigationItem(
                icon = Icons.Default.LocalShipping,
                title = "Автодоставка",
                description = "Настройка шаблонов доставки товаров",
                onClick = onNavigateToAutoDelivery
            )

            SettingNavigationItem(
                icon = Icons.Default.Chat,
                title = "Автоответы",
                description = "Настройка команд и ответов",
                onClick = onNavigateToAutoResponse
            )

            SettingNavigationItem(
                icon = Icons.Default.Telegram,
                title = "Telegram",
                description = "Настройка бота и уведомлений",
                onClick = onNavigateToTelegram
            )

            SettingNavigationItem(
                icon = Icons.Default.VpnLock,
                title = "Прокси",
                description = "Настройка прокси-сервера",
                onClick = onNavigateToProxy
            )

            // Locale
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("Язык")

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalSurface
            ) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    listOf(
                        "ru" to "Русский",
                        "en" to "English",
                        "uk" to "Українська"
                    ).forEach { (code, name) ->
                        val isSelected = locale == code
                        Surface(
                            onClick = { viewModel.setLocale(code) },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) CardinalPurple.copy(alpha = 0.2f) else CardinalSurfaceVariant,
                            modifier = Modifier.padding(4.dp).weight(1f)
                        ) {
                            Text(
                                name,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                color = if (isSelected) CardinalPurpleLight else CardinalTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Danger zone
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader("Опасная зона")

            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CardinalError)
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Сбросить все настройки")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Сбросить настройки?", color = CardinalTextPrimary) },
            text = { Text("Все настройки будут удалены. Вам потребуется повторная настройка. Это действие необратимо.", color = CardinalTextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetConfigs()
                    showResetDialog = false
                }) {
                    Text("Удалить", color = CardinalError)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Отмена", color = CardinalTextSecondary)
                }
            },
            containerColor = CardinalSurface
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.labelLarge,
        color = CardinalPurpleLight,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingToggle(
    icon: ImageVector,
    title: String,
    description: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CardinalSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = CardinalTextSecondary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = CardinalTextPrimary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyLarge)
                Text(description, color = CardinalTextSecondary, style = MaterialTheme.typography.bodySmall)
            }
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(checkedThumbColor = CardinalPurpleLight, checkedTrackColor = CardinalPurple)
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun SettingNavigationItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = CardinalSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = CardinalTextSecondary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = CardinalTextPrimary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyLarge)
                Text(description, color = CardinalTextSecondary, style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CardinalTextSecondary, modifier = Modifier.size(20.dp))
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

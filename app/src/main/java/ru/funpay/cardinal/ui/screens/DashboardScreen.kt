package ru.funpay.cardinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.funpay.cardinal.python.CardinalBridge
import ru.funpay.cardinal.ui.theme.*
import ru.funpay.cardinal.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToLogs: () -> Unit,
    onNavigateToSetup: () -> Unit
) {
    val status by viewModel.cardinalStatus.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val uptime by viewModel.uptime.collectAsState()
    val autoRaiseEnabled by viewModel.autoRaiseEnabled.collectAsState()
    val autoDeliveryEnabled by viewModel.autoDeliveryEnabled.collectAsState()
    val autoResponseEnabled by viewModel.autoResponseEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = CardinalGold, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("FunPay Cardinal", color = CardinalTextPrimary, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSetup) {
                        Icon(Icons.Default.Key, contentDescription = "Настройки аккаунта", tint = CardinalTextSecondary)
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки", tint = CardinalTextSecondary)
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

            // Status card
            StatusCard(status = status, uptime = uptime)

            Spacer(modifier = Modifier.height(16.dp))

            // Main action button
            ActionButton(
                status = status,
                onStart = viewModel::startCardinal,
                onStop = viewModel::stopCardinal
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Features section
            Text(
                text = "Функции автоматизации",
                style = MaterialTheme.typography.titleMedium,
                color = CardinalTextPrimary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            FeatureToggleCard(
                icon = Icons.Default.TrendingUp,
                title = "Автоподнятие лотов",
                description = "Автоматическое поднятие ваших лотов для лучшей видимости",
                enabled = autoRaiseEnabled
            )

            Spacer(modifier = Modifier.height(8.dp))

            FeatureToggleCard(
                icon = Icons.Default.LocalShipping,
                title = "Автодоставка",
                description = "Автоматическая выдача товаров после оплаты заказа",
                enabled = autoDeliveryEnabled
            )

            Spacer(modifier = Modifier.height(8.dp))

            FeatureToggleCard(
                icon = Icons.Default.Chat,
                title = "Автоответы",
                description = "Автоматические ответы на сообщения покупателей по командам",
                enabled = autoResponseEnabled
            )

            Spacer(modifier = Modifier.height(24.dp))

            // System info
            Text(
                text = "Системная информация",
                style = MaterialTheme.typography.titleMedium,
                color = CardinalTextPrimary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalSurfaceVariant
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Python", value = viewModel.getPythonVersion())
                    HorizontalDivider(color = CardinalSurface, modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(label = "Движок", value = "Chaquopy + FunPayCardinal")
                    HorizontalDivider(color = CardinalSurface, modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(label = "Версия приложения", value = "1.0.0")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logs preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Последние логи",
                    style = MaterialTheme.typography.titleMedium,
                    color = CardinalTextPrimary
                )
                TextButton(onClick = onNavigateToLogs) {
                    Text("Все логи", color = CardinalPurpleLight)
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = CardinalPurpleLight, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardinalSurfaceVariant
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    if (logs.isEmpty()) {
                        Text(
                            "Логи пока пусты. Запустите Cardinal для начала работы.",
                            color = CardinalTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        logs.takeLast(5).forEach { log ->
                            Text(
                                log,
                                color = CardinalTextSecondary,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (log != logs.takeLast(5).last()) {
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatusCard(status: CardinalBridge.CardinalStatus, uptime: String) {
    val (statusColor, statusText, statusIcon) = when (status) {
        CardinalBridge.CardinalStatus.STOPPED -> Triple(CardinalTextSecondary, "Остановлен", Icons.Default.StopCircle)
        CardinalBridge.CardinalStatus.STARTING -> Triple(CardinalGold, "Запускается...", Icons.Default.HourglassEmpty)
        CardinalBridge.CardinalStatus.RUNNING -> Triple(CardinalSuccess, "Активен", Icons.Default.CheckCircle)
        CardinalBridge.CardinalStatus.ERROR -> Triple(CardinalError, "Ошибка", Icons.Default.Error)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CardinalSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = statusColor.copy(alpha = 0.15f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(28.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Статус: $statusText", color = statusColor, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
                if (status == CardinalBridge.CardinalStatus.RUNNING) {
                    Text("Время работы: $uptime", color = CardinalTextSecondary, style = MaterialTheme.typography.bodyMedium)
                }
            }
            // Pulsing dot when running
            if (status == CardinalBridge.CardinalStatus.RUNNING) {
                Surface(
                    shape = CircleShape,
                    color = CardinalSuccess,
                    modifier = Modifier.size(12.dp)
                ) {}
            }
        }
    }
}

@Composable
private fun ActionButton(
    status: CardinalBridge.CardinalStatus,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    when (status) {
        CardinalBridge.CardinalStatus.STOPPED, CardinalBridge.CardinalStatus.ERROR -> {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CardinalPurple)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Запустить Cardinal", style = MaterialTheme.typography.titleMedium, color = CardinalTextPrimary)
            }
        }
        CardinalBridge.CardinalStatus.STARTING -> {
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = false,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CardinalGold.copy(alpha = 0.3f))
            ) {
                CircularProgressIndicator(color = CardinalGold, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Запуск...", style = MaterialTheme.typography.titleMedium, color = CardinalGold)
            }
        }
        CardinalBridge.CardinalStatus.RUNNING -> {
            Button(
                onClick = onStop,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CardinalError)
            ) {
                Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Остановить Cardinal", style = MaterialTheme.typography.titleMedium, color = CardinalTextPrimary)
            }
        }
    }
}

@Composable
private fun FeatureToggleCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    enabled: Boolean
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CardinalSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (enabled) CardinalPurple.copy(alpha = 0.15f) else CardinalSurfaceVariant,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = if (enabled) CardinalPurpleLight else CardinalTextSecondary, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = CardinalTextPrimary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyLarge)
                Text(description, color = CardinalTextSecondary, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (enabled) CardinalSuccess.copy(alpha = 0.15f) else CardinalSurfaceVariant
            ) {
                Text(
                    if (enabled) "ВКЛ" else "ВЫКЛ",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = if (enabled) CardinalSuccess else CardinalTextSecondary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = CardinalTextSecondary, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = CardinalTextPrimary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
    }
}
